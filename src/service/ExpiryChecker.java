
package service;

import model.Food;
import model.PerishableFood;

import java.time.LocalDate;
import java.util.ArrayList;

public class ExpiryChecker {

    private FoodManager foodManager;

    public ExpiryChecker(FoodManager foodManager) {
        this.foodManager = foodManager;
    }

    public ArrayList<PerishableFood> getExpiringFoods() {

        ArrayList<PerishableFood> expiringFoods = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate limitDate = today.plusDays(3);

        for (Food food : foodManager.getAllFoods()) {

            if (food instanceof PerishableFood) {

                PerishableFood perishable = (PerishableFood) food;

                LocalDate expiryDate = perishable.getExpiryDate();

                if (expiryDate != null &&
                        !expiryDate.isBefore(today) &&
                        !expiryDate.isAfter(limitDate)) {

                    expiringFoods.add(perishable);
                }
            }
        }

        return expiringFoods;
    }
}

