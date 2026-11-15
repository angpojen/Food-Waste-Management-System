
package model;

public abstract class Food {

    // Attributes
    private String foodId;
    private String foodName;
    private String category;
    private double quantity;
    private String unit;

    // Constructor
    public Food(String foodId, String foodName,
                String category, double quantity, String unit) {

        this.foodId = foodId;
        this.foodName = foodName;
        this.category = category;
        setQuantity(quantity);
        this.unit = unit;
    }

    // Getters
    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getCategory() {
        return category;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    // Setters
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(double quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative."
            );
        }

        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Abstract method
    public abstract String getFoodType();
}
