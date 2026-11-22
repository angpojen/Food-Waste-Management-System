
package model;

/**
 * Represents food items that do not have an expiry date.
 * This class extends the Food class.
 */
public class NonPerishableFood extends Food {

    /**
     * Creates a non-perishable food item.
     *
     * @param foodId unique food ID
     * @param foodName name of the food
     * @param category food category
     * @param quantity available quantity
     * @param unit measurement unit
     */
    public NonPerishableFood(String foodId, String foodName,
                             String category, double quantity,
                             String unit) {

        super(foodId, foodName, category, quantity, unit);
    }

    /**
     * Returns the type of this food item.
     *
     * @return "Non-Perishable"
     */
    @Override
    public String getFoodType() {
        return "Non-Perishable";
    }
}
