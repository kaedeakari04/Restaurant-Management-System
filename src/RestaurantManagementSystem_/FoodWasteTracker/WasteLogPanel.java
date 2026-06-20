package RestaurantManagementSystem_.FoodWasteTracker;
import RestaurantManagementSystem_.InventoryManagement.InventoryManager;
import RestaurantManagementSystem_.InventoryManagement.invItem;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class WasteLogPanel extends JPanel implements ActionListener {
    private static final List<WasteLog> SHARED_LOGS = new ArrayList<>();
    public static List<WasteLog> getSharedLogs() { return SHARED_LOGS; }

    public enum Role { STAFF, ADMIN, SUPER_ADMIN }
    public static WasteLogPanel forStaff()      { return new WasteLogPanel(SHARED_LOGS, Role.STAFF); }
    public static WasteLogPanel forAdmin()      { return new WasteLogPanel(SHARED_LOGS, Role.ADMIN); }
    public static WasteLogPanel forSuperAdmin() { return new WasteLogPanel(SHARED_LOGS, Role.SUPER_ADMIN); }

    private List<WasteLog> logs;
    private Role role;
    private boolean editMode = false;

    private DefaultTableModel tableModel;
    private JTable tblWasteLog;
    private JScrollPane scrollPane;

    private JButton btnAddLog, btnEditLogs, btnConfirmEdit;
    private JPanel btnPanel;

    Color colorCream  = new Color(0xFF, 0xF8, 0xE1);
    Color colorTeal   = new Color(0x36, 0x63, 0x79);
    Color colorRed    = new Color(0xB7, 0x1C, 0x1C);
    Color colorSalmon = new Color(0xF5, 0xCF, 0xBA);
    Color colorSteel  = new Color(0x89, 0xB7, 0xB3);
    Color colorDark   = new Color(0x22, 0x3A, 0x45);
    Color colorWhite  = Color.WHITE;
    Color colorRowAlt = new Color(0xFF, 0xF0, 0xD0);

    Font fontBold   = new Font("Arial", Font.BOLD, 14);
    Font fontNormal = new Font("Arial", Font.PLAIN, 13);
    Font fontHeader = new Font("Arial", Font.BOLD, 22);

    public WasteLogPanel(List<WasteLog> logs, Role role)
    {
        this.logs = logs;
        this.role = role;

        wasteLogTable();
        buttons();

        setBounds(300, 80, 980, 720);
        setLayout(null);
        setBackground(colorCream);
    }

    private void wasteLogTable()
    {
        JLabel lblTitle = new JLabel("FOOD WASTE LOGS");
        lblTitle.setBounds(30, 20, 400, 40);
        lblTitle.setFont(fontHeader);
        lblTitle.setForeground(colorDark);
        add(lblTitle);

        String[] columns = {"TIME", "ITEM", "QTY", "REASON", "STAFF", "REMARKS"};
        tableModel = new DefaultTableModel(columns, 0)
        {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblWasteLog = new JTable(tableModel);
        tblWasteLog.setFont(fontNormal);
        tblWasteLog.setRowHeight(36);
        tblWasteLog.setShowGrid(false);
        tblWasteLog.setBackground(colorWhite);
        tblWasteLog.setForeground(colorDark);
        tblWasteLog.setSelectionBackground(colorSteel);
        tblWasteLog.setSelectionForeground(colorWhite);
        tblWasteLog.getTableHeader().setBackground(colorTeal);
        tblWasteLog.getTableHeader().setForeground(colorWhite);
        tblWasteLog.getTableHeader().setFont(fontBold);
        tblWasteLog.getTableHeader().setReorderingAllowed(false);
        tblWasteLog.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblWasteLog.getColumnModel().getColumn(1).setPreferredWidth(130);
        tblWasteLog.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblWasteLog.getColumnModel().getColumn(3).setPreferredWidth(110);
        tblWasteLog.getColumnModel().getColumn(4).setPreferredWidth(70);
        tblWasteLog.getColumnModel().getColumn(5).setPreferredWidth(250);
        scrollPane = new JScrollPane(tblWasteLog);
        scrollPane.setBounds(30, 75, 920, 520);
        scrollPane.getViewport().setBackground(colorCream);
        scrollPane.setBorder(BorderFactory.createLineBorder(colorTeal, 1));
        add(scrollPane);
    }

    private void buttons()
    {
        btnPanel = new JPanel(null);
        btnPanel.setBounds(30, 610, 600, 50);
        btnPanel.setBackground(colorCream);
        add(btnPanel);

        btnAddLog = new JButton("ADD LOG");
        btnAddLog.setBounds(0, 5, 130, 38);
        btnAddLog.setBackground(colorRed);
        btnAddLog.setForeground(colorWhite);
        btnAddLog.setFont(fontBold);
        btnAddLog.setFocusPainted(false);
        btnAddLog.setBorderPainted(false);
        btnAddLog.addActionListener(this);
        btnPanel.add(btnAddLog);

        btnEditLogs = new JButton("EDIT LOGS");
        btnEditLogs.setBounds(145, 5, 130, 38);
        btnEditLogs.setBackground(colorRed);
        btnEditLogs.setForeground(colorWhite);
        btnEditLogs.setFont(fontBold);
        btnEditLogs.setFocusPainted(false);
        btnEditLogs.setBorderPainted(false);
        btnEditLogs.addActionListener(this);
        btnEditLogs.setVisible(role == Role.SUPER_ADMIN && !logs.isEmpty());
        btnPanel.add(btnEditLogs);

        refreshTable();
    }

    private void refreshTable()
    {
        tableModel.setRowCount(0);
        for (WasteLog log : logs)
        {
            tableModel.addRow(new Object[]{
                    log.time, log.item, log.qty,
                    log.reason, log.staff, log.remarks
            });
        }
        if (btnEditLogs != null)
            btnEditLogs.setVisible(role == Role.SUPER_ADMIN && !logs.isEmpty());
    }

    private void enterEditMode()
    {
        editMode = true;
        btnPanel.removeAll();
        btnConfirmEdit = new JButton("CONFIRM EDIT");
        btnConfirmEdit.setBounds(0, 5, 160, 38);
        btnConfirmEdit.setBackground(colorRed);
        btnConfirmEdit.setForeground(colorWhite);
        btnConfirmEdit.setFont(fontBold);
        btnConfirmEdit.setFocusPainted(false);
        btnConfirmEdit.setBorderPainted(false);
        btnConfirmEdit.addActionListener(this);
        btnPanel.add(btnConfirmEdit);
        btnPanel.revalidate();
        btnPanel.repaint();
        tblWasteLog.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (!editMode) return;
                int selectedRow = tblWasteLog.getSelectedRow();
                if (selectedRow == -1) return;
                showDeleteDialog(selectedRow);
            }
        });
    }

    private void exitEditMode()
    {
        editMode = false;
        btnPanel.removeAll();
        btnAddLog.setBounds(0, 5, 130, 38);
        btnPanel.add(btnAddLog);
        btnEditLogs.setBounds(145, 5, 130, 38);
        btnPanel.add(btnEditLogs);
        btnPanel.revalidate();
        btnPanel.repaint();
        refreshTable();
    }

    private void showDeleteDialog(int selectedRow)
    {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to DELETE this log?",
                "DELETE LOG",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION)
        {
            WasteLog removed = logs.get(selectedRow);
            double qty = 0;
            try { qty = Double.parseDouble(removed.qty); } catch (NumberFormatException ignored) {}
            if (qty > 0) InventoryManager.getInstance().addStock(removed.item, qty);
            logs.remove(selectedRow);
            refreshTable();
        }
    }

    private void showAddLogDialog()
    {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        String[] logTypes = {"Ingredient", "Dish"};
        int typeChoice = JOptionPane.showOptionDialog(
                frame,
                "What type of waste are you logging?",
                "ADD WASTE LOG",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                logTypes,
                logTypes[0]
        );
        if (typeChoice == JOptionPane.CLOSED_OPTION) return;

        String timeNow    = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        String currentEmp = WasteLogSession.getInstance().getEmployeeNo();

        RestaurantManagementSystem_.InventoryManagement.InventoryPopulatedData.loadInventoryData();

        if (typeChoice == 1)
        {
            JPanel panelDish = new JPanel(new GridLayout(5, 2, 5, 10));

            JTextField txtTime = new JTextField(timeNow);
            txtTime.setEditable(false);

            String[] dishNames = {"- Select Dish -", "Chicken Adobo", "Chicharon Bulaklak",
                    "Tortang Talong", "Turon", "Iced Tea", "Buko Juice"};
            JComboBox<String> cbDish = new JComboBox<>(dishNames);

            JTextField txtQty = new JTextField();

            JTextField txtStaff = new JTextField(currentEmp);
            txtStaff.setEditable(false);
            txtStaff.setBackground(new Color(0xEE, 0xEE, 0xEE));

            JTextField txtRemarks = new JTextField();

            panelDish.add(new JLabel("TIME:"));       panelDish.add(txtTime);
            panelDish.add(new JLabel("* DISH:"));     panelDish.add(cbDish);
            panelDish.add(new JLabel("* SERVINGS:")); panelDish.add(txtQty);
            panelDish.add(new JLabel("STAFF:"));      panelDish.add(txtStaff);
            panelDish.add(new JLabel("REMARKS:"));    panelDish.add(txtRemarks);

            int confirm = JOptionPane.showConfirmDialog(frame, panelDish, "ADD WASTE LOG — Dish", JOptionPane.OK_CANCEL_OPTION);
            if (confirm != JOptionPane.OK_OPTION) return;

            String selectedDish = (String) cbDish.getSelectedItem();
            String inputQty     = txtQty.getText().trim();

            if (selectedDish.equals("- Select Dish -"))
            {
                JOptionPane.showMessageDialog(frame, "Please select a dish.", "MISSING FIELD", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (inputQty.isEmpty())
            {
                JOptionPane.showMessageDialog(frame, "Please enter number of servings.", "MISSING FIELD", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int servings;
            try
            {
                servings = Integer.parseInt(inputQty);
                if (servings <= 0)
                {
                    JOptionPane.showMessageDialog(frame, "Servings must be greater than zero.", "INVALID QUANTITY", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(frame, "Servings must be a whole number.", "INVALID QUANTITY", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String> missing = checkDishStock(selectedDish, servings);
            if (!missing.isEmpty())
            {
                JOptionPane.showMessageDialog(frame,
                        "Insufficient stock for the following ingredients:\n" + String.join(", ", missing),
                        "STOCK ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String extraRemarks = txtRemarks.getText().trim();

            Map<String, Double> req = getDishRequirements(selectedDish, servings);
            for (Map.Entry<String, Double> entry : req.entrySet())
            {
                InventoryManager.getInstance().deductStock(entry.getKey(), entry.getValue());
                String dishRemarks = "X" + servings + " " + selectedDish + (extraRemarks.isEmpty() ? "" : " | " + extraRemarks);
                logs.add(new WasteLog(timeNow, entry.getKey(), String.valueOf(entry.getValue()), "Dish Waste", currentEmp, dishRemarks));
            }

            refreshTable();
        }
        else
        {
            JPanel panelAdd = new JPanel(new GridLayout(6, 2, 5, 10));

            JTextField txtTime = new JTextField(timeNow);
            txtTime.setEditable(false);

            List<invItem> invList = InventoryManager.getInstance().getInventoryList();
            String[] itemNames = new String[invList.size() + 1];
            itemNames[0] = "-Select Item-";
            for (int i = 0; i < invList.size(); i++) itemNames[i + 1] = invList.get(i).getItemName();
            JComboBox<String> cbItem = new JComboBox<>(itemNames);

            JTextField txtQty = new JTextField();

            String[] reasons = {"-Select Reason-", "Spoilage/Expired", "Leftovers",
                    "Customer Returns", "Contaminated", "Staff Error", "Other"};
            JComboBox<String> cbReason = new JComboBox<>(reasons);

            JTextField txtStaff = new JTextField(currentEmp);
            txtStaff.setEditable(false);
            txtStaff.setBackground(new Color(0xEE, 0xEE, 0xEE));

            JTextField txtRemarks = new JTextField();

            panelAdd.add(new JLabel("TIME:"));        panelAdd.add(txtTime);
            panelAdd.add(new JLabel("* FOOD ITEM:")); panelAdd.add(cbItem);
            panelAdd.add(new JLabel("* QUANTITY:"));  panelAdd.add(txtQty);
            panelAdd.add(new JLabel("* REASON:"));    panelAdd.add(cbReason);
            panelAdd.add(new JLabel("STAFF:"));       panelAdd.add(txtStaff);
            panelAdd.add(new JLabel("REMARKS:"));     panelAdd.add(txtRemarks);

            int userConfirm = JOptionPane.showConfirmDialog(frame, panelAdd, "ADD WASTE LOG — Ingredient", JOptionPane.OK_CANCEL_OPTION);
            if (userConfirm != JOptionPane.OK_OPTION) return;

            String inputItem   = (String) cbItem.getSelectedItem();
            String inputQty    = txtQty.getText().trim();
            String inputReason = (String) cbReason.getSelectedItem();

            if (inputItem.equals("-Select Item-"))
            {
                JOptionPane.showMessageDialog(frame, "Please select a food item.", "MISSING FIELD", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (inputReason.equals("-Select Reason-"))
            {
                JOptionPane.showMessageDialog(frame, "Please select a reason.", "MISSING FIELD", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (inputQty.isEmpty())
            {
                JOptionPane.showMessageDialog(frame, "Please enter a quantity.", "MISSING FIELD", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double parsedInputQty;
            try
            {
                parsedInputQty = Double.parseDouble(inputQty);
                if (parsedInputQty <= 0)
                {
                    JOptionPane.showMessageDialog(frame, "Quantity must be greater than zero.", "INVALID QUANTITY", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(frame, "Please enter a valid numeric quantity.", "INVALID QUANTITY", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!InventoryManager.getInstance().hasStock(inputItem, parsedInputQty))
            {
                JOptionPane.showMessageDialog(frame,
                        "Insufficient stock. The quantity entered exceeds what is available in inventory.",
                        "STOCK ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            InventoryManager.getInstance().deductStock(inputItem, parsedInputQty);
            logs.add(new WasteLog(timeNow, inputItem, inputQty, inputReason, currentEmp, txtRemarks.getText().trim()));
            refreshTable();
        }
    }

    private List<String> checkDishStock(String dishName, int servings)
    {
        List<String> missing = new ArrayList<>();
        for (Map.Entry<String, Double> entry : getDishRequirements(dishName, servings).entrySet())
        {
            if (!InventoryManager.getInstance().hasStock(entry.getKey(), entry.getValue()))
                missing.add(entry.getKey());
        }
        return missing;
    }

    private Map<String, Double> getDishRequirements(String dishName, int qty)
    {
        Map<String, Double> req = new LinkedHashMap<>();
        switch (dishName)
        {
            case "Chicken Adobo":
                req.put("Chicken",      0.30 * qty);
                req.put("Garlic",       0.02 * qty);
                req.put("Onion",        0.05 * qty);
                req.put("Soy Sauce",    0.05 * qty);
                req.put("Vinegar",      0.05 * qty);
                req.put("Black Pepper", 0.005 * qty);
                break;
            case "Chicharon Bulaklak":
                req.put("Pork Intestine", 0.30 * qty);
                req.put("Cooking Oil",    0.10 * qty);
                req.put("Salt",           0.01 * qty);
                break;
            case "Tortang Talong":
                req.put("Eggplant",    0.20 * qty);
                req.put("Egg",         2.0  * qty);
                req.put("Salt",        0.005 * qty);
                req.put("Cooking Oil", 0.02 * qty);
                break;
            case "Turon":
                req.put("Banana",              0.15 * qty);
                req.put("Spring Roll Wrapper", 1.0  * qty);
                req.put("Sugar",               0.02 * qty);
                req.put("Cooking Oil",         0.05 * qty);
                break;
            case "Iced Tea":
                req.put("Tea Powder", 0.01 * qty);
                req.put("Sugar",      0.03 * qty);
                req.put("Water",      0.50 * qty);
                break;
            case "Buko Juice":
                req.put("Coconut", 1.0  * qty);
                req.put("Water",   0.20 * qty);
                req.put("Sugar",   0.01 * qty);
                break;
        }
        return req;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnAddLog)        { showAddLogDialog(); }
        else if (e.getSource() == btnEditLogs) { enterEditMode(); }
        else if (e.getSource() == btnConfirmEdit) { exitEditMode(); }
    }
}