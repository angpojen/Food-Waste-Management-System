
package model;

import java.time.LocalDate;

/**
 * Represents a record of wasted food in the system.
 */
public class WasteRecord {

    private String wasteId;
    private String foodId;
    private String foodName;
    private double wastedQuantity;
    private String unit;
    private LocalDate wasteDate;

    /**
     * Creates a waste record with validated information.
     *
     * @param wasteId unique waste record ID
     * @param foodId ID of the food item
     * @param foodName name of the food item
     * @param wastedQuantity quantity of wasted food
     * @param unit measurement unit
     * @param wasteDate date when the food was wasted
     */
    public WasteRecord(String wasteId, String foodId,
                       String foodName, double wastedQuantity,
                       String unit, LocalDate wasteDate) {

        if (wasteId == null || wasteId.trim().isEmpty()) {
            throw new IllegalArgumentException("Waste ID cannot be empty.");
        }

        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty.");
        }

        if (foodName == null || foodName.trim().isEmpty()) {
            throw new IllegalArgumentException("Food name cannot be empty.");
        }

        if (!Double.isFinite(wastedQuantity) || wastedQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Wasted quantity must be a valid positive number."
            );
        }

        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be empty.");
        }

        if (wasteDate == null) {
            throw new IllegalArgumentException("Waste date cannot be empty.");
        }

        this.wasteId = wasteId.trim();
        this.foodId = foodId.trim();
        this.foodName = foodName.trim();
        this.wastedQuantity = wastedQuantity;
        this.unit = unit.trim();
        this.wasteDate = wasteDate;
    }

    /** @return waste record ID */
    public String getWasteId() {
        return wasteId;
    }

    /** @return food item ID */
    public String getFoodId() {
        return foodId;
    }

    /** @return food item name */
    public String getFoodName() {
        return foodName;
    }

    /** @return wasted quantity */
    public double getWastedQuantity() {
        return wastedQuantity;
    }

    /** @return measurement unit */
    public String getUnit() {
        return unit;
    }

    /** @return waste record date */
    public LocalDate getWasteDate() {
        return wasteDate;
    }
}
