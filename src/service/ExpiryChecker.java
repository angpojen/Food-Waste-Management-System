
package service;

import model.Food;
import model.PerishableFood;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Checks expired and soon-to-expire food items.
 */
public class ExpiryChecker {

    /**
     * Manages the food inventory.
     */
    private FoodManager foodManager;

    /**
     * Creates an expiry checker using the food manager.
     *
     * @param foodManager food inventory manager
     */
    public ExpiryChecker(FoodManager foodManager) {
        this.foodManager = foodManager;
    }

    /**
     * Returns expired food and food expiring within 3 days.
     * Items with zero quantity are excluded.
     *
     * @return list of expired and soon-to-expire food items
     */
    public ArrayList<PerishableFood> getExpiringFoods() {

        ArrayList<PerishableFood> expiringFoods =
                new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate limitDate = today.plusDays(3);

        for (Food food : foodManager.getAllFoods()) {

            if (food instanceof PerishableFood) {

                PerishableFood perishable =
                        (PerishableFood) food;

                LocalDate expiryDate =
                        perishable.getExpiryDate();

                if (food.getQuantity() > 0 &&
                        expiryDate != null &&
                        !expiryDate.isAfter(limitDate)) {

                    expiringFoods.add(perishable);
                }
            }
        }

        return expiringFoods;
    }
}
