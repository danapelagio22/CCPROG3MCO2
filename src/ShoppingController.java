import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * ShoppingController handles product browsing and adding items to cart.
 */
public class ShoppingController {
    private Customer customer;
    private ConvenienceStore store;
    private MainApplication mainApp;
    
    private CustomerView view;
    
    /**
     * Constructs a ShoppingController with the specified customer, store,
     * and main application reference.
     *
     * @param customer The current customer using the shopping screen.
     * @param store The convenience store containing inventory data.
     * @param mainApp The main application used for view navigation.
     */
    public ShoppingController(Customer customer, ConvenienceStore store, MainApplication mainApp) {
        this.customer = customer;
        this.store = store;
        this.mainApp = mainApp;
    }
    
    /**
     * Sets the CustomerView associated with this controller.
     *
     * @param view The CustomerView instance.
     */
    public void setView(CustomerView view) {
        this.view = view;
    }

    /**
     * Returns the name of the convenience store.
     *
     * @return The store name.
     */
    public String getStoreName() {
        return store.getName();
    }

    /**
     * Returns the location of the convenience store.
     *
     * @return The store location.
     */
    public String getStoreLocation() {
        return store.getLocation();
    }

    /**
     * Returns the list of shelves in the store inventory.
     *
     * @return An ArrayList of shelves.
     */
    public ArrayList<Shelf> getShelves() {
        return store.getInventory().getShelves();
    }

    /**
     * Returns the name of the currently logged-in customer.
     *
     * @return The customer's name.
     */
    public String getCustomerName() {
        return customer.getName();
    }

    /**
     * Returns the total number of items currently in the cart.
     *
     * @return The cart item count.
     */
    public int getCartItemCount() {
        return customer.getCart().getItems().size();
    }

    /**
     * Checks whether the customer has an active membership card.
     *
     * @return true if the customer has a membership card; false otherwise.
     */
    public boolean hasMembershipCard() {
        return customer.hasMembershipCard();
    }

    /**
     * Returns the customer's current membership points.
     *
     * @return The membership points, or 0 if no membership card exists.
     */
    public int getMembershipPoints() {
        if (customer.hasMembershipCard()) {
            return customer.getMembershipCard().getPoints();
        }
        return 0;
    }
    
    /**
     * Handles adding a product to the customer's cart.
     * Validates stock availability before adding.
     *
     * @param product The product being added to the cart.
     * @param quantity The quantity to add.
     */
    public void handleAddToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }
        
        if (quantity > product.getStock()) {
            showAlert("Insufficient Stock", 
                     "Only " + product.getStock() + " units available.", 
                     Alert.AlertType.WARNING);
            return;
        }
        
        customer.addToCart(product, quantity);
        view.updateCartCount();
        view.showNotification("Added " + quantity + "x " + product.getName() + " to cart");
    }
    
    /**
     * Handles navigating to cart view.
     */
    public void handleViewCart() {
        if (customer.getCart().isEmpty()) {
            showAlert("Empty Cart", "Your cart is empty. Add some items first!", Alert.AlertType.WARNING);
            return;
        }
        
        mainApp.showCartView();
    }
    
    /**
     * Handles logout.
     */
    public void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText("Logout Confirmation");
        confirm.setContentText("Are you sure you want to logout?\nYour cart will be cleared.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mainApp.logout();
            }
        });
    }
    
    /**
     * Displays a standardized alert dialog.
     *
     * @param title The alert title.
     * @param message The alert message.
     * @param type The type of alert to display.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}