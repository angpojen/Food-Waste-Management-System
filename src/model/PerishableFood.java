
package model;

import java.time.LocalDate;

public class PerishableFood extends Food {

    private LocalDate expiryDate;

    public PerishableFood(String foodId, String foodName,
                          String category, double quantity,
                          String unit, LocalDate expiryDate) {

        super(foodId, foodName, category, quantity, unit);
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getFoodType() {
        return "Perishable";
    }
}

