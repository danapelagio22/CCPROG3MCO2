/**
 * Represents a single line item in a shopping cart,
 * containing a product and its selected quantity.
 */
public class CartItem {
    private Product product;
    private int quantity;

    /**
     * Constructs a CartItem with the specified product and quantity.
     * 
     * @param product the product in this cart item
     * @param quantity the quantity of the product
     */
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Computes the line total (price × quantity) for this cart item.
     *
     * @return the total price for this line item
     */
    public double computeLineTotal() {
        return product.getPrice() * quantity;
    }

    /**
     * Retrieves the product in this cart item.
     *
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Retrieves the quantity of the product in this cart item.
     *
     * @return the quantity value
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the quantity for this cart item.
     * Only positive quantities are accepted.
     *
     * @param quantity the new quantity to set
     */
    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }
}