import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * The Transaction class represents a completed purchase.
 * It keeps track of the customer, the items they bought,
 * how they paid, and automatically computes the total cost with tax.
 * 
 * @author Dana Ysabelle A. Pelagio
 */
public class Transaction {
    private String transactionID;
    private Customer customer;
    private ArrayList<CartItem> purchasedItems;
    private double totalCost;
    private Payment payment;
    private LocalDateTime timeStamp;
    private static final double VAT_RATE = 0.12;

    /**
     * Creates a new Transaction with all needed details including payment.
     *
     * @param transactionID unique ID for this transaction
     * @param customer the customer who made the purchase
     * @param purchasedItems list of all items bought
     * @param payment payment information
     */
    public Transaction(String transactionID, Customer customer, ArrayList<CartItem> purchasedItems, Payment payment) {
        this.transactionID = transactionID;
        this.customer = customer;
        this.purchasedItems = new ArrayList<>(purchasedItems);
        this.payment = payment;
        this.timeStamp = LocalDateTime.now();
        this.totalCost = calculateTotalWithTax();
    }

    /**
     * Creates a new Transaction without payment (payment added later during checkout).
     *
     * @param transactionID unique ID for this transaction
     * @param customer the customer who made the purchase
     * @param purchasedItems list of all items bought
     * @param totalCost the subtotal before tax
     */
    public Transaction(String transactionID, Customer customer, ArrayList<CartItem> purchasedItems, double totalCost) {
        this.transactionID = transactionID;
        this.customer = customer;
        this.purchasedItems = new ArrayList<>(purchasedItems);
        this.payment = null; 
        this.timeStamp = LocalDateTime.now();
        this.totalCost = calculateTotalWithTax();
    }

    /**
     * Calculates the total amount with VAT added.
     *
     * @return total cost including tax
     */
    public double calculateTotalWithTax() {
        double subtotal = 0.0;
        for (CartItem item : purchasedItems) {
            subtotal += item.computeLineTotal();
        }

        double discountedSubtotal = applyDiscounts();
        double vat = discountedSubtotal * VAT_RATE;
        return discountedSubtotal + vat;
    }

    /**
     * Applies discounts (like membership or senior discounts)
     * to the total before adding VAT.
     *
     * @return discounted subtotal
     */
    public double applyDiscounts() {
        double subtotal = 0.0;
        for (CartItem item : purchasedItems) {
            subtotal += item.computeLineTotal();
        }

        double discountedTotal = subtotal;

        if (customer.hasMembershipCard()) {
            discountedTotal = DiscountPolicy.applyMembershipDiscount(customer, discountedTotal);
        }

        return discountedTotal;
    }

    /**
     * Makes a receipt for this transaction.
     *
     * @return a Receipt object
     */
    public Receipt generateReceipt() {
        return new Receipt(this);
    }

    /**
     * Sets the payment information associated with this transaction.
     *
     * @param payment The Payment object used for this transaction.
     */
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    /**
     * Returns the unique transaction identifier.
     *
     * @return The transaction ID.
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Returns the customer who made the transaction.
     *
     * @return The Customer associated with this transaction.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the list of items purchased in this transaction.
     *
     * @return An ArrayList of purchased cart items.
     */
    public ArrayList<CartItem> getPurchasedItems() {
        return purchasedItems;
    }

    /**
     * Returns the total cost of the transaction.
     *
     * @return The total transaction cost.
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Returns the payment information for this transaction.
     *
     * @return The Payment object used in this transaction.
     */
    public Payment getPayment() {
        return payment;
    }
    
    /**
     * Returns the timestamp indicating when the transaction occurred.
     *
     * @return The date and time of the transaction.
     */
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}