import java.util.Scanner;
import java.util.InputMismatchException;

public class LoginPage {

    private Scanner scanner;

    public LoginPage() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome!");
            System.out.println("1. Log in as Customer");
            System.out.println("2. Log in as Staff");
            System.out.println("3. Exit");

            int choice = getUserChoice(3);

            switch (choice) {
                case 1:
                    loginAsCustomer();
                    break;
                case 2:
                    loginAsStaff();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    break;
            }
        }
        scanner.close();
    }

    private int getUserChoice(int maxChoice) {
        int choice = 0;
        boolean isValidChoice = false;
        while (!isValidChoice) {
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (choice >= 1 && choice <= maxChoice) {
                    isValidChoice = true;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + maxChoice + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        return choice;
    }

    private void loginAsCustomer() {
    	Menu menu = new Menu();
        Customer customer = new Customer(menu);
        //System.out.println("Go Order your food");
        customer.run();
    }

    private void loginAsStaff() {
        boolean isValidLogin = false;

        while (!isValidLogin) {
            System.out.println("Please choose an option:");
            System.out.println("1. Login as Regular Staff");
            System.out.println("2. Login as Manager");
            System.out.println("3. Login as Admin");
            System.out.println("4. Go Back");

            int choice = getUserChoice(4);

            switch (choice) {
                case 1:
                    // Login as regular staff
                    loginAsRegularStaff();
                    break;
                case 2:
                    // Login as manager
                    loginAsManager();
                    break;
                case 3:
                    // Login as admin
                    loginAsAdmin();
                    break;
                case 4:
                    // Go back to previous menu
                    return;
            }
        }
    }

    private void loginAsManager() {
    	boolean isValidLogin = false;

        while (!isValidLogin) {
            LoginVerification loginVerification = new LoginVerification("staffAccounts.txt");
            String loginMessage = loginVerification.ManagerattemptLogin();

            if (loginMessage != null) {
                System.out.println(loginMessage);
                String branchOfStaff = loginVerification.getReturnBranch(); 
                Manager manager = new Manager(branchOfStaff);
                manager.run();
                isValidLogin = true;
            } else {
                System.out.println("Admin Login failed. Please try again.");
                boolean tryAgain = getUserConfirmation("Do you want to try again? (yes/no)");
                if (!tryAgain) {
                    return; // Go back to previous menu
                }
            }
        }
    }

    private void loginAsRegularStaff() {
    	boolean isValidLogin = false;

        while (!isValidLogin) {
            LoginVerification loginVerification = new LoginVerification("staffAccounts.txt");
            String loginMessage = loginVerification.StaffattemptLogin();

            if (loginMessage != null) {
                System.out.println(loginMessage);
                String branchOfStaff = loginVerification.getReturnBranch(); 
                Staff staff = new Staff(branchOfStaff);
                staff.run();
                isValidLogin = true;
            } else {
                System.out.println("Admin Login failed. Please try again.");
                boolean tryAgain = getUserConfirmation("Do you want to try again? (yes/no)");
                if (!tryAgain) {
                    return; // Go back to previous menu
                }
            }
        }
    }

    private void loginAsAdmin() {
        boolean isValidLogin = false;

        while (!isValidLogin) {
            LoginVerification loginVerification = new LoginVerification("staffAccounts.txt");
            String loginMessage = loginVerification.AdminattemptLogin();

            if (loginMessage != null) {
                System.out.println(loginMessage);
                new AdminAccount().run();
                isValidLogin = true;
            } else {
                System.out.println("Admin Login failed. Please try again.");
                boolean tryAgain = getUserConfirmation("Do you want to try again? (yes/no)");
                if (!tryAgain) {
                    return; // Go back to previous menu
                }
            }
        }
    }

    private boolean getUserConfirmation(String message) {
        while (true) {
            try {
                System.out.println(message);
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("yes")) {
                    return true;
                } else if (input.equals("no")) {
                    return false;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred while processing your input. Please try again.");
            }
        }
    }
}
