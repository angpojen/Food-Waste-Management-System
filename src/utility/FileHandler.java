
package utility;

import model.Food;
import model.PerishableFood;
import model.NonPerishableFood;
import model.WasteRecord;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading food inventory and waste records
 * using CSV files.
 */
public class FileHandler {

    /**
     * Path of the food inventory CSV file.
     */
    private static final String FOOD_FILE =
            "data/food_inventory.csv";

    /**
     * Path of the waste records CSV file.
     */
    private static final String WASTE_FILE =
            "data/waste_records.csv";

    /**
     * Creates the data directory if it does not exist.
     */
    public FileHandler() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            System.out.println("Unable to create data folder.");
        }
    }

    /**
     * Escapes a value according to CSV formatting rules.
     */
    private String escapeCsv(String value) {
        if (value == null) {
            value = "";
        }

        if (value.contains(",") ||
                value.contains("\"") ||
                value.contains("\n") ||
                value.contains("\r")) {

            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    /**
     * Parses one CSV row, including quoted commas and quotes.
     */
    private String[] parseCsvLine(String line) {

        List<String> values = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char current = line.charAt(i);

            if (current == '"') {

                if (insideQuotes &&
                        i + 1 < line.length() &&
                        line.charAt(i + 1) == '"') {

                    field.append('"');
                    i++;

                } else {
                    insideQuotes = !insideQuotes;
                }

            } else if (current == ',' && !insideQuotes) {

                values.add(field.toString());
                field.setLength(0);

            } else {
                field.append(current);
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException(
                    "Invalid CSV quotation."
            );
        }

        values.add(field.toString());

        return values.toArray(new String[0]);
    }

    /**
     * Reads an old pipe-separated row for data compatibility.
     */
    private String[] parseLegacyLine(String line) {
        return line.split("\\|", -1);
    }

    /**
     * Saves all food inventory records in CSV format.
     */
    public void saveFoods(List<Food> foods) throws IOException {

        Files.createDirectories(Paths.get("data"));

        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(FOOD_FILE))) {

            writer.write(
                    "Type,ID,Name,Category,Quantity,Unit,ExpiryDate"
            );
            writer.newLine();

            for (Food food : foods) {

                String expiry = "";

                if (food instanceof PerishableFood) {

                    PerishableFood p = (PerishableFood) food;

                    if (p.getExpiryDate() != null) {
                        expiry = p.getExpiryDate().toString();
                    }
                }

                String[] values = {
                        food.getFoodType(),
                        food.getFoodId(),
                        food.getFoodName(),
                        food.getCategory(),
                        String.valueOf(food.getQuantity()),
                        food.getUnit(),
                        expiry
                };

                writeCsvRow(writer, values);
            }
        }
    }

    /**
     * Writes one properly escaped CSV row.
     */
    private void writeCsvRow(
            BufferedWriter writer, String[] values
    ) throws IOException {

        for (int i = 0; i < values.length; i++) {

            if (i > 0) {
                writer.write(",");
            }

            writer.write(escapeCsv(values[i]));
        }

        writer.newLine();
    }

    /**
     * Loads food inventory records.
     * Invalid rows are skipped without stopping the entire loading process.
     */
    public ArrayList<Food> loadFoods() throws IOException {

        ArrayList<Food> foods = new ArrayList<>();
        Path path = Paths.get(FOOD_FILE);

        if (!Files.exists(path)) {
            return foods;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String header = reader.readLine();

            if (header == null) {
                return foods;
            }

            boolean legacy = header.contains("|");

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {

                    String[] data = legacy
                            ? parseLegacyLine(line)
                            : parseCsvLine(line);

                    if (data.length != 7) {
                        continue;
                    }

                    String type = data[0];
                    String id = data[1];
                    String name = data[2];
                    String category = data[3];
                    double quantity = Double.parseDouble(data[4]);
                    String unit = data[5];

                    if (id.trim().isEmpty() ||
                            name.trim().isEmpty() ||
                            category.trim().isEmpty() ||
                            unit.trim().isEmpty() ||
                            !Double.isFinite(quantity) ||
                            quantity < 0) {
                        continue;
                    }

                    if (type.equals("Perishable")) {

                        LocalDate expiry = data[6].isEmpty()
                                ? null
                                : LocalDate.parse(data[6]);

                        foods.add(new PerishableFood(
                                id, name, category,
                                quantity, unit, expiry
                        ));

                    } else if (type.equals("Non-Perishable")) {

                        foods.add(new NonPerishableFood(
                                id, name, category,
                                quantity, unit
                        ));
                    }

                } catch (RuntimeException ex) {

                    System.out.println(
                            "Skipping invalid food row: " + line
                    );
                }
            }
        }

        return foods;
    }

    /**
     * Saves waste records in CSV format.
     */
    public void saveWasteRecords(
            List<WasteRecord> records) throws IOException {

        Files.createDirectories(Paths.get("data"));

        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(WASTE_FILE))) {

            writer.write(
                    "WasteID,FoodID,FoodName,Quantity,Unit,Date"
            );
            writer.newLine();

            for (WasteRecord record : records) {

                String[] values = {
                        record.getWasteId(),
                        record.getFoodId(),
                        record.getFoodName(),
                        String.valueOf(record.getWastedQuantity()),
                        record.getUnit(),
                        record.getWasteDate().toString()
                };

                writeCsvRow(writer, values);
            }
        }
    }

    /**
     * Loads waste records while skipping invalid rows.
     */
    public ArrayList<WasteRecord> loadWasteRecords()
            throws IOException {

        ArrayList<WasteRecord> records = new ArrayList<>();
        Path path = Paths.get(WASTE_FILE);

        if (!Files.exists(path)) {
            return records;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String header = reader.readLine();

            if (header == null) {
                return records;
            }

            boolean legacy = header.contains("|");

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {

                    String[] data = legacy
                            ? parseLegacyLine(line)
                            : parseCsvLine(line);

                    if (data.length != 6) {
                        continue;
                    }

                    double quantity =
                            Double.parseDouble(data[3]);

                    if (data[0].trim().isEmpty() ||
                            data[1].trim().isEmpty() ||
                            data[2].trim().isEmpty() ||
                            data[4].trim().isEmpty() ||
                            !Double.isFinite(quantity) ||
                            quantity <= 0) {
                        continue;
                    }

                    records.add(new WasteRecord(
                            data[0],
                            data[1],
                            data[2],
                            quantity,
                            data[4],
                            LocalDate.parse(data[5])
                    ));

                } catch (RuntimeException ex) {

                    System.out.println(
                            "Skipping invalid waste row: " + line
                    );
                }
            }
        }

        return records;
    }
}
