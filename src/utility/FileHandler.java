
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

public class FileHandler {

    private static final String FOOD_FILE =
            "data/food_inventory.csv";

    private static final String WASTE_FILE =
            "data/waste_records.csv";

    public FileHandler() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            System.out.println("Unable to create data folder.");
        }
    }

    // Save food inventory
    public void saveFoods(List<Food> foods) throws IOException {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(FOOD_FILE))) {

            writer.write("Type|ID|Name|Category|Quantity|Unit|ExpiryDate");
            writer.newLine();

            for (Food food : foods) {

                String expiry = "";

                if (food instanceof PerishableFood) {
                    PerishableFood p = (PerishableFood) food;

                    if (p.getExpiryDate() != null) {
                        expiry = p.getExpiryDate().toString();
                    }
                }

                writer.write(
                        food.getFoodType() + "|" +
                                food.getFoodId() + "|" +
                                food.getFoodName() + "|" +
                                food.getCategory() + "|" +
                                food.getQuantity() + "|" +
                                food.getUnit() + "|" +
                                expiry
                );

                writer.newLine();
            }
        }
    }

    // Load food inventory
    public ArrayList<Food> loadFoods() throws IOException {

        ArrayList<Food> foods = new ArrayList<>();

        Path path = Paths.get(FOOD_FILE);

        if (!Files.exists(path)) {
            return foods;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length != 7) {
                    continue;
                }

                String type = data[0];
                String id = data[1];
                String name = data[2];
                String category = data[3];
                double quantity = Double.parseDouble(data[4]);
                String unit = data[5];

                if (type.equals("Perishable")) {

                    LocalDate expiry = data[6].isEmpty()
                            ? null : LocalDate.parse(data[6]);

                    foods.add(new PerishableFood(
                            id, name, category, quantity, unit, expiry
                    ));

                } else if (type.equals("Non-Perishable")) {

                    foods.add(new NonPerishableFood(
                            id, name, category, quantity, unit
                    ));
                }
            }
        }

        return foods;
    }

    // Save waste records
    public void saveWasteRecords(
            List<WasteRecord> records) throws IOException {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(WASTE_FILE))) {

            writer.write("WasteID|FoodID|FoodName|Quantity|Unit|Date");
            writer.newLine();

            for (WasteRecord record : records) {

                writer.write(
                        record.getWasteId() + "|" +
                                record.getFoodId() + "|" +
                                record.getFoodName() + "|" +
                                record.getWastedQuantity() + "|" +
                                record.getUnit() + "|" +
                                record.getWasteDate()
                );

                writer.newLine();
            }
        }
    }

    // Load waste records
    public ArrayList<WasteRecord> loadWasteRecords()
            throws IOException {

        ArrayList<WasteRecord> records = new ArrayList<>();

        Path path = Paths.get(WASTE_FILE);

        if (!Files.exists(path)) {
            return records;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data.length != 6) {
                    continue;
                }

                records.add(new WasteRecord(
                        data[0],
                        data[1],
                        data[2],
                        Double.parseDouble(data[3]),
                        data[4],
                        LocalDate.parse(data[5])
                ));
            }
        }

        return records;
    }
}

