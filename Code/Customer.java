import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class Customer {
    private Menu menu;
    private Cart cart;
    private String branch;
    private String orderType;
    private Scanner scanner;

    public Customer(Menu menu) {
        this.menu = menu;
        this.cart = new Cart();
        this.scanner = new Scanner(System.in); // Initialize Scanner object
    }

    public void run() {
        AdminAccount admin = new AdminAccount();
        BranchManager branchManager = new BranchManager();
        admin.viewBranches();
        while (true) {
            System.out.print("Enter branch name: ");
            branch = scanner.nextLine();

            if (branchManager.getBranch(branch) == null) {
                System.out.println("Location not found. Please try again.");
            } else {
                break;
            }
        }

        boolean exit = false;

        while (!exit) {
            displayCart();
            System.out.println("\nCustomer Menu for Branch " + branch + ":");
            System.out.println("1. View Menu by Category and order");
            System.out.println("2. View Whole Menu and order");
            System.out.println("3. Finish ordering and proceed to payment");
            System.out.println("4. Track my order");
            System.out.println("5. Display the cart");
            System.out.print("Enter your choice: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1:
                        viewMenuByCategory();
                        break;
                    case 2:
                        viewWholeMenu();
                        break;
                    case 3:
                        selectOrderType();
                        proceedToPayment();
                        exit = true;
                        if (cart.getTotalPrice() == 0) {
                            return;
                        }
                        System.exit(0);
                        break;
                    case 4:
                        trackOrder();
                        break;
                    case 5:
                    	displayCart();
                    	break;
                    default:
                        System.out.println("Invalid choice.");
                }
                if (!exit && choice<4) {
                    handleCartOptions(scanner);
                }
            } catch (Exception e) {
                System.err.println("Error: Invalid input. Please enter a number.");
                scanner.nextLine(); // consume newline
            }
        }
    }

    private void selectOrderType() {
        System.out.println("Select order type:");
        System.out.println("1. Takeaway");
        System.out.println("2. Dine-in");
        System.out.print("Enter your choice: ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (choice == 1) {
                orderType = "Takeaway";
            } else if (choice == 2) {
                orderType = "Dine-in";
            } else {
                System.out.println("Invalid choice. Defaulting to Takeaway.");
                orderType = "Takeaway";
            }
        } catch (Exception e) {
            System.err.println("Error: Invalid input. Defaulting to Takeaway.");
            orderType = "Takeaway";
            scanner.nextLine(); // consume newline
        }
    }

    private void proceedToPayment() {
        PaymentManager paymentManager = new PaymentManager();
        if (cart.getTotalPrice() == 0) {
            System.out.println("Error: No items selected.");
            run();return;
        }
        System.out.println("Total amount due: $" + cart.getTotalPrice());

        // Display available payment methods
        try {
            Set<String> availableMethods = paymentManager.getPaymentMethods();
            if (availableMethods.isEmpty()) {
                System.out.println("No payment methods available.");
                System.exit(0);
                return; // Early exit if no methods are available
            }

            System.out.println("Available Payment Methods:");
            for (String method : availableMethods) {
                System.out.println(method);
            }

            // Prompt the customer to choose a payment method
            System.out.print("Enter your preferred payment method: ");
            String paymentMethod = scanner.nextLine();
            while (!availableMethods.contains(paymentMethod)) {
                System.out.println("Invalid payment method selected. Please try again.");
                paymentMethod = scanner.nextLine();
            }

            System.out.println("Proceeding to payment with " + paymentMethod + "...");
            Payment payment = new Payment(cart.getTotalPrice(), paymentMethod);
            boolean paymentResult = payment.run(); // Adjusted to expect a boolean return from run()

            if (paymentResult) {
                Summary summary = new Summary();
                summary.run(cart.getItems(), cart.getTotalPrice(), branch, orderType);
            } else {
                System.out.println("Payment failed. Transaction terminated.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void trackOrder() {
        System.out.print("Enter your Order ID: ");
        String orderID = scanner.nextLine();

        Summary.displayOrderDetails(orderID);
        String orderStatus = Summary.getOrderStatus(orderID);

        if ("Uncollected".equals(orderStatus)) {
            System.out.println("Your order status is 'Uncollected'. Please contact the staff for help.");
        } else if ("Ready for Pickup".equals(orderStatus)) {
            System.out.println("Your order is ready for pickup.");
            System.out.print("Press 1 to pickup now or 0 to pickup later: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (choice == 1) {
                    Summary.updateOrderStatus(orderID, "Completed");
                    System.out.println("Thank you for your visit. Your order status has been changed to 'Completed'.");
                } else {
                    System.out.println("You can pick up your order later.");
                }
            } catch (Exception e) {
                System.err.println("Error: Invalid input. Please enter a number.");
                scanner.nextLine(); // consume newline
            }
        }
    }

    public void displayCart() {
        System.out.println("\nCurrent Cart:");
        cart.displayCart();
    }

    public void viewMenuByCategory() {
        menu.displayCategories(branch);
        System.out.print("Enter category index: ");
        try {
            int categoryIndex = scanner.nextInt();
            scanner.nextLine(); // consume newline
            menu.viewMenuByCategory(branch, categoryIndex);
        } catch (Exception e) {
            System.err.println("Error: Invalid input. Please enter a number.");
            scanner.nextLine(); // consume newline
        }
    }

    public void viewWholeMenu() {
        menu.viewMenu(branch);
    }

    public void handleCartOptions(Scanner scanner) {
        boolean stayInCartOptions = true;
        while (stayInCartOptions) {
            System.out.println("\nCart Options:");
            System.out.println("1. Add Item to Cart");
            System.out.println("2. Remove Item from Cart");
            System.out.println("3. Clear Cart");
            System.out.println("4. Back to Menu");
            System.out.print("Enter your choice: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1:
                        addItemToCart(scanner);
                        break;
                    case 2:
                        removeItemFromCart(scanner);
                        break;
                    case 3:
                        clearCart();
                        break;
                    case 4:
                        stayInCartOptions = false; // Exit the loop to go back to the menu
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Error: Invalid input. Please enter a number.");
                scanner.nextLine(); // consume newline
            }
        }
    }


    public void addItemToCart(Scanner scanner) {
        System.out.print("Enter the index of the item to add to cart: ");
        try {
            int itemNumber = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.print("Enter the number of the item to add to cart: ");
            int q = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            while (q <= 0) {
                System.out.println("Quantity must be greater than zero. Please enter a valid quantity.");
                System.out.print("Enter the number of the item to add to cart: ");
                q = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            }

            // Retrieve branch-specific menu
            ArrayList<Item> branchMenu = menu.getBranchMenu(branch);
            // Check if item number is valid
            if (branchMenu != null && itemNumber >= 1 && itemNumber <= branchMenu.size()) {
                Item item = branchMenu.get(itemNumber - 1);
                item.setAmount(q);
                cart.addItem(item);
            } else {
                System.out.println("Invalid item number or branch.");
            }
        } catch (Exception e) {
            System.err.println("Error: Invalid input. Please enter a number.");
            scanner.nextLine(); // consume newline
        }
    }

    public void removeItemFromCart(Scanner scanner) {
        System.out.print("Enter item name to remove from cart: ");
        String itemName = scanner.nextLine();
        Item item = menu.findItemByName(branch, itemName);
        System.out.print("Enter the number of the item to add to cart: ");
        int q = scanner.nextInt();
        scanner.nextLine();
        while (q <= 0) {
            System.out.println("Quantity must be greater than zero. Please enter a valid quantity.");
            System.out.print("Enter the number of the item to add to cart: ");
            q = scanner.nextInt();
            scanner.nextLine(); 
        }
        if (item != null) {
        	while(q>0) {
        		cart.removeItem(item);
        	}
            
        } else {
            System.out.println("Item not found in the cart.");
        }
    }

    public void clearCart() {
        cart.clearCart();
    }
}
