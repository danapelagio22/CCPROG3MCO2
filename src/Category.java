/**
 * Represents a category for products, composed of a main category (e.g., Food, Beverages)
 * and a more specific sub-category (e.g., Vegetable, Juice).
 */
public class Category {
    private String mainCategory;
    private String subCategory;

    /**
     * Constructs a new Category with the specified main and sub-categories.
     *
     * @param mainCategory The category name.
     * @param subCategory The specific type within the main category.
     */
    public Category(String mainCategory, String subCategory) {
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
    }

    /**
     * Returns the main category name.
     *
     * @return the main category
     */
    public String getName() { 
        return mainCategory; 
    }

    /**
     * Returns the sub-category name.
     *
     * @return the sub-category
     */
    public String getType() { 
        return subCategory; 
    }

     /**
     * Sets the main category name.
     *
     * @param mainCategory the new main category
     */
    public String setName(String mainCategory) { 
        return this.mainCategory = mainCategory; 
    }

    /**
     * Sets the sub-category name.
     *
     * @param subCategory the new sub-category
     */
    public String setType(String subCategory) { 
        return this.subCategory = subCategory; 
    }
}