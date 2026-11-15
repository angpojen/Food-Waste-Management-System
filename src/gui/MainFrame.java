
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

public class MainFrame extends JFrame {

    private FoodManager foodManager;
    private ExpiryChecker expiryChecker;
    private WasteReport wasteReport;
    private FileHandler fileHandler;

    private JTable foodTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField nameField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JTextField unitField;
    private JTextField expiryField;

    private JComboBox<String> typeBox;

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

    private void addFood() {

        try {

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            double quantity = Double.parseDouble(
                    quantityField.getText().trim()
            );
            String unit = unitField.getText().trim();

            if (id.isEmpty() || name.isEmpty() ||
                    category.isEmpty() || unit.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "Please fill in all required fields."
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

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this, ex.getMessage()
            );
        }
    }

    private void updateFood() {

        try {

            String id = idField.getText().trim();

            double quantity = Double.parseDouble(
                    quantityField.getText().trim()
            );

            boolean success = foodManager.updateFood(
                    id,
                    nameField.getText().trim(),
                    categoryField.getText().trim(),
                    quantity,
                    unitField.getText().trim()
            );

            if (success) {
                saveData();
                refreshTable();
                clearFields();

                JOptionPane.showMessageDialog(
                        this, "Food updated successfully!"
                );

            } else {
                JOptionPane.showMessageDialog(
                        this, "Food not found or invalid quantity."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Invalid quantity."
            );
        }
    }

    private void deleteFood() {

        String id = idField.getText().trim();

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

    private void showExpiryAlert() {

        ArrayList<PerishableFood> foods =
                expiryChecker.getExpiringFoods();

        StringBuilder message = new StringBuilder();

        if (foods.isEmpty()) {

            message.append("No food expiring within 3 days.");

        } else {

            message.append("FOOD EXPIRY ALERT\n\n");

            for (PerishableFood food : foods) {

                message.append(food.getFoodName())
                        .append(" - ")
                        .append(food.getExpiryDate())
                        .append("\n");
            }
        }

        JOptionPane.showMessageDialog(
                this, message.toString(), "Expiry Alert",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showWasteReport() {

        JOptionPane.showMessageDialog(
                this,
                wasteReport.generateReport(),
                "Waste Report",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

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
