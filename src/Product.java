import java.time.LocalDate;

/**
 * Represents a product available in the convenience store inventory.
 * Stores information like ID, name, price, stock, category, brand, variant,
 * and expiration date.
 */
class Product {
    private int productID;
    private String name;
    private double price;
    private int stock;
    private Category category;
    private String brand;
    private String variant;
    private LocalDate expirationDate;
    private boolean isPerishable;

    /**
     * Constructs a Product with essential general attributes.
     * Brand, variant, and expiration date are set to null.
     *
     * @param productID The unique ID of the product.
     * @param name The name of the product.
     * @param price The price of the product.
     * @param stock The initial stock quantity.
     * @param category The product's category.
     */
    public Product(int productID, String name, double price, int stock, Category category) {
        this(productID, name, price, stock, category, null, null, null);
    }

    /**
     * Constructs a Product with all possible attributes, including brand, variant, and expiration date.
     * The `isPerishable` flag is set based on the presence of an `expirationDate`.
     *
     * @param productID The unique ID of the product.
     * @param name The name of the product.
     * @param price The price of the product.
     * @param stock The initial stock quantity.
     * @param category The product's category.
     * @param brand The brand of the product (can be null).
     * @param variant The variant or size of the product (can be null).
     * @param expirationDate The expiration date (can be null).
     */
    public Product(int productID, String name, double price, int stock,
                   Category category, String brand, String variant, LocalDate expirationDate) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.brand = brand;
        this.variant = variant;
        this.expirationDate = expirationDate;
        this.isPerishable = (expirationDate != null);
    }

    /**
     * Decrements the stock of the product by the specified quantity.
     *
     * @param quantity The number of units to remove from stock.
     * @return true if stock was successfully reduced, false if there was insufficient stock.
     */
    public boolean reduceStock(int quantity) {
        if (quantity > 0 && quantity <= stock) {
            stock -= quantity;
            return true;
        }
        else {
            System.out.println("Insufficient stock for " + name + ".\n");
            return false;
        }
    }

    /**
     * Increases the stock of the product by the specified quantity.
     *
     * @param quantity The number of units to add to stock. Must be positive.
     */
    public void restock(int quantity) {
        if (quantity > 0) {
            stock += quantity;
        }
        else {
            System.out.println("Invalid Quantity.\n");
        }
    }
    
    /**
     * Sets the name of the product.
     *
     * @param name The new product name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the price of the product.
     * Price values less than zero are ignored.
     *
     * @param price The new price of the product.
     */
    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    /**
     * Sets the brand of the product.
     *
     * @param brand The product brand.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Sets the variant of the product.
     *
     * @param variant The product variant or size.
     */
    public void setVariant(String variant) {
        this.variant = variant;
    }

    /**
     * Sets the expiration date of the product.
     * Automatically updates the perishable status.
     *
     * @param expirationDate The expiration date (may be {@code null}).
     */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        this.isPerishable = (expirationDate != null);
    }

    /**
     * @return The unique product ID.
     */
    public int getProductID() {
        return productID;
    }

    /**
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The price of the product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return The number of units currently in stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return The category to which the product belongs.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @return The brand of the product, or {@code null} if none is set.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @return The product variant or size, or {@code null} if none is set.
     */
    public String getVariant() {
        return variant;
    }

    /**
     * @return The expiration date of the product, or {@code null} if non-perishable.
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Indicates whether the product is perishable.
     *
     * @return {@code true} if the product has an expiration date,
     *         {@code false} otherwise.
     */
    public boolean isPerishable() {
        return isPerishable;
    }
}
