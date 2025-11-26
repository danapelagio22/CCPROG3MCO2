import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * EmployeeController manages inventory operations.
 * Handles product management, restocking, and sales history.
 */
public class EmployeeController {
    private ConvenienceStore store;
    private Employee employee;
    private DataManager dataManager;
    private MainApplication mainApp;
    private EmployeeView view;
    
    /**
     * Constructs an EmployeeController with the specified store, employee, 
     * data manager, and main application reference.
     *
     * @param store The convenience store the employee works at.
     * @param employee The employee performing inventory actions.
     * @param dataManager Handles data persistence (products, transactions, etc.).
     * @param mainApp Reference to the main application for navigation/logout.
     */
    public EmployeeController(ConvenienceStore store, Employee employee,
                             DataManager dataManager, MainApplication mainApp) {
        this.store = store;
        this.employee = employee;
        this.dataManager = dataManager;
        this.mainApp = mainApp;
    }
    
    /**
     * Sets the associated EmployeeView for this controller.
     * Used to refresh UI or show notifications.
     *
     * @param view The EmployeeView instance to associate with this controller.
     */
    public void setView(EmployeeView view) {
        this.view = view;
    }

    /**
     * Returns the name of the current employee.
     *
     * @return Employee's full name.
     */
    public String getEmployeeName() {
        return employee.getName();
    }

    /**
     * Returns the ID of the current employee.
     *
     * @return Employee's unique ID.
     */
    public String getEmployeeID() {
        return employee.getEmployeeID();
    }

    /**
     * Flags products with low stock from the store inventory.
     *
     * @return ArrayList of products that are low in stock.
     */
    public ArrayList<Product> flagLowStock() {
        return store.getInventory().flagLowStock();
    }

    /**
     * Flags products that are expiring within a specified number of days.
     *
     * @param daysThreshold Number of days before expiration to consider.
     * @return ArrayList of products expiring within the threshold.
     */
    public ArrayList<Product> flagExpiringProducts(int daysThreshold) {
        return store.getInventory().flagExpiringProducts(daysThreshold);
    }

    /**
     * Returns all shelves from the store inventory.
     *
     * @return ArrayList of shelves.
     */
    public ArrayList<Shelf> getShelves() {
        return store.getInventory().getShelves();
    }
    
    /**
     * Handles restocking a product by the employee.
     * Validates quantity, updates inventory, persists data, and refreshes the view.
     *
     * @param product The product to restock.
     * @param quantity Amount to add to the product's current stock.
     */
    public void handleRestock(Product product, int quantity) {
        if (quantity <= 0) {
            showAlert("Invalid Quantity", "Please enter a positive quantity.", Alert.AlertType.WARNING);
            return;
        }
        
        employee.restockItem(store.getInventory(), product, quantity);
        dataManager.saveProducts(store.getInventory().getProducts());
        view.refreshInventory();
        showAlert("Success", "Product restocked successfully!", Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handles editing/updating an existing product's information.
     * Updates inventory, persists changes, and refreshes the view.
     *
     * @param updatedProduct Product object with updated information.
     */
    public void handleEditProduct(Product updatedProduct) {
        employee.updateProductInfo(store.getInventory(), updatedProduct);
        dataManager.saveProducts(store.getInventory().getProducts());
        view.refreshInventory();
        showAlert("Success", "Product updated successfully!", Alert.AlertType.INFORMATION);
    }
    
    /**
     * Handles adding a new product to the store inventory.
     * Validates inputs, creates product object, assigns it to the correct shelf,
     * persists data, and refreshes the view.
     *
     * @param id Product ID
     * @param name Product name
     * @param price Product price
     * @param stock Product stock quantity
     * @param mainCategory Main category name
     * @param subCategory Subcategory name
     * @param brand Product brand (nullable)
     * @param variant Product variant (nullable)
     * @param expDate Expiration date (nullable)
     */
    public void handleRemoveProduct(Product product) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Product");
        confirm.setHeaderText("Remove: " + product.getName());
        confirm.setContentText("Are you sure you want to remove this product from inventory?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                store.getInventory().removeProduct(product.getProductID());
                dataManager.saveProducts(store.getInventory().getProducts());
                view.refreshInventory();
                showAlert("Success", "Product removed successfully!", Alert.AlertType.INFORMATION);
            }
        });
    }
    
    /**
     * Handles adding a new product to inventory.
     */
    public void handleAddProduct(int id, String name, double price, int stock, 
                                String mainCategory, String subCategory, 
                                String brand, String variant, LocalDate expDate) {
        if (name == null || name.trim().isEmpty()) {
            showAlert("Error", "Product name is required.", Alert.AlertType.ERROR);
            return;
        }
        
        if (price < 0) {
            showAlert("Error", "Price cannot be negative.", Alert.AlertType.ERROR);
            return;
        }
        
        if (stock < 0) {
            showAlert("Error", "Stock cannot be negative.", Alert.AlertType.ERROR);
            return;
        }
        
        if (store.getInventory().productExists(id)) {
            showAlert("Error", "Product ID already exists!", Alert.AlertType.ERROR);
            return;
        }

        Category category = new Category(mainCategory, subCategory);
        Product product = new Product(id, name, price, stock, category, brand, variant, expDate);

        employee.addProduct(store.getInventory(), product);
        dataManager.saveProducts(store.getInventory().getProducts());

        boolean shelfFound = false;
        for (Shelf shelf : store.getInventory().getShelves()) {
            if (shelf.getCategory().getName().equals(category.getName()) &&
                shelf.getCategory().getType().equals(category.getType())) {
                shelf.addProduct(product);
                shelfFound = true;
                break;
            }
        }

        if (!shelfFound) {
            Shelf newShelf = new Shelf(category);
            newShelf.addProduct(product);
            store.getInventory().addShelf(newShelf);
        }
        
        view.refreshInventory();
        view.showAddProductSuccess();
    }
    
    /**
     * Handles employee logout.
     * Prompts for confirmation and calls main application logout.
     */
    public void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText("Logout Confirmation");
        confirm.setContentText("Are you sure you want to logout?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mainApp.logout();
            }
        });
    }
    
    /**
     * Returns the DataManager used for inventory and transaction persistence.
     *
     * @return DataManager instance.
     */
    public DataManager getDataManager() {
        return dataManager;
    }
    
    /**
     * Displays an alert dialog with the specified title, message, and alert type.
     *
     * @param title The title of the alert window.
     * @param message The message content to display.
     * @param type The type of alert (e.g., INFORMATION, WARNING, ERROR, CONFIRMATION).
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}