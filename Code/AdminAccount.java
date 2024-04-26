import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AdminAccount {

    private BranchManager branchManager;
    private StaffAccount staffAccount;
    private Scanner scanner;
    private BranchStaffManagerCount branchStaffManagerCount;

    public AdminAccount() {
        this.branchManager = new BranchManager();
        this.staffAccount = new StaffAccount();
        this.scanner = new Scanner(System.in);
        this.branchStaffManagerCount = new BranchStaffManagerCount(staffAccount.getStaffAccounts());
    }

    public void run(){

        boolean running = true;
        while (running){
        	try {
            System.out.println("Enter\n1 to view or add or delete a staff\n2 to change staff branch or role or age\n3 to view or add or delete branches\n4 to view or add or delete Payment Method\n5 assign a staff to manager\n0 Logout");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                System.out.println("Enter\n1 to view staff\n2 to add a staff\n3 to delete a staff");
                String nextchoice = scanner.nextLine();
    
                switch (nextchoice) {

                    case "1":
                        System.out.println("View staff list. Choose a filter: \n1. Branch \n2. Role \n3. Gender \n4. Age \n5. View everything");
                        String filterChoice = scanner.nextLine();
                        String filterDetail = "";

                        switch (filterChoice) {
                            case "1":
                                viewBranches();
                                System.out.println("Enter Branch to filter by:");
                                filterDetail = scanner.nextLine();
                                break;
                            case "2":
                                System.out.println("Enter Role to filter by (M for Manager or S for Staff):");
                                filterDetail = scanner.nextLine();
                                break;
                            case "3":
                                System.out.println("Enter Gender to filter by (M for Male or F for Female):");
                                filterDetail = scanner.nextLine();
                                break;
                            case "4":
                                System.out.println("Enter Age to filter by:");
                                filterDetail = scanner.nextLine();
                                break;
                            case "5":
                                System.out.println("Viewing all staff...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please enter 1, 2, 3, 4, or 5.");
                                break;
                        }
                
                        staffAccount.displayFilteredStaff(filterChoice, filterDetail);
                        break;

                    case "2":

                        // Assuming both staffAccount and branchManager are defined and instantiated in this class
                        StaffRegistration staffRegistration = new StaffRegistration(staffAccount, branchManager);
                        staffRegistration.registerNewStaff();
                        break;

                    case "3":
                        System.out.println("Enter the Staff ID of the staff to delete:");
                        String staffID = scanner.nextLine();
                        if (staffAccount.deleteStaffID(staffID)) {
                            System.out.println("Staff deleted successfully.");
                        } else {
                            System.out.println("Failed to delete staff. Staff ID might not exist.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                        break;
                    
                }
                break;    
                
                case "2":
                    System.out.println("Enter the StaffID to change information:");
                    String staffIDForInfoChange = scanner.nextLine();
                    System.out.println("Enter 1 to change branch, 2 to change role, 3 to change age:");
                    String infoChoice = scanner.nextLine();
                    String newValue = "";
                
                    switch (infoChoice) {
                        case "1":
                            System.out.println("Enter new Branch:");
                            newValue = scanner.nextLine();
                            break;
                        case "2":
                            System.out.println("Enter new Role (M for Manager or S for Staff or A for Admin):");
                            newValue = scanner.nextLine();
                            break;
                        case "3":
                            System.out.println("Enter new Age:");
                            newValue = scanner.nextLine();
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                            break;
                    }
                    
                    if (!newValue.isEmpty() && staffAccount.changeStaffInformation(staffIDForInfoChange, infoChoice, newValue)) {
                        System.out.println("Information updated successfully.");
                    } 
                    else if (!newValue.isEmpty()) {
                        System.out.println("Failed to update information.");
                    }
                    break;

                case "3":
                    manageBranches();
                    break;
                
                case "4":
                    PaymentManager paymentManager = new PaymentManager();
                    paymentManager.startMenu();
                    break;
                case "5":
                    // Call method to assign staff to manager
                    String branchName;
                    while (true) {
                    	System.out.println("Enter the branch name to assign a manager:");
                        branchName = scanner.nextLine();

                        if (branchManager.getBranch(branchName) == null) {
                            System.out.println("Location not found. Please try again.");
                        } else {
                            break;
                        }
                    }

                    assignStaffToManager(branchName);
                    break;
                    
                case "0":
                    System.out.println("Logging out");
                    System.exit(0);
                    running = false;
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2 or 3.");
                    break;
            }
        } catch (NoSuchElementException e) {
            System.err.println("Input not found. Please try again.");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        }
        scanner.close();
    }
    private void addBranch() {
    	try {
    		System.out.println("Current branches:");
            for (String branchName : branchManager.getBranches().keySet()) {
                System.out.println(branchName);
            }
            System.out.println("Enter Branch Name:");
            String branchName = scanner.nextLine();
            System.out.println("Enter Branch Location:");
            String branchLocation = scanner.nextLine(); // Prompt for branch location
            if (branchManager.addBranch(branchName, branchLocation)) {
                System.out.println("Branch added successfully.");
            } else {
                System.out.println("Failed to add branch. It may already exist.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter a valid option.");
            scanner.next(); // Clear the invalid input
        } catch (NoSuchElementException e) {
            System.err.println("Input not found. Please try again.");
        }
    	
        
    }
    

    private void deleteBranch() {
    	try {
    		System.out.println("Enter Branch Name to delete:");
            String branchName = scanner.nextLine();
            if (branchManager.deleteBranch(branchName)) {
                System.out.println("Branch deleted successfully.");
            } else {
                System.out.println("Failed to delete branch. It may not exist or it may have active staff members.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter a valid option.");
            scanner.next(); // Clear the invalid input
        } catch (NoSuchElementException e) {
            System.err.println("Input not found. Please try again.");
        }
    	
        
    }


    public void viewBranches() {
        System.out.println("Current branches:");
        for (String branchName : branchManager.getBranches().keySet()) {
            String branchLocation = branchManager.getBranches().get(branchName).getLocation();
            System.out.println(branchName + " - Location: " + branchLocation);
        }
    }
    
    // Other methods...


    

    // ... other methods ...
    private void manageBranches() {
    	boolean managingBranches = true;
        try {
            while (managingBranches) {
                System.out.println("Enter\n1 to view branches\n2 to add a branch\n3 to delete a branch\n0 to return to main menu:");
                String branchChoice = scanner.nextLine();

                switch (branchChoice) {
                    case "1":
                        // Call method to view branches
                        viewBranches();
                        break;
                    case "2":
                        // Call method to add a branch
                        addBranch();
                        break;
                    case "3":
                        // Call method to delete a branch
                        deleteBranch();
                        break;
                    case "0":
                        managingBranches = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, 3, or 0.");
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter a valid option.");
            scanner.next(); // Clear the invalid input
        } catch (NoSuchElementException e) {
            System.err.println("Input not found. Please try again.");
        }
    }
    private void assignStaffToManager(String branchName) {
    	try {
    		int currentManagers = branchStaffManagerCount.countManagersInBranch(branchName);

            // Get the quota for managers based on the number of non-managerial staff
            int nonManagerCount = branchStaffManagerCount.countNonManagerStaffInBranch(branchName);
            int allowedManagers = branchStaffManagerCount.calculateManagerQuota(nonManagerCount);

            // Check if the current number of managers is less than the quota
            if (currentManagers < allowedManagers) {
                System.out.println("The branch currently has " + currentManagers + " manager(s), and the quota allows for " + allowedManagers + " manager(s).");
                System.out.println("Would you like to assign a manager to this branch? (Y/N)");
                String choice = scanner.nextLine().toUpperCase();

                if (choice.equals("Y")) {
                    // Display non-managerial staff in the branch
                    System.out.println("Non-managerial staff in branch '" + branchName + "':");
                    staffAccount.displayFilteredStaff("1", branchName); // Filter by branch

                    // Prompt admin to enter the ID of the staff member to be assigned as manager
                    System.out.println("Enter the Staff ID of the staff member to assign as manager:");
                    String staffID = scanner.nextLine();

                    // Check if the entered staff ID corresponds to a staff member in the specified branch
                    Staff staff = staffAccount.getStaff(staffID);
                    if (staff != null && staff.getBranch().equalsIgnoreCase(branchName)) {
                        // Update the role of the selected staff member to "Manager"
                        staff.setRole("M");
                        // Display a confirmation message
                        System.out.println("Staff member with ID " + staffID + " has been assigned as the manager of branch '" + branchName + "'.");
                    } else {
                        System.out.println("Invalid Staff ID or Staff member does not belong to the specified branch.");
                    }
                } else if (!choice.equals("N")) {
                    System.out.println("Invalid choice. Please enter 'Y' for yes or 'N' for no.");
                }
            } else {
                System.out.println("The branch already has the maximum number of managers allowed by quota.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter a valid option.");
            scanner.next(); // Clear the invalid input
        } catch (NoSuchElementException e) {
            System.err.println("Input not found. Please try again.");
        }
    	
    	
    }

    
    
    
}
