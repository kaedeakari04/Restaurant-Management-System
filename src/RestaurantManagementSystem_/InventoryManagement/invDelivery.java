package RestaurantManagementSystem_.InventoryManagement;

import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class invDelivery extends JPanel implements ActionListener {

    public enum Role { STAFF, ADMIN, SUPER_ADMIN }

    private DefaultTableModel model;
    private JTable deliveryTable;
    private JPanel panelFunctionMenu, panelDeliveryTable, panelDate, panelTime, panelAdd, panelModify;
    private JLabel lblDate, lblTime, lblAddItemID, lblAddName, lblAddQuantity, lblAddCategory, lblAddMeasurement,
                   lblAddDeliveryID, lblAddExpirationDate, lblAddDeliveryDate, lblAddDeliveryDate1, lblAddDeliveryTime, lblAddDeliveryTime1,
                   lblAddDeliveryCourier;
    private JComboBox cbAddCategory, cbAddMeasurement;
    private JButton btnAdd, btnModify, btnRemove;
    private JTextField txtAddName, txtAddQuantity, txtAddExpirationDate, txtAddDeliveryDate, txtAddDeliveryTime, txtAddDeliveryCourier;
    private List<invItem> deliveryList;
    private Role role;

    public invDelivery()
    {
        this(Role.STAFF);
    }

    public invDelivery(Role role)
    {
        this.role = role;
        this.deliveryList = InventoryManager.getInstance().getDeliveryList();
        setBounds(300, 80, 980, 720);
        setLayout(null);
        setBackground(Color.decode("#FFF8E1"));
        functionMenu();
        loadTableFromList();
    }

    private void loadTableFromList()
    {
        model.setRowCount(0);
        for (invItem item : deliveryList)
        {
            model.addRow(new Object[]{
                item.getDeliveryID(),
                item.getItemID(),
                item.getItemName(),
                item.getItemQuantity(),
                item.getItemCategory(),
                item.getItemMeasurement(),
                item.getItemExpirationDate(),
                item.getItemDeliveryDate(),
                item.getItemDeliveryTime(),
                item.getItemDeliveryCourier()
            });
        }
    }

    private void functionMenu()
    {
        panelFunctionMenu = new JPanel();
        panelFunctionMenu.setBounds(0, 0, 980, 720);
        panelFunctionMenu.setBackground(Color.decode("#FFF8E1"));
        panelFunctionMenu.setLayout(null);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");

        panelDate = new JPanel();
        panelDate.setBounds(25, 25, 400, 50);
        panelDate.setBackground(Color.decode("#1b4a62"));

        lblDate = new JLabel("Date: " + now.format(dateFormat));
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(new Font("Arial", Font.BOLD, 35));
        panelDate.add(lblDate);

        panelTime = new JPanel();
        panelTime.setBounds(521, 25, 400, 50);
        panelTime.setBackground(Color.decode("#1b4a62"));

        lblTime = new JLabel("Time: " + now.format(timeFormat));
        lblTime.setForeground(Color.WHITE);
        lblTime.setFont(new Font("Arial", Font.BOLD, 35));
        panelTime.add(lblTime);

        panelDeliveryTable = new JPanel(new BorderLayout());
        panelDeliveryTable.setBounds(25, 100, 900, 400);
        panelDeliveryTable.setBackground(Color.decode("#f5cfba"));

        model = new DefaultTableModel();
        model.addColumn("STOCKS ID");
        model.addColumn("ITEM ID");
        model.addColumn("ITEM NAME");
        model.addColumn("QUANTITY");
        model.addColumn("CATEGORY");
        model.addColumn("MEASUREMENT");
        model.addColumn("EXPIRATION DATE");
        model.addColumn("DATE");
        model.addColumn("TIME");
        model.addColumn("COURIER");

        deliveryTable = new JTable(model);
        deliveryTable.setDefaultEditor(Object.class, null);
        deliveryTable.getTableHeader().setBackground(Color.decode("#1b4a62"));
        deliveryTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(deliveryTable);
        panelDeliveryTable.add(scrollPane, BorderLayout.CENTER);

        btnAdd = new JButton("ADD");
        btnAdd.setBounds(315, 550, 150, 30);
        btnModify = new JButton("MODIFY");
        btnModify.setBounds(515, 550, 150, 30);

        stylebtnFunction(btnAdd);
        stylebtnFunction(btnModify);

        btnModify.setVisible(role == Role.ADMIN || role == Role.SUPER_ADMIN);

        btnAdd.addActionListener(this);
        btnModify.addActionListener(this);

        panelFunctionMenu.add(panelDate);
        panelFunctionMenu.add(panelTime);
        panelFunctionMenu.add(panelDeliveryTable);
        panelFunctionMenu.add(btnAdd);
        panelFunctionMenu.add(btnModify);

        add(panelFunctionMenu);
    }

    private void stylebtnFunction(JButton btn)
    {
        btn.setBackground(Color.decode("#e7191f"));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(Color.decode("#b71c1c")); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(Color.decode("#e7191f")); }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnAdd)
        {
            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss a");

            String autoStocksID = InventoryManager.generateStocksID();
            String autoItemID   = InventoryManager.generateItemID();
            String autoDate     = now.format(dateFormat);
            String autoTime     = now.format(timeFormat);

            panelAdd = new JPanel(new GridLayout(10, 2, 5, 5));

            lblAddDeliveryID = new JLabel("Stocks ID :");
            JLabel lblAutoStocksID = new JLabel(autoStocksID);
            lblAutoStocksID.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));

            lblAddItemID = new JLabel("Item ID :");
            JLabel lblAutoItemID = new JLabel(autoItemID);
            lblAutoItemID.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));

            lblAddName = new JLabel("*Item Name:");
            txtAddName = new JTextField();

            lblAddQuantity = new JLabel("*Quantity:");
            txtAddQuantity = new JTextField();

            lblAddCategory = new JLabel("*Category:");
            String[] categories = {"MEAT", "SEASONING", "VEGETABLE", "FRUIT", "CONDIMENTS", "OTHERS"};
            cbAddCategory = new JComboBox<>(categories);

            lblAddMeasurement = new JLabel("*Measurement:");
            String[] measurements = {"KG", "LITER", "PACK", "PCS"};
            cbAddMeasurement = new JComboBox<>(measurements);

            lblAddExpirationDate = new JLabel("Expiration Date (01/01/2026):");
            txtAddExpirationDate = new JTextField();

            lblAddDeliveryDate  = new JLabel("Delivery Date: ");
            lblAddDeliveryDate1 = new JLabel(autoDate);

            lblAddDeliveryTime  = new JLabel("Delivery Time: ");
            lblAddDeliveryTime1 = new JLabel(autoTime);

            lblAddDeliveryCourier = new JLabel("*   Courier:");
            txtAddDeliveryCourier = new JTextField();

            panelAdd.add(lblAddDeliveryID);
            panelAdd.add(lblAutoStocksID);
            panelAdd.add(lblAddItemID);
            panelAdd.add(lblAutoItemID);
            panelAdd.add(lblAddName);
            panelAdd.add(txtAddName);
            panelAdd.add(lblAddQuantity);
            panelAdd.add(txtAddQuantity);
            panelAdd.add(lblAddCategory);
            panelAdd.add(cbAddCategory);
            panelAdd.add(lblAddMeasurement);
            panelAdd.add(cbAddMeasurement);
            panelAdd.add(lblAddExpirationDate);
            panelAdd.add(txtAddExpirationDate);
            panelAdd.add(lblAddDeliveryDate);
            panelAdd.add(lblAddDeliveryTime);
            panelAdd.add(lblAddDeliveryDate1);
            panelAdd.add(lblAddDeliveryTime1);
            panelAdd.add(lblAddDeliveryCourier);
            panelAdd.add(txtAddDeliveryCourier);

            int userConfirm = JOptionPane.showConfirmDialog(this, panelAdd, "Add Delivery", JOptionPane.OK_CANCEL_OPTION);

            if (userConfirm == JOptionPane.OK_OPTION)
            {
                String inputItemName        = txtAddName.getText().trim();
                String inputItemQuantity    = txtAddQuantity.getText().trim();
                String inputItemCategory    = cbAddCategory.getSelectedItem().toString();
                String inputItemMeasurement = cbAddMeasurement.getSelectedItem().toString();
                String inputItemExpiration  = txtAddExpirationDate.getText().trim();
                String inputDeliveryCourier = txtAddDeliveryCourier.getText().trim();

                if (!inputItemName.isEmpty() && !inputItemQuantity.isEmpty()
                        && !inputItemExpiration.isEmpty()
                        && !inputDeliveryCourier.isEmpty())
                {
                    try
                    {
                        double inputItemQuantityDouble = Double.parseDouble(inputItemQuantity);

                        if (inputItemQuantityDouble <= 0)
                        {
                            JOptionPane.showMessageDialog(this, "Quantity must be over 0", "Add Delivery | Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (!InventoryManager.getInstance().itemExistsInInventory(inputItemName))
                        {
                            JOptionPane.showMessageDialog(this,
                                    "\"" + inputItemName + "\" does not exist in inventory.\n" +
                                    "Only items already recorded in the inventory can receive deliveries.\n" +
                                    "Please check the item name and try again.",
                                    "Add Delivery | Item Not Found",
                                    JOptionPane.ERROR_MESSAGE);
                            InventoryManager.rollbackItemID();
                            return;
                        }

                        invItem item = new invItem(autoItemID, inputItemName, inputItemQuantityDouble, inputItemCategory, inputItemMeasurement, autoStocksID, inputItemExpiration, autoDate, autoTime, inputDeliveryCourier, "");

                        deliveryList.add(item);
                        InventoryManager.getInstance().receiveDelivery(item);

                        model.addRow(new Object[]{
                            autoStocksID,
                            autoItemID,
                            inputItemName,
                            inputItemQuantityDouble,
                            inputItemCategory,
                            inputItemMeasurement,
                            inputItemExpiration,
                            autoDate,
                            autoTime,
                            inputDeliveryCourier
                        });

                        JOptionPane.showMessageDialog(this, "Delivery added successfully", "Add Delivery", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(this, "Quantity must be a valid number", "Add Delivery | Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Please enter all fields", "Add Delivery | Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        else if (e.getSource() == btnModify)
        {
            int selectedRow = deliveryTable.getSelectedRow();

            if (selectedRow == -1)
            {
                JOptionPane.showMessageDialog(this, "Please select a delivery to modify", "Modify | Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedStocksID = (String) model.getValueAt(selectedRow, 0);
            invItem itemToModify = null;

            for (invItem item : deliveryList)
            {
                if (item.getDeliveryID().equalsIgnoreCase(selectedStocksID))
                {
                    itemToModify = item;
                    break;
                }
            }

            if (itemToModify == null)
            {
                JOptionPane.showMessageDialog(this, "Delivery not found", "Modify | Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            panelModify = new JPanel(new GridLayout(10, 2, 5, 5));

            JLabel lblModStocksID = new JLabel(itemToModify.getDeliveryID());
            lblModStocksID.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
            JLabel lblModItemID = new JLabel(itemToModify.getItemID());
            lblModItemID.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));

            txtAddName            = new JTextField(itemToModify.getItemName());
            txtAddQuantity        = new JTextField(String.valueOf(itemToModify.getItemQuantity()));

            String[] categories = {"MEAT", "SEASONING", "VEGETABLE", "FRUIT", "CONDIMENTS", "OTHERS"};
            cbAddCategory = new JComboBox<>(categories);
            cbAddCategory.setSelectedItem(itemToModify.getItemCategory());

            String[] measurements = {"KG", "LITER", "PACK", "PCS"};
            cbAddMeasurement = new JComboBox<>(measurements);
            cbAddMeasurement.setSelectedItem(itemToModify.getItemMeasurement());

            txtAddExpirationDate  = new JTextField(itemToModify.getItemExpirationDate());
            txtAddDeliveryCourier = new JTextField(itemToModify.getItemDeliveryCourier());

            panelModify.add(new JLabel("Stocks ID:"));
            panelModify.add(lblModStocksID);
            panelModify.add(new JLabel("Item ID:"));
            panelModify.add(lblModItemID);
            panelModify.add(new JLabel("Item Name:"));
            panelModify.add(txtAddName);
            panelModify.add(new JLabel("Category:"));
            panelModify.add(cbAddCategory);
            panelModify.add(new JLabel("Measurement:"));
            panelModify.add(cbAddMeasurement);
            panelModify.add(new JLabel("Expiration Date:"));
            panelModify.add(txtAddExpirationDate);
            panelModify.add(new JLabel("Courier:"));
            panelModify.add(txtAddDeliveryCourier);

            int result = JOptionPane.showConfirmDialog(this, panelModify, "Modify Delivery", JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) return;

            String newItemName        = txtAddName.getText().trim();
            String newItemCategory    = cbAddCategory.getSelectedItem().toString();
            String newItemMeasurement = cbAddMeasurement.getSelectedItem().toString();
            String newItemExpiration  = txtAddExpirationDate.getText().trim();
            String newDeliveryCourier = txtAddDeliveryCourier.getText().trim();

            String keptStocksID = itemToModify.getDeliveryID();
            String keptItemID   = itemToModify.getItemID();

            itemToModify.setItemName(newItemName);
            itemToModify.setItemCategory(newItemCategory);
            itemToModify.setItemMeasurement(newItemMeasurement);
            itemToModify.setExpirationDate(newItemExpiration);
            itemToModify.setDeliveryCourier(newDeliveryCourier);

            model.setValueAt(keptStocksID,       selectedRow, 0);
            model.setValueAt(keptItemID,          selectedRow, 1);
            model.setValueAt(newItemName,         selectedRow, 2);
            model.setValueAt(newItemCategory,     selectedRow, 4);
            model.setValueAt(newItemMeasurement,  selectedRow, 5);
            model.setValueAt(newItemExpiration,   selectedRow, 6);
            model.setValueAt(newDeliveryCourier,  selectedRow, 9);

            JOptionPane.showMessageDialog(this, "Delivery modified successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}