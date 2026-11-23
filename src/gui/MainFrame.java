
package gui;

import model.Food;
import model.PerishableFood;
import model.NonPerishableFood;
import model.WasteRecord;

import service.FoodManager;
import service.ExpiryChecker;
import service.WasteReport;

import utility.FileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Main application window for the Food Waste Management System.
 * Provides the graphical interface for managing food inventory and waste records.
 */
public class MainFrame extends JFrame {

    /** Manages food inventory operations. */
    private FoodManager foodManager;
    /** Finds expired and soon-to-expire food items. */
    private ExpiryChecker expiryChecker;
    /** Stores and generates food waste reports. */
    private WasteReport wasteReport;
    /** Loads and saves food and waste data in CSV files. */
    private FileHandler fileHandler;

    /** Displays the current food inventory. */
    private JTable foodTable;
    /** Supplies rows and columns to the food table. */
    private DefaultTableModel tableModel;

    /** Input field for the food ID. */
    private JTextField idField;
    /** Input field for the food name. */
    private JTextField nameField;
    /** Input field for the food category. */
    private JTextField categoryField;
    /** Input field for the food quantity. */
    private JTextField quantityField;
    /** Input field for the quantity unit. */
    private JTextField unitField;
    /** Input field for the expiry date of perishable food. */
    private JTextField expiryField;
    /** Selects whether the food is perishable or non-perishable. */
    private JComboBox<String> typeBox;

    /**
     * Creates the main window, initializes services, loads saved data,
     * and prepares the graphical user interface.
     */
    public MainFrame() {

        foodManager = new FoodManager();
        expiryChecker = new ExpiryChecker(foodManager);
        wasteReport = new WasteReport();
        fileHandler = new FileHandler();

        loadData();

        setTitle("Food Waste Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        refreshTable();
    }

    /**
     * Builds the Swing interface, including input fields, the inventory table,
     * action buttons, and table-row selection handling.
     */
    private void createGUI() {

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "FOOD WASTE MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        idField = new JTextField();
        nameField = new JTextField();
        categoryField = new JTextField();
        quantityField = new JTextField();
        unitField = new JTextField();
        expiryField = new JTextField();

        typeBox = new JComboBox<>(
                new String[]{"Perishable", "Non-Perishable"}
        );

        inputPanel.add(new JLabel("Food ID:"));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Food Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Unit:"));
        inputPanel.add(unitField);

        inputPanel.add(new JLabel("Food Type:"));
        inputPanel.add(typeBox);

        inputPanel.add(new JLabel("Expiry Date (yyyy-mm-dd):"));
        inputPanel.add(expiryField);

        // Table
        String[] columns = {
                "Food ID", "Name", "Category",
                "Quantity", "Unit", "Type", "Expiry Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        foodTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(foodTable);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        JButton addButton = new JButton("Add Food");
        JButton updateButton = new JButton("Update Food");
        JButton deleteButton = new JButton("Delete Food");
        JButton refreshButton = new JButton("Refresh");

        JButton expiryButton = new JButton("Expiry Alert");
        JButton wasteButton = new JButton("Record Waste");
        JButton reportButton = new JButton("Waste Report");
        JButton clearButton = new JButton("Clear Fields");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        buttonPanel.add(expiryButton);
        buttonPanel.add(wasteButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(clearButton);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        scrollPane.setPreferredSize(new Dimension(900, 250));

        // Button actions
        addButton.addActionListener(e -> addFood());
        updateButton.addActionListener(e -> updateFood());
        deleteButton.addActionListener(e -> deleteFood());
        refreshButton.addActionListener(e -> refreshTable());

        expiryButton.addActionListener(e -> showExpiryAlert());
        wasteButton.addActionListener(e -> recordWaste());
        reportButton.addActionListener(e -> showWasteReport());
        clearButton.addActionListener(e -> clearFields());

        // Select table row
        foodTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting() &&
                    foodTable.getSelectedRow() != -1) {

                int row = foodTable.getSelectedRow();

                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                categoryField.setText(tableModel.getValueAt(row, 2).toString());
                quantityField.setText(tableModel.getValueAt(row, 3).toString());
                unitField.setText(tableModel.getValueAt(row, 4).toString());

                typeBox.setSelectedItem(
                        tableModel.getValueAt(row, 5).toString()
                );

                expiryField.setText(
                        tableModel.getValueAt(row, 6).toString()
                );
            }
        });
    }
    /**
     * Validates whether a quantity is a positive finite number.
     */
    /**
     * Checks whether a quantity is a positive finite number.
     *
     * @param quantity quantity value to validate
     * @return true if the value is finite and greater than zero
     */
    private boolean isValidQuantity(double quantity) {
        return Double.isFinite(quantity) && quantity > 0;
    }
    /**
     * Validates the input fields, creates a food object, and adds it to inventory.
     * Saves the updated data and refreshes the table when the operation succeeds.
     */
    private void addFood() {

        try {

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String unit = unitField.getText().trim();

            if (id.isEmpty() || name.isEmpty() ||
                    category.isEmpty() || unit.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "Please fill in all required fields."
                );
                return;
            }

            double quantity = Double.parseDouble(
                    quantityField.getText().trim()
            );

            if (!Double.isFinite(quantity) || quantity < 0) {
                JOptionPane.showMessageDialog(
                        this, "Quantity must be a valid non-negative number."
                );
                return;
            }

            Food food;

            if (typeBox.getSelectedItem().equals("Perishable")) {

                String expiryText = expiryField.getText().trim();

                if (expiryText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this, "Please enter an expiry date."
                    );
                    return;
                }

                LocalDate expiry = LocalDate.parse(expiryText);

                food = new PerishableFood(
                        id, name, category, quantity, unit, expiry
                );

            } else {

                food = new NonPerishableFood(
                        id, name, category, quantity, unit
                );
            }

