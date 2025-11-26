import java.util.ArrayList;

/**
 * Represents a shelf or display area in the store, dedicated to a specific category.
 * It holds a collection of Product objects that match its category.
 */
public class Shelf {
    private Category category;
    private ArrayList<Product> products;

    /**
     * Constructs a new Shelf for a specific product category.
     *
     * @param category The category (main and sub) of products this shelf will hold.
     */
    public Shelf(Category category) {
        this.category = category;
        this.products = new ArrayList<>();
    }

    /**
     * Adds a product to the shelf, but only if the product's category matches the shelf's category.
     *
     * @param product The product to add.
     */
    public void addProduct(Product product) {
        if (product.getCategory().getName().equalsIgnoreCase(category.getName())) {
            products.add(product);
        } 
        else {
            System.out.println("Product category does not match shelf category.\n");
        }
    }

    /**
     * Returns the category assigned to this shelf.
     *
     * @return The shelf category.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the list of products stored on this shelf.
     *
     * @return An ArrayList of products on the shelf.
     */
    public ArrayList<Product> getProducts() {
        return products;
    }
}