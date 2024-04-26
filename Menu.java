import java.io.*;
import java.util.*;

public class Menu {
    private HashMap<String, ArrayList<Item>> branchMenus = new HashMap<>();
    private final String STORAGE_FILE = "menu_list.txt";
    private String branch = "default_branch"; // Initialize with a default value
    private final String[] FIXED_CATEGORIES = {"seasonal", "set meal", "burger", "side", "drink"};

    // Rest of the class remains unchanged
    // ...



    public Menu() {
        loadMenuItems();
    }
    public Menu(String branch) {
        this.branch = branch;
        loadMenuItems();
    }
    private void loadMenuItems() {
        try (Scanner scanner = new Scanner(new File(STORAGE_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 5) { // Check if there are 5 columns
                    String branch = parts[0].trim();
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    String category = parts[3].trim();
                    String description = parts[4].trim();
                    Item item = new Item(name, price, category, description);
                    branchMenus.computeIfAbsent(branch, k -> new ArrayList<>()).add(item);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Menu file not found, starting with an empty menu.");
        }
    }

    private void saveMenuItems() {
        try (PrintWriter out = new PrintWriter(new FileWriter(STORAGE_FILE))) {
            for (Map.Entry<String, ArrayList<Item>> entry : branchMenus.entrySet()) {
                for (Item item : entry.getValue()) {
                    String line = String.format("%s;%s;%.2f;%s;%s",
                            entry.getKey(),
                            item.getName(),
                            item.getPrice(),
                            item.getCategory(),
                            item.getDescription());
                    out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to save menu items.");
        }
    }

    public void addMenuItem(String branch, Item item) {
        if (branchMenus.containsKey(branch)) {
            branchMenus.get(branch).add(item);
            saveMenuItems(); // Save the updated menu items to file
            System.out.println("Item added to the menu successfully.");
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void viewMenu() {
        for (String branch : branchMenus.keySet()) {
            viewMenu(branch);
        }
    }

    
    public void viewMenu(String branch) {
        if (branchMenus.containsKey(branch)) {
            System.out.println("Menu for branch: " + branch);
            ArrayList<Item> branchMenu = branchMenus.get(branch);
            for (int i = 0; i < branchMenu.size(); i++) {
                System.out.println((i + 1) + ". " + branchMenu.get(i));
            }
        } else {
            System.out.println("Branch not found.");
        }
    }


    public void displayCategories() {
        for (String branch : branchMenus.keySet()) {
            displayCategories(branch);
        }
    }

    public void displayCategories(String branch) {
        System.out.println("Fixed Categories for Branch: " + branch);
        for (int i = 0; i < FIXED_CATEGORIES.length; i++) {
            System.out.println((i + 1) + ". " + FIXED_CATEGORIES[i]);
        }
    }

    public void deleteMenuItem(String itemName) {
        for (String branch : branchMenus.keySet()) {
            deleteMenuItem(branch, itemName);
        }
    }

    public void deleteMenuItem(String branch, String itemName) {
        if (branchMenus.containsKey(branch)) {
            ArrayList<Item> branchMenu = branchMenus.get(branch);
            Item itemToRemove = null;
            for (Item item : branchMenu) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    itemToRemove = item;
                    break;
                }
            }
            if (itemToRemove != null) {
                branchMenu.remove(itemToRemove);
                saveMenuItems(); // Save the updated menu items to file
                System.out.println("Item deleted from the menu successfully.");
            } else {
                System.out.println("Item not found in the menu.");
            }
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void editMenuItem(String itemName, String newDescription) {
        for (String branch : branchMenus.keySet()) {
            editMenuItem(branch, itemName, newDescription);
        }
    }

    public void editMenuItem(String branch, String itemName, String newDescription) {
        if (branchMenus.containsKey(branch)) {
            ArrayList<Item> branchMenu = branchMenus.get(branch);
            for (Item item : branchMenu) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    item.setDescription(newDescription);
                    saveMenuItems(); // Save the updated menu items to file
                    System.out.println("Item description updated successfully.");
                    return;
                }
            }
            System.out.println("Item not found in the menu.");
        } else {
            System.out.println("Branch not found.");
        }
    }
    public void viewBranchMenu(String branch) {
        if (branchMenus.containsKey(branch)) {
            System.out.println("Menu for branch: " + branch);
            ArrayList<Item> branchMenu = branchMenus.get(branch);
            for (Item item : branchMenu) {
                System.out.println(item);
            }
        } else {
            System.out.println("Branch not found.");
        }
    }
    public void addMenuItemToBranch(String branch) {
    	if (branchMenus.containsKey(branch)) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter item details:");
            System.out.print("Name: ");
            String name = scanner.nextLine();

            // Check for duplicate item names
            if (isDuplicateName(branch, name)) {
                System.out.println("Error: Item with the same name already exists in the menu.");
                return;
            }

            System.out.print("Price: ");
            double price;
            while (true) {
                //System.out.print("Enter the price: ");
                price = scanner.nextDouble();
                scanner.nextLine(); 
                
                if (price > 0) {
                    System.out.println("Price:" + price);
                    break;
                } else {
                    System.out.println("Price must be greater than zero. Please enter a valid price.");
                }
            }

        
            
            System.out.print("Category: ");
            String category = scanner.nextLine();
            System.out.print("Description: ");
            String description = scanner.nextLine();

            Item newItem = new Item(name, price, category, description);
            branchMenus.get(branch).add(newItem);
            saveMenuItems(); // Save the updated menu items to file
            System.out.println("Item added to the menu of branch '" + branch + "' successfully.");
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void deleteMenuItemFromBranch(String branch) {
        if (branchMenus.containsKey(branch)) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the name of the item to delete: ");
            String itemName = scanner.nextLine();

            ArrayList<Item> branchMenu = branchMenus.get(branch);
            Item itemToRemove = null;
            for (Item item : branchMenu) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    itemToRemove = item;
                    break;
                }
            }
            if (itemToRemove != null) {
                branchMenu.remove(itemToRemove);
                saveMenuItems(); // Save the updated menu items to file
                System.out.println("Item '" + itemName + "' deleted from the menu of branch '" + branch + "' successfully.");
            } else {
                System.out.println("Item not found in the menu of branch '" + branch + "'.");
            }
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void editMenuItem(String branch) {
        if (branchMenus.containsKey(branch)) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the name of the item to edit: ");
            String itemName = scanner.nextLine();

            ArrayList<Item> branchMenu = branchMenus.get(branch);
            for (Item item : branchMenu) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    System.out.println("Select property to edit:");
                    System.out.println("1. Price");
                    System.out.println("2. Category");
                    System.out.println("3. Description");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
 
                            double newPrice;
                    
                            while (true) {
                                try {
                                    System.out.print("Enter new price: ");
                                    newPrice = scanner.nextDouble();
                    
                                    if (newPrice <= 0) {
                                        System.out.println("Price must be a positive number.");
                                        continue;
                                    }
                                    
                                    // If the input is valid, break out of the loop
                                    break;
                                } catch (InputMismatchException e) {
                                    // If the input is not a valid double, catch the exception and prompt again
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    scanner.nextLine(); // Consume the invalid input
                                }
                            }
                            
                            item.setPrice(newPrice);
                            break;
                        case 2:
                            System.out.print("Enter new category: ");
                            String newCategory = scanner.nextLine();
                            item.setCategory(newCategory);
                            break;
                        case 3:
                            System.out.print("Enter new description: ");
                            String newDescription = scanner.nextLine();
                            item.setDescription(newDescription);
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            return;
                    }
                    saveMenuItems(); // Save the updated menu items to file
                    System.out.println("Item '" + itemName + "' in branch '" + branch + "' updated successfully.");
                    return;
                }
            }
            System.out.println("Item not found in the menu of branch '" + branch + "'.");
        } else {
            System.out.println("Branch not found.");
        }
    }
    public void viewMenuByCategory(int categoryIndex) {
        for (String branch : branchMenus.keySet()) {
            viewMenuByCategory(branch, categoryIndex);
        }
    }

