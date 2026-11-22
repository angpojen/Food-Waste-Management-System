
package model;

/**
 * Represents a general food item in the Food Waste Management System.
 */
public abstract class Food {

    private String foodId;
    private String foodName;
    private String category;
    private double quantity;
    private String unit;

    /**
     * Creates a food item with the given details.
     *
     * @param foodId unique food ID
     * @param foodName name of the food
     * @param category food category
     * @param quantity available quantity
     * @param unit measurement unit
     */
    public Food(String foodId, String foodName,
                String category, double quantity, String unit) {

        this.foodId = foodId;
        setFoodName(foodName);
        setCategory(category);
        setQuantity(quantity);
        setUnit(unit);
    }

    /**
     * Returns the food ID.
     *
     * @return food ID
     */
    public String getFoodId() {
        return foodId;
    }

    /**
     * Returns the food name.
     *
     * @return food name
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * Returns the food category.
     *
     * @return food category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the food quantity.
     *
     * @return food quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Returns the measurement unit.
     *
     * @return measurement unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Updates the food name after validation.
     *
     * @param foodName new food name
     */
    public void setFoodName(String foodName) {
        if (foodName == null || foodName.trim().isEmpty()) {
            throw new IllegalArgumentException("Food name cannot be empty.");
        }
        this.foodName = foodName.trim();
    }

    /**
     * Updates the food category after validation.
     *
     * @param category new food category
     */
    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category.trim();
    }

    /**
     * Updates the food quantity after validation.
     *
     * @param quantity new food quantity
     */
    public void setQuantity(double quantity) {
        if (!Double.isFinite(quantity) || quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity must be a valid non-negative number."
            );
        }
        this.quantity = quantity;
    }

    /**
     * Updates the food unit after validation.
     *
     * @param unit new measurement unit
     */
    public void setUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be empty.");
        }
        this.unit = unit.trim();
    }

    /**
     * Returns the type of food.
     *
     * @return food type
     */
    public abstract String getFoodType();
}
