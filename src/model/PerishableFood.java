
package model;

import java.time.LocalDate;

/**
 * Represents food items that have an expiry date.
 * This class extends the Food class.
 */
public class PerishableFood extends Food {

    private LocalDate expiryDate;

    /**
     * Creates a perishable food item.
     *
     * @param foodId unique food ID
     * @param foodName name of the food
     * @param category food category
     * @param quantity available quantity
     * @param unit measurement unit
     * @param expiryDate expiry date of the food
     */
    public PerishableFood(String foodId, String foodName,
                          String category, double quantity,
                          String unit, LocalDate expiryDate) {

        super(foodId, foodName, category, quantity, unit);
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the expiry date.
     *
     * @return expiry date
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * Updates the expiry date.
     *
     * @param expiryDate new expiry date
     */
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the type of this food item.
     *
     * @return "Perishable"
     */
    @Override
    public String getFoodType() {
        return "Perishable";
    }
}
