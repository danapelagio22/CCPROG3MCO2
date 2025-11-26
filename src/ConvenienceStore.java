import java.util.ArrayList;

/**
 * Represents the main convenience store entity.
 * It holds the store's name, location, and manages its inventory.
 * 
 */
public class ConvenienceStore {
    private String name;
    private String location;
    private Inventory inventory;
    private ArrayList<Transaction> salesHistory;

    /**
     * Constructs a new ConvenienceStore with a name and location, and initializes an empty inventory.
     *
     * @param name The name of the store.
     * @param location The location of the store.
     */
    public ConvenienceStore(String name, String location) {
        this.name = name;
        this.location = location;
        this.inventory = new Inventory();
        this.salesHistory = new ArrayList<>();
    }

    /**
     * Adds a transaction to the store's sales history.
     *
     * @param transaction The transaction to save
     */
    public void saveToSalesHistory(Transaction transaction) {
        salesHistory.add(transaction);
    }

    /**
     * Returns the sales history list.
     *
     * @return The ArrayList of transactions.
     */
    public ArrayList<Transaction> getSalesHistory() {
        return salesHistory;
    }

    /**
     * Returns the store's inventory.
     *
     * @return The Inventory object managed by the store
     */
    public Inventory getInventory() {
        return inventory;
    }

     /**
     * Returns the store's name.
     *
     * @return The name of the store
     */
    public String getName() {
        return name;
    }

     /**
     * Returns the store's location.
     *
     * @return The location of the store
     */
    public String getLocation() {
        return location;
    }
}