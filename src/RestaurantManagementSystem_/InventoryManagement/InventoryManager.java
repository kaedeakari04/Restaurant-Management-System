package RestaurantManagementSystem_.InventoryManagement;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private static InventoryManager instance;
    private List<invItem> inventoryList = new ArrayList<>();
    private List<invItem> deliveryList  = new ArrayList<>();

    private static int itemCounter     = 1001;
    private static int deliveryCounter = 1001;

    private InventoryManager() {}

    public static String generateItemID() {
        return String.format("IT%04d", itemCounter++);
    }

    public static void rollbackItemID() {
        if (itemCounter > 1001) itemCounter--;
    }

    public static String generateStocksID() {
        return String.format("ST%04d", deliveryCounter++);
    }

    public static InventoryManager getInstance() {
        if (instance == null) instance = new InventoryManager();
        return instance;
    }

    public List<invItem> getInventoryList() { return inventoryList; }
    public List<invItem> getDeliveryList()  { return deliveryList;  }

    public void receiveDelivery(invItem delivered) {
        for (invItem item : inventoryList) {
            if (item.getItemName().equalsIgnoreCase(delivered.getItemName())) {
                if (!item.getItemCategory().equalsIgnoreCase(delivered.getItemCategory())) {
                    throw new IllegalArgumentException("Item found but category does not match.");
                } else {
                    item.setItemQuantity(item.getItemQuantity() + delivered.getItemQuantity());
                    item.setItemCurrentStatus(computeStatus(item.getItemQuantity(), item.getItemCategory()));
                    return;
                }
            }
        }
        inventoryList.add(delivered);
        delivered.setItemCurrentStatus(computeStatus(delivered.getItemQuantity(), delivered.getItemCategory()));
    }

    public boolean hasStock(String itemName, double amount) {
        for (invItem item : inventoryList) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                return item.getItemQuantity() >= amount;
            }
        }
        return false;
    }

    public boolean itemExistsInInventory(String itemName) {
        for (invItem item : inventoryList) {
            if (item.getItemName().equalsIgnoreCase(itemName)) return true;
        }
        return false;
    }

    public boolean deductStock(String itemName, double amount) {
        for (invItem item : inventoryList) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                if (item.getItemQuantity() < amount) return false;
                item.setItemQuantity(item.getItemQuantity() - amount);
                item.setItemCurrentStatus(computeStatus(item.getItemQuantity(), item.getItemCategory()));
                return true;
            }
        }
        return false;
    }

    public void addStock(String itemName, double amount) {
        for (invItem item : inventoryList) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                item.setItemQuantity(item.getItemQuantity() + amount);
                item.setItemCurrentStatus(computeStatus(item.getItemQuantity(), item.getItemCategory()));
                return;
            }
        }
    }

    public List<String> getLowStockItems() {
        List<String> lowItems = new ArrayList<>();
        for (invItem item : inventoryList) {
            if ("Low".equals(item.getItemCurrentStatus())) {
                lowItems.add(item.getItemName() + " (" + item.getItemQuantity() + " " + item.getItemMeasurement() + ")");
            }
        }
        return lowItems;
    }

    public static String computeStatus(double quantity, String category) {
        double lowItemBasis;
        switch (category.toUpperCase()) {
            case "MEAT":       lowItemBasis = 15;  break;
            case "SEASONING":  lowItemBasis = 0.5; break;
            case "VEGETABLE":  lowItemBasis = 2;   break;
            case "FRUIT":      lowItemBasis = 1;   break;
            case "CONDIMENTS": lowItemBasis = 5;   break;
            case "OTHERS":     lowItemBasis = 1;   break;
            default:           lowItemBasis = 10;  break;
        }
        if (quantity <= lowItemBasis) return "Low";
        return "Good";
    }
}