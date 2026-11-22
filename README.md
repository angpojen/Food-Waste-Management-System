# Food Waste Management System (FWMS)

A Java-based desktop application developed to support food inventory management and reduce food waste. The project is related to **United Nations Sustainable Development Goal 12 (SDG 12): Responsible Consumption and Production**.

## Project Information

- **Project Name:** Food Waste Management System (FWMS)
- **Language:** Java
- **User Interface:** Java Swing
- **Data Storage:** CSV files
- **SDG:** SDG 12 – Responsible Consumption and Production

## SDG 12 Connection

SDG 12 encourages responsible consumption and production. This system supports that goal by helping users keep track of food inventory, monitor expiry dates, and record wasted food. These functions can help users become more aware of food usage and reduce avoidable food waste.

## System Features

- **Food Inventory Management:** Add, update, delete, and view food items.
- **Food Categories and Types:** Store food details such as name, category, quantity, unit, and food type.
- **Expiry Alerts:** Identify expired food and food that is expiring soon.
- **Waste Recording:** Record the quantity of food that has been wasted.
- **Waste Report:** View waste records and see totals separated by measurement unit (for example, kg and L).
- **CSV Data Storage:** Save and load food inventory and waste records using CSV files.

## Project Structure

```text
src/
├── gui/
│   └── MainFrame.java
├── main/
│   └── Main.java
├── model/
│   ├── Food.java
│   ├── NonPerishableFood.java
│   ├── PerishableFood.java
│   └── WasteRecord.java
├── service/
│   ├── ExpiryChecker.java
│   ├── FoodManager.java
│   └── WasteReport.java
└── utility/
    └── FileHandler.java

data/
├── food_inventory.csv
└── waste_records.csv
```

## Requirements

- Java Development Kit (JDK) installed.
- IntelliJ IDEA or another Java IDE.

## How to Run

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Make sure the project is configured to use an installed JDK.
4. Open `src/main/Main.java`.
5. Run the `main()` method.
6. The Food Waste Management System window should open.

The application stores its data in the `data` folder. The CSV files are used to save food inventory and waste records between runs.

## Group Members

- **Member 1:** ANG PO JEN
- **Member 2:** CHOWDHURY MD JOBAIR
- **Member 3:** JIYINGYU
- **Member 4:** ZHANG SHUYUE

## Notes

- Food inventory is stored in `data/food_inventory.csv`.
- Waste records are stored in `data/waste_records.csv`.
- Do not delete the `data` folder if you want to keep the saved application records.