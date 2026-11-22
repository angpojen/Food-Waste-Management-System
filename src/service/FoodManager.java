
package service;

import model.Food;
import model.PerishableFood;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Manages food items in the inventory.
 */
public class FoodManager {

    /**
     * Stores all food items in the inventory.
     */
    private ArrayList<Food> foodList;

    /**
     * Initializes an empty food inventory.
     */
    public FoodManager() {
        foodList = new ArrayList<>();
    }

    /**
     * Adds a new food item if its information is valid
     * and its ID does not already exist.
     *
     * @param food food item to be added
     * @return true if added successfully, otherwise false
     */
    public boolean addFood(Food food) {

        if (food == null ||
                isBlank(food.getFoodId()) ||
                isBlank(food.getFoodName()) ||
                isBlank(food.getCategory()) ||
                isBlank(food.getUnit()) ||
                !Double.isFinite(food.getQuantity()) ||
                food.getQuantity() < 0) {
            return false;
        }

        if (findFoodById(food.getFoodId()) != null) {
            return false;
        }

        foodList.add(food);
        return true;
    }

    /**
     * Returns a copy of all food items.
     *
     * @return list of food items
     */
    public ArrayList<Food> getAllFoods() {
        return new ArrayList<>(foodList);
    }

    /**
     * Finds a food item using its ID.
     *
     * @param foodId ID of the food item
     * @return matching food item, or null if not found
     */
    public Food findFoodById(String foodId) {

        if (isBlank(foodId)) {
            return null;
        }

        for (Food food : foodList) {
            if (food.getFoodId().equalsIgnoreCase(foodId.trim())) {
                return food;
            }
        }

        return null;
    }

    /**
     * Deletes a food item using its ID.
     *
     * @param foodId ID of the food item to delete
     * @return true if deleted successfully, otherwise false
     */
    public boolean deleteFood(String foodId) {

        Food food = findFoodById(foodId);

        if (food != null) {
            foodList.remove(food);
            return true;
        }

        return false;
    }

    /**
     * Updates food information including the expiry date.
     *
     * @param foodId ID of the food item
     * @param foodName updated food name
     * @param category updated food category
     * @param quantity updated quantity
     * @param unit updated measurement unit
     * @param expiryDate updated expiry date
     * @return true if updated successfully, otherwise false
     */
    public boolean updateFood(String foodId, String foodName,
                              String category, double quantity,
                              String unit, LocalDate expiryDate) {

        Food food = findFoodById(foodId);

        if (food == null ||
                isBlank(foodName) ||
                isBlank(category) ||
                isBlank(unit) ||
                !Double.isFinite(quantity) ||
                quantity < 0) {
            return false;
        }

        if (food instanceof PerishableFood &&
                expiryDate == null) {
            return false;
        }

        food.setFoodName(foodName.trim());
        food.setCategory(category.trim());
        food.setQuantity(quantity);
        food.setUnit(unit.trim());

        if (food instanceof PerishableFood) {
            PerishableFood perishable = (PerishableFood) food;
            perishable.setExpiryDate(expiryDate);
        }

        return true;
    }

    /**
     * Reduces food quantity when sufficient stock is available.
     *
     * @param foodId ID of the food item
     * @param amount quantity to reduce
     * @return true if quantity was reduced, otherwise false
     */
    public boolean reduceQuantity(String foodId, double amount) {

        Food food = findFoodById(foodId);

        if (food == null ||
                !Double.isFinite(amount) ||
                amount <= 0 ||
                amount > food.getQuantity()) {
            return false;
        }

        food.setQuantity(food.getQuantity() - amount);
        return true;
    }

    /**
     * Returns the total number of food items.
     *
     * @return number of food items
     */
    public int getFoodCount() {
        return foodList.size();
    }

    /**
     * Checks whether a string is null, empty, or contains only spaces.
     *
     * @param value string to check
     * @return true if the string is blank, otherwise false
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
