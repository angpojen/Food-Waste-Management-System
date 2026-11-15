
package service;

import model.Food;
import java.util.ArrayList;

public class FoodManager {

    private ArrayList<Food> foodList;

    public FoodManager() {
        foodList = new ArrayList<>();
    }

    public boolean addFood(Food food) {
        if (findFoodById(food.getFoodId()) != null) {
            return false;
        }

        foodList.add(food);
        return true;
    }

    public ArrayList<Food> getAllFoods() {
        return new ArrayList<>(foodList);
    }

    public Food findFoodById(String foodId) {
        for (Food food : foodList) {
            if (food.getFoodId().equalsIgnoreCase(foodId)) {
                return food;
            }
        }
        return null;
    }

    public boolean deleteFood(String foodId) {
        Food food = findFoodById(foodId);

        if (food != null) {
            foodList.remove(food);
            return true;
        }

        return false;
    }

    public boolean updateFood(String foodId, String foodName,
                              String category, double quantity,
                              String unit) {

        Food food = findFoodById(foodId);

        if (food == null || quantity < 0) {
            return false;
        }

        food.setFoodName(foodName);
        food.setCategory(category);
        food.setQuantity(quantity);
        food.setUnit(unit);

        return true;
    }

    public boolean reduceQuantity(String foodId, double amount) {
        Food food = findFoodById(foodId);

        if (food == null || amount <= 0 ||
                amount > food.getQuantity()) {
            return false;
        }

        food.setQuantity(food.getQuantity() - amount);
        return true;
    }

    public int getFoodCount() {
        return foodList.size();
    }
}