    public void viewMenuByCategory(String branch, int categoryIndex) {
        if (categoryIndex >= 1 && categoryIndex <= FIXED_CATEGORIES.length) {
            String category = FIXED_CATEGORIES[categoryIndex - 1];
            viewMenuByCategory(branch, category);
        } else {
            System.out.println("Invalid category index.");
        }
    }

    private void viewMenuByCategory(String branch, String category) {
        if (branchMenus.containsKey(branch)) {
            System.out.println("Menu for branch: " + branch + ", Category: " + category);
            ArrayList<Item> branchMenu = branchMenus.get(branch);
            boolean found = false;
            int index = 1;
            for (Item item : branchMenu) {
                if (item.getCategory().equalsIgnoreCase(category)) {
                    System.out.println(index + ". " + item);
                    found = true;
                }
                index++;
            }
            if (!found) {
                System.out.println("No items found in the category: " + category);
            }
        } else {
            System.out.println("Branch not found.");
        }
    }

    public void findItemByName(String itemName) {
        for (String branch : branchMenus.keySet()) {
            findItemByName(branch, itemName);
        }
    }

    public Item findItemByName(String branch, String itemName) {
        if (branchMenus.containsKey(branch)) {
            ArrayList<Item> branchMenu = branchMenus.get(branch);
            for (Item item : branchMenu) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    return item;
                }
            }
            System.out.println("Item not found in the menu.");
            return null; // Item not found
        } else {
            System.out.println("Branch not found.");
            return null; // Branch not found
        }
    }
    public ArrayList<Item> getBranchMenu(String branch) {
        return branchMenus.get(branch);
    }
    private boolean isDuplicateName(String branch, String itemName) {
        for (Item item : branchMenus.get(branch)) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true; // Duplicate name found
            }
        }
        return false; // No duplicate names found
    }
}
