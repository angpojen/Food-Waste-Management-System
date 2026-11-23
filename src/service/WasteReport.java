
package service;

import model.WasteRecord;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages waste records and generates food waste reports.
 */
public class WasteReport {

    /**
     * Stores all food waste records.
     */
    private ArrayList<WasteRecord> wasteRecords;

    /**
     * Initializes an empty waste record list.
     */
    public WasteReport() {
        wasteRecords = new ArrayList<>();
    }

    /**
     * Adds a valid waste record.
     *
     * @param record waste record to be added
     */
    public void addWasteRecord(WasteRecord record) {

        if (record == null ||
                record.getUnit() == null ||
                record.getUnit().trim().isEmpty() ||
                !Double.isFinite(record.getWastedQuantity()) ||
                record.getWastedQuantity() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid waste record."
            );
        }

        wasteRecords.add(record);
    }

    /**
     * Returns a copy of all waste records.
     *
     * @return list of waste records
     */
    public ArrayList<WasteRecord> getAllWasteRecords() {
        return new ArrayList<>(wasteRecords);
    }

    /**
     * Calculates total wasted quantity separately by unit.
     *
     * @return map containing total quantities grouped by unit
     */
    public Map<String, Double> getTotalsByUnit() {

        Map<String, Double> totals = new LinkedHashMap<>();

        for (WasteRecord record : wasteRecords) {

            String unit = record.getUnit().trim();

            if (unit.equalsIgnoreCase("l")) {
                unit = "L";
            }

            double quantity = record.getWastedQuantity();

            totals.put(
                    unit,
                    totals.getOrDefault(unit, 0.0) + quantity
            );
        }

        return totals;
    }

    /**
     * Returns the total number of waste records.
     *
     * @return total number of records
     */
    public int getWasteRecordCount() {
        return wasteRecords.size();
    }

    /**
     * Generates a waste report with quantities grouped by unit.
     *
     * @return formatted waste report
     */
    public String generateReport() {

        StringBuilder report = new StringBuilder();

        report.append("===== FOOD WASTE REPORT =====\n");

        report.append("Total Records: ")
                .append(getWasteRecordCount())
                .append("\n\n");

        report.append("Total Wasted Quantity by Unit:\n");

        Map<String, Double> totals = getTotalsByUnit();

        if (totals.isEmpty()) {
            report.append("No waste records available.\n");
        } else {

            for (Map.Entry<String, Double> entry :
                    totals.entrySet()) {

                report.append(entry.getValue())
                        .append(" ")
                        .append(entry.getKey())
                        .append("\n");
            }
        }

        report.append("=============================");

        return report.toString();
    }
}