            if (foodManager.addFood(food)) {
                saveData();
                refreshTable();
                clearFields();

                JOptionPane.showMessageDialog(
                        this, "Food added successfully!"
                );

            } else {
                JOptionPane.showMessageDialog(
                        this, "Food ID already exists."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid quantity."
            );
        } catch (java.time.format.DateTimeParseException ex) {

            JOptionPane.showMessageDialog(
                    this, "Invalid expiry date. Use yyyy-mm-dd."
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this, ex.getMessage()
            );
        }
    }
    
    /**
     * Updates the selected food item, including its expiry date when applicable.
     * Validates required fields and quantity before saving the changes.
     */
    private void updateFood() {

        try {

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String unit = unitField.getText().trim();

            // Validate required fields
            if (id.isEmpty() || name.isEmpty() ||
                    category.isEmpty() || unit.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all required fields."
                );
                return;
            }

            // Validate quantity
            double quantity = Double.parseDouble(
                    quantityField.getText().trim()
            );

            if (!Double.isFinite(quantity) || quantity < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity must be a valid non-negative number."
                );
                return;
            }

            // Check whether the food exists
            Food existingFood = foodManager.findFoodById(id);

            if (existingFood == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Food ID not found."
                );
                return;
            }

            // Read expiry date for perishable food
            LocalDate expiryDate = null;

            if (existingFood instanceof PerishableFood) {

                String expiryText = expiryField.getText().trim();

                if (expiryText.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter an expiry date."
                    );
                    return;
                }

                expiryDate = LocalDate.parse(expiryText);
            }

            // Update food information and expiry date
            boolean success = foodManager.updateFood(
                    id,
                    name,
                    category,
                    quantity,
                    unit,
                    expiryDate
            );

            if (success) {

                saveData();
                refreshTable();
                clearFields();

                JOptionPane.showMessageDialog(
                        this,
                        "Food updated successfully!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to update food."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid quantity."
            );

        } catch (java.time.format.DateTimeParseException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid expiry date. Use yyyy-mm-dd."
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );
        }
    }

    /**
     * Deletes a food item after user confirmation.
     */
    /**
     * Deletes the selected food item after asking the user for confirmation.
     */
    private void deleteFood() {

        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please select a food item first."
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this food item?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (foodManager.deleteFood(id)) {

            saveData();
            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(
                    this, "Food deleted successfully!"
            );

        } else {

            JOptionPane.showMessageDialog(
                    this, "Food ID not found."
            );
        }
    }

    /**
     * Records a quantity of food as waste, reduces the inventory quantity,
     * and saves the updated inventory and waste records.
     */
    private void recordWaste() {

        try {

            String id = idField.getText().trim();

            Food food = foodManager.findFoodById(id);

            if (food == null) {
                JOptionPane.showMessageDialog(
                        this, "Please select a valid food item."
                );
                return;
            }

            String input = JOptionPane.showInputDialog(
                    this, "Enter wasted quantity:"
            );

            if (input == null) {
                return;
            }

            double amount = Double.parseDouble(input);
            if (!isValidQuantity(amount)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Waste quantity must be a positive number."
                );
                return;
            }
            if (!foodManager.reduceQuantity(id, amount)) {

                JOptionPane.showMessageDialog(
                        this, "Invalid quantity or insufficient stock."
                );
                return;
            }

            WasteRecord record = new WasteRecord(
                    "W" + System.currentTimeMillis(),
                    food.getFoodId(),
                    food.getFoodName(),
                    amount,
                    food.getUnit(),
                    LocalDate.now()
            );

            wasteReport.addWasteRecord(record);

            saveData();
            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(
                    this, "Waste recorded successfully!"
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid quantity."
            );
        }
    }


    /**
     * Displays expired and soon-to-expire food alerts.
     */
    /**
     * Displays expired, expiring-today, and soon-to-expire food items.
     */
    private void showExpiryAlert() {

        ArrayList<PerishableFood> foods =
                expiryChecker.getExpiringFoods();

        StringBuilder message = new StringBuilder();

        if (foods.isEmpty()) {

            message.append("No expired or expiring food.");

        } else {

            message.append("FOOD EXPIRY ALERT\n\n");

            LocalDate today = LocalDate.now();

            for (PerishableFood food : foods) {

                LocalDate expiry = food.getExpiryDate();

                String status;

                if (expiry.isBefore(today)) {
                    status = "EXPIRED";
                } else if (expiry.isEqual(today)) {
                    status = "EXPIRES TODAY";
                } else {
                    status = "EXPIRING SOON";
                }

                message.append(food.getFoodName())
                        .append(" - ")
                        .append(expiry)
                        .append(" - ")
                        .append(status)
                        .append("\n");
            }
        }

        JOptionPane.showMessageDialog(
                this,
                message.toString(),
                "Expiry Alert",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    /**
     * Displays the waste report generated from the stored waste records.
     */
    private void showWasteReport() {

        JOptionPane.showMessageDialog(
                this,
                wasteReport.generateReport(),
                "Waste Report",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Clears and repopulates the inventory table using the latest food data.
     */
    private void refreshTable() {

        tableModel.setRowCount(0);

        for (Food food : foodManager.getAllFoods()) {

            String expiry = "N/A";

            if (food instanceof PerishableFood) {

                PerishableFood p = (PerishableFood) food;

                if (p.getExpiryDate() != null) {
                    expiry = p.getExpiryDate().toString();
                }
            }

            tableModel.addRow(new Object[]{
                    food.getFoodId(),
                    food.getFoodName(),
                    food.getCategory(),
                    food.getQuantity(),
                    food.getUnit(),
                    food.getFoodType(),
                    expiry
            });
        }
    }

    /**
     * Clears all input fields, resets the food type selection,
     * and removes the current table selection.
     */
    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        unitField.setText("");
        expiryField.setText("");

        typeBox.setSelectedIndex(0);
        foodTable.clearSelection();
    }

    /**
     * Saves all food inventory items and waste records to their CSV files.
     * Shows an error message if the files cannot be written.
     */
    private void saveData() {

        try {

            fileHandler.saveFoods(foodManager.getAllFoods());

            fileHandler.saveWasteRecords(
                    wasteReport.getAllWasteRecords()
            );

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    this, "Unable to save data: " + ex.getMessage()
            );
        }
    }

    /**
     * Loads food inventory items and waste records from their CSV files
     * when the application starts.
     */
    private void loadData() {

        try {

            for (Food food : fileHandler.loadFoods()) {
                foodManager.addFood(food);
            }

            for (WasteRecord record : fileHandler.loadWasteRecords()) {
                wasteReport.addWasteRecord(record);
            }

        } catch (IOException | RuntimeException ex) {

            JOptionPane.showMessageDialog(
                    null, "Unable to load data: " + ex.getMessage()
            );
        }
    }
}
