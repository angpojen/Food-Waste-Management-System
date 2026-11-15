
package model;

public class NonPerishableFood extends Food {

    public NonPerishableFood(String foodId, String foodName,
                             String category, double quantity,
                             String unit) {

        super(foodId, foodName, category, quantity, unit);
    }

    @Override
    public String getFoodType() {
        return "Non-Perishable";
    }
}

