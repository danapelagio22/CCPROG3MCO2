/**
 * Represents a customer of the convenience store.
 * Extends the User class and includes a shopping cart and optional membership card.
 * Provides methods for cart management, checking out, and membership handling.
 */
public class Customer extends User {
    private MembershipCard membershipCard;
    private Cart cart;

    /**
     * Constructs a new Customer with the specified name, username, and password.
     * Initializes an empty shopping cart.
     *
     * @param name the customer's full name
     * @param username the customer's username
     * @param password the customer's password
     */
    public Customer(String name, String username, String password) {
        super(name, username, password);
        this.cart = new Cart();
    }

    /**
     * Checks if the customer has a membership card.
     *
     * @return true if a membership card is assigned, false otherwise
     */
    public boolean hasMembershipCard() { 
        return membershipCard != null; 
    }

    /**
     * Returns the customer's membership card.
     *
     * @return the MembershipCard object, or null if none assigned
     */
    public MembershipCard getMembershipCard() { 
        return membershipCard; 
    }

    /**
     * Assigns a membership card to the customer.
     *
     * @param card the MembershipCard to assign
     */
    public void setMembershipCard(MembershipCard card) { 
        this.membershipCard = card; 
    }

    /**
     * Returns the customer's current shopping cart.
     *
     * @return the Cart object
     */
    public Cart getCart() { 
        return cart; 
    }

    /**
     * Adds a product and specified quantity to the customer's cart.
     *
     * @param product the Product to add
     * @param quantity the number of units to add
     */
    public void addToCart(Product product, int quantity) { 
        cart.addItem(product, quantity); 
    }

    /**
     * Computes the running total (subtotal) of the items in the cart.
     *
     * @return the subtotal amount
     */
    public double viewRunningTotal() { 
        return cart.computeSubtotal(); 
    }

    /**
     * Performs checkout for the customer, creating a transaction, updating inventory,
     * saving it to the store's sales history, and resetting the cart.
     *
     * @param store the ConvenienceStore where the checkout occurs
     * @return the Transaction representing this checkout
     */
    public Transaction checkOut(ConvenienceStore store) {
        double subtotal = cart.computeSubtotal();
        Transaction txn = new Transaction(
            "TXN-" + System.currentTimeMillis(), this, cart.getItems(), subtotal
        );
        store.getInventory().autoReduceStock(cart);
        store.saveToSalesHistory(txn);
        cart = new Cart();
        return txn;
    }
}
