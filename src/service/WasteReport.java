
package service;

import model.WasteRecord;
import java.util.ArrayList;

public class WasteReport {

    private ArrayList<WasteRecord> wasteRecords;

    public WasteReport() {
        wasteRecords = new ArrayList<>();
    }

    public void addWasteRecord(WasteRecord record) {
        wasteRecords.add(record);
    }

    public ArrayList<WasteRecord> getAllWasteRecords() {
        return new ArrayList<>(wasteRecords);
    }

    public double getTotalWastedQuantity() {
        double total = 0;

        for (WasteRecord record : wasteRecords) {
            total += record.getWastedQuantity();
        }

        return total;
    }

    public int getWasteRecordCount() {
        return wasteRecords.size();
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();

        report.append("===== FOOD WASTE REPORT =====\n");
        report.append("Total Records: ")
                .append(getWasteRecordCount()).append("\n");

        report.append("Total Wasted Quantity: ")
                .append(getTotalWastedQuantity()).append("\n");

        report.append("=============================");

        return report.toString();
    }
}
