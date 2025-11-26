import java.util.ArrayList;

import javafx.scene.control.Alert;

/**
 * CheckoutController handles the payment process.
 * Manages pricing calculations, discounts, membership cards, and transaction completion.
 */
public class CheckoutController {
    private Customer customer;
    private Cart cart;
    private ConvenienceStore store;
    private DataManager dataManager;
    private MainApplication mainApp;
    private CheckoutView view;

    private double currentSubtotal;
    private double currentDiscount;
    private double currentVAT;
    private double currentTotal;

    /**
     * Constructs a CheckoutController with the specified customer, cart, store, data manager, and main application.
     *
     * @param customer the customer making the purchase
     * @param cart the cart containing products
     * @param store the convenience store instance
     * @param dataManager the data manager for persistence
     * @param mainApp the main application instance for navigation
     */
    public CheckoutController(Customer customer, Cart cart, ConvenienceStore store,
                             DataManager dataManager, MainApplication mainApp) {
        this.customer = customer;
        this.cart = cart;
        this.store = store;
        this.dataManager = dataManager;
        this.mainApp = mainApp;
    }
       
    /**
     * Sets the associated view for this controller.
     * Automatically recalculates pricing upon view attachment.
     *
     * @param view the CheckoutView instance
     */
    public void setView(CheckoutView view) {
        this.view = view;
        recalculatePricing();
    }

    /**
     * Returns the list of items currently in the cart.
     *
     * @return the list of CartItem objects
     */
    public ArrayList<CartItem> getCartItems() {
        return cart.getItems();
    }

    /**
     * Checks whether the customer has a membership card.
     *
     * @return true if the customer has a membership card, false otherwise
     */
    public boolean hasMembershipCard() {
        return customer.hasMembershipCard();
    }

     /**
     * Returns the customer's membership card.
     *
     * @return the MembershipCard object if present, null otherwise
     */
    public MembershipCard getMembershipCard() {
        return customer.getMembershipCard();
    }

    /**
     * Applies a membership card to the customer.
     * Validates input, sets the card, updates the view, and recalculates pricing.
     *
     * @param cardNumber the membership card number to apply
     */
    public void handleApplyMembershipCard(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            showAlert("Invalid Card", "Please enter a card number.", Alert.AlertType.WARNING);
            return;
        }

        MembershipCard card = new MembershipCard(cardNumber.trim());
        customer.setMembershipCard(card);

        showAlert("Card Applied", "Membership card successfully applied!", Alert.AlertType.INFORMATION);
        view.updateMembershipDisplay();
        recalculatePricing();
    }

    /**
     * Recalculates pricing with all applicable discounts and VAT.
     * Updates currentSubtotal, currentDiscount, currentVAT, and currentTotal.
     * Updates the view with the latest pricing.
     */
    public void recalculatePricing() {
        currentSubtotal = cart.computeSubtotal();
        currentDiscount = 0.0;

        double afterDiscount = currentSubtotal;

        if (view.isSeniorDiscountSelected()) {
            double seniorDiscounted = DiscountPolicy.applySeniorDiscount(afterDiscount);
            currentDiscount += (afterDiscount - seniorDiscounted);
            afterDiscount = seniorDiscounted;
        }

        if (view.isUseMembershipPointsSelected() && customer.hasMembershipCard()) {
            MembershipCard card = customer.getMembershipCard();
            double pointsDiscount = Math.min(card.getDiscount(), afterDiscount);
            currentDiscount += pointsDiscount;
            afterDiscount -= pointsDiscount;
        }

        currentVAT = DiscountPolicy.calculateVAT(afterDiscount);

        currentTotal = afterDiscount + currentVAT;

        view.displayPricing(currentSubtotal, currentDiscount, currentVAT, currentTotal);
    }

    /**
     * Handles changes to the amount received input.
     * Calculates and displays change, or indicates invalid input.
     *
     * @param amountText the text input representing the amount received
     */
    public void handleAmountChanged(String amountText) {
        try {
            double amount = Double.parseDouble(amountText);
            double change = amount - currentTotal;
            view.displayChange(change);
        } catch (NumberFormatException e) {
            view.displayChange(-1); 
        }
    }

    /**
     * Processes the payment and completes the transaction.
     * Validates amount, updates membership points, creates transaction,
     * saves data, generates receipt, shows receipt, and navigates back to main view.
     */
    public void handleProcessPayment() {
        String amountText = view.getAmountReceived();
        if (amountText == null || amountText.trim().isEmpty()) {
            showAlert("Invalid Payment", "Please enter payment amount.", Alert.AlertType.WARNING);
            return;
        }

        double amountReceived;
        try {
            amountReceived = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showAlert("Invalid Payment", "Please enter a valid number.", Alert.AlertType.ERROR);
            return;
        }

        if (amountReceived < currentTotal) {
            showAlert("Insufficient Payment",
                    String.format("Payment is insufficient. Need ₱%.2f more.",
                            currentTotal - amountReceived),
                    Alert.AlertType.WARNING);
            return;
        }

        Payment payment = new Payment(amountReceived, currentTotal);

        if (customer.hasMembershipCard()) {
            MembershipCard card = customer.getMembershipCard();

            if (view.isUseMembershipPointsSelected()) {
                int pointsToRedeem = Math.min(card.getPoints(), (int)currentDiscount);
                card.redeemPoints(pointsToRedeem);
            }

            card.addPoints(currentTotal);
        }

        Transaction transaction = customer.checkOut(store);
        transaction.setPayment(payment);

        if (customer.hasMembershipCard()) {
            dataManager.updateCustomer(customer);
        }
        dataManager.saveProducts(store.getInventory().getProducts());
        dataManager.saveTransaction(transaction);

        Receipt receipt = transaction.generateReceipt();
        receipt.setDataManager(dataManager);
        receipt.saveToFile();

        ReceiptController receiptController = new ReceiptController(receipt);
        ReceiptView receiptView = new ReceiptView(receiptController);
        receiptView.show();

        showAlert("Payment Successful",
                String.format("Change: ₱%.2f\nReceipt saved automatically.\nThank you for shopping!",
                        payment.computeChange()),
                Alert.AlertType.INFORMATION);

        mainApp.showCustomerView();
    }

    /**
     * Handles going back to cart.
     */
    public void handleBack() {
        mainApp.showCartView();
    }

    /**
     * Displays an alert dialog with the specified title, message, and alert type.
     *
     * @param title the title of the alert
     * @param message the content message
     * @param type the type of the alert (e.g., INFORMATION, WARNING, ERROR)
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}