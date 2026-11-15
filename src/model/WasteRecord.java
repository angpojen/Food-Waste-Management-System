
package model;

import java.time.LocalDate;

public class WasteRecord {

    private String wasteId;
    private String foodId;
    private String foodName;
    private double wastedQuantity;
    private String unit;
    private LocalDate wasteDate;

    public WasteRecord(String wasteId, String foodId,
                       String foodName, double wastedQuantity,
                       String unit, LocalDate wasteDate) {

        this.wasteId = wasteId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.wastedQuantity = wastedQuantity;
        this.unit = unit;
        this.wasteDate = wasteDate;
    }

    public String getWasteId() {
        return wasteId;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getWastedQuantity() {
        return wastedQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDate getWasteDate() {
        return wasteDate;
    }
}
