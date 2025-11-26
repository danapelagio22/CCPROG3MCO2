import java.util.ArrayList;

/**
 * Represents a shopping cart containing items selected by a customer.
 * Provides operations for adding, removing, and retrieving cart items,
 * as well as computing the subtotal.
 */
public class Cart {
    private ArrayList<CartItem> items;

    /**
     * Constructs an empty Cart.
     */
    public Cart() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a product to the cart with the specified quantity.
     * If the product already exists in the cart, updates the quantity.
     *
     * @param product the product to add
     * @param quantity the quantity to add
     */
    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }

        for (CartItem item : items) {
            if (item.getProduct().getProductID() == product.getProductID()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        items.add(new CartItem(product, quantity));
    }

    /**
     * Removes a product from the cart.
     * If the product does not exist in the cart, nothing happens.
     *
     * @param product the product to remove
     */
    public void removeItem(Product product) {
        if (product == null) {
            return;
        }

        items.removeIf(item -> item.getProduct().getProductID() == product.getProductID());
    }

    /**
     * Computes the subtotal of all items in the cart.
     * Subtotal does not include taxes or discounts.
     *
     * @return the total cost of all items before tax/discounts
     */
    public double computeSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : items) {
            subtotal += item.computeLineTotal();
        }
        return subtotal;
    }

     /**
     * Retrieves all items currently in the cart.
     *
     * @return a list of CartItem objects
     */
    public ArrayList<CartItem> getItems() {
        return items;
    }

    /**
     * Clears all items from the cart.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Checks if the cart is empty.
     *
     * @return true if cart has no items, false otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}