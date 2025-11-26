import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Handles cart operations such as modifying item quantities,
 * removing items, clearing the cart, and navigating to checkout.
 */
public class CartController {
    private Cart cart;
    private MainApplication mainApp;
    private CartView view;

    /**
     * Constructs a CartController with the specified cart and application reference.
     *
     * @param cart the shopping cart being managed
     * @param mainApp the main application reference used for navigation
     */
    public CartController(Cart cart, MainApplication mainApp) {
        this.cart = cart;
        this.mainApp = mainApp;
    }
    
    /**
     * Assigns the CartView instance to this controller.
     *
     * @param view the view associated with the cart
     */
    public void setView(CartView view) {
        this.view = view;
    }

     /**
     * Checks whether the cart contains any items.
     *
     * @return true if the cart is empty, false otherwise
     */
    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    /**
     * Retrieves all items currently stored in the cart.
     *
     * @return a list of CartItem objects
     */
    public ArrayList<CartItem> getCartItems() {
        return cart.getItems();
    }

     /**
     * Computes the subtotal of all cart items before tax or discounts.
     *
     * @return the subtotal amount
     */
    public double computeSubtotal() {
        return cart.computeSubtotal();
    }

     /**
     * Handles quantity changes for a specific cart item.
     * If the new quantity is zero or below, the item is removed.
     * If stock is insufficient, an alert is shown.
     *
     * @param item the cart item to update
     * @param newQuantity the updated quantity value
     */
    public void handleQuantityChange(CartItem item, int newQuantity) {
        if (newQuantity <= 0) {
            handleRemoveItem(item);
            return;
        }

        if (newQuantity > item.getProduct().getStock()) {
            showAlert("Insufficient Stock",
                    "Only " + item.getProduct().getStock() + " units available.",
                    Alert.AlertType.WARNING);
            view.refreshCartDisplay();
            return;
        }

        item.setQuantity(newQuantity);
        view.refreshCartDisplay();
    }

    /**
     * Handles removing a specific item from the cart.
     * Displays a confirmation prompt before removal.
     *
     * @param item the cart item to remove
     */
    public void handleRemoveItem(CartItem item) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Remove Item");
        confirmAlert.setHeaderText("Remove from cart?");
        confirmAlert.setContentText("Remove " + item.getProduct().getName() + " from your cart?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                cart.removeItem(item.getProduct());
                view.refreshCartDisplay();
            }
        });
    }

    /**
     * Handles clearing the entire cart.
     */
    public void handleClearCart() {
        if (cart.isEmpty()) {
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Clear Cart");
        confirmAlert.setHeaderText("Clear entire cart?");
        confirmAlert.setContentText("This will remove all items from your cart.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                cart.clear();
                view.refreshCartDisplay();
                showAlert("Cart Cleared", "All items removed from cart.", Alert.AlertType.INFORMATION);
            }
        });
    }

    /**
     * Handles proceeding to checkout.
     */
    public void handleCheckout() {
        if (cart.isEmpty()) {
            showAlert("Empty Cart", "Please add items to your cart first.", Alert.AlertType.WARNING);
            return;
        }

        mainApp.showCheckoutView();
    }
    
    /**
     * Handles going back to shopping.
     */
    public void handleBackToShopping() {
        mainApp.showCustomerView();
    }

    /**
     * Displays a simple alert with the specified title, message, and type.
     *
     * @param title the alert title
     * @param message the message content
     * @param type the alert type
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}