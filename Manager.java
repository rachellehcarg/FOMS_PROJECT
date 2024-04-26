import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Manager extends Staff {
    public Manager(String branch) {
        super(branch);
    }
        
    // Additional constructor if needed
    public Manager(String branch, String name, String staffID, String role, String gender, String age, String password) {
        super(branch, name, staffID, role, gender, age, password);
    }
    
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Manager Menu for Branch " + getBranch() + ":");
            System.out.println("1. View Orders");
            System.out.println("2. Process Order");
            System.out.println("3. Complete Order");
            System.out.println("4. Edit Menu");
            System.out.println("5. View Staff in My Branch");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            
            // Check if there's more input available
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine(); // Read user input as a string
                if (input.equals("6")) {
                    System.exit(0); // Exit the loop if the user chooses option 6
                } else {
                    try {
                        int choice = Integer.parseInt(input);
                        switch (choice) {
                            case 1:
                                viewOrders();
                                break;
                            case 2:
                                processOrder();
                                break;
                            case 3:
                                completeOrder();
                                break;
                            case 4:
                                editMenu();
                                break;
                            case 5:
                                viewStaff();
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
            } else {
                System.out.println("No input provided. Exiting...");
                exit = true;
            }
        }
        scanner.close();
    }

    // Method to edit the menu
    public void editMenu() {
        Menu menu = new Menu(getBranch()); // Create a Menu instance for the current branch
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Edit Menu:");
            System.out.println("1. View Item");
            System.out.println("2. Add Item");
            System.out.println("3. Delete Item");
            System.out.println("4. Edit Item Description");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        menu.viewBranchMenu(getBranch());
                        break;
                    case 2:
                        menu.addMenuItemToBranch(getBranch());
                        break;
                    case 3:
                        menu.deleteMenuItemFromBranch(getBranch());
                        break;
                    case 4:
                        menu.editMenuItem(getBranch());
                        break;
                    case 5:
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    public void viewStaff() {
        StaffAccount staffAccount = new StaffAccount();
		System.out.println("Staff under Manager in Branch " + getBranch() + ":");
		staffAccount.displayFilteredStaff("1", getBranch());
    }
}
