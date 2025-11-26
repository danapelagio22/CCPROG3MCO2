import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * ReceiptController handles receipt display logic.
 * Provides formatted data to ReceiptView without exposing models.
 */
public class ReceiptController {
    private Receipt receipt;
    private Transaction transaction;
    
    /**
     * Constructs a ReceiptController using the given Receipt.
     *
     * @param receipt The Receipt containing the transaction data.
     */
    public ReceiptController(Receipt receipt) {
        this.receipt = receipt;
        this.transaction = receipt.getTransaction();
    }
    
    /**
     * Returns the unique transaction identifier.
     *
     * @return The transaction ID.
     */
    public String getTransactionID() {
        return transaction.getTransactionID();
    }
    
    /**
     * Returns the formatted transaction date and time.
     *
     * @return A formatted date-time string.
     */
    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return transaction.getTimeStamp().format(formatter);
    }
    
    /**
     * Returns the customer's full name.
     *
     * @return The customer name.
     */
    public String getCustomerName() {
        return transaction.getCustomer().getName();
    }
    
    /**
     * Checks whether the customer has an active membership card.
     *
     * @return true if the customer has a membership card; false otherwise.
     */
    public boolean hasMembershipCard() {
        return transaction.getCustomer().hasMembershipCard();
    }
    
    /**
     * Returns the membership card number of the customer.
     *
     * @return The membership card number, or an empty string if none exists.
     */
    public String getMembershipCardNumber() {
        if (hasMembershipCard()) {
            return transaction.getCustomer().getMembershipCard().getCardNumber();
        }
        return "";
    }
    
    /**
     * Returns the customer's membership points.
     *
     * @return The membership points, or 0 if no membership card exists.
     */
    public int getMembershipPoints() {
        if (hasMembershipCard()) {
            return transaction.getCustomer().getMembershipCard().getPoints();
        }
        return 0;
    }
    
    /**
     * Returns the list of purchased items in the transaction.
     *
     * @return An ArrayList of purchased cart items.
     */
    public ArrayList<CartItem> getPurchasedItems() {
        return transaction.getPurchasedItems();
    }
    
    /**
     * Computes and returns the subtotal of all purchased items.
     *
     * @return The subtotal amount.
     */
    public double computeSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : transaction.getPurchasedItems()) {
            subtotal += item.computeLineTotal();
        }
        return subtotal;
    }
    
    /**
     * Returns the discount amount applied to the transaction.
     *
     * @return The discount value.
     */
    public double getDiscount() {
        double subtotal = computeSubtotal();
        double afterDiscount = transaction.applyDiscounts();
        return subtotal - afterDiscount;
    }
    
    /**
     * Returns the total amount after discounts are applied.
     *
     * @return The discounted total amount.
     */
    public double getAfterDiscount() {
        return transaction.applyDiscounts();
    }
    
    /**
     * Computes and returns the value-added tax (VAT) for the transaction.
     *
     * @return The VAT amount.
     */
    public double getVAT() {
        return getAfterDiscount() * 0.12;
    }
    
    /**
     * Returns the final total cost of the transaction.
     *
     * @return The total transaction amount.
     */
    public double getTotal() {
        return transaction.getTotalCost();
    }
    
    /**
     * Checks whether payment information is available for the transaction.
     *
     * @return true if payment details exist; false otherwise.
     */
    public boolean hasPayment() {
        return transaction.getPayment() != null;
    }
    
    /**
     * Returns the amount received from the customer.
     *
     * @return The received payment amount, or 0 if no payment exists.
     */
    public double getAmountReceived() {
        if (hasPayment()) {
            return transaction.getPayment().getAmountReceived();
        }
        return 0.0;
    }
    
    /**
     * Returns the change due to the customer.
     *
     * @return The change amount, or 0 if no payment exists.
     */
    public double getChange() {
        if (hasPayment()) {
            return transaction.getPayment().computeChange();
        }
        return 0.0;
    }
    
    /**
     * Returns the name of the product in the given cart item.
     *
     * @param item The CartItem being queried.
     * @return The product name.
    */
    public String getProductName(CartItem item) {
        return item.getProduct().getName();
    }
    
    /**
     * Returns the quantity purchased for the given cart item.
     *
     * @param item The CartItem being queried.
     * @return The item quantity.
     */
    public int getItemQuantity(CartItem item) {
        return item.getQuantity();
    }
    
    /**
     * Returns the line total for the given cart item.
     *
     * @param item The CartItem being queried.
     * @return The line total amount.
     */
    public double getLineTotal(CartItem item) {
        return item.computeLineTotal();
    }
}
    

