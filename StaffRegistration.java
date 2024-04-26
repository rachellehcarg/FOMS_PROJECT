import java.util.Scanner;
import java.util.Set;

public class StaffRegistration {
    private Scanner scanner;
    private StaffAccount staffAccount;
    private BranchManager branchManager;

    public StaffRegistration(StaffAccount staffAccount, BranchManager branchManager) {
        this.scanner = new Scanner(System.in);
        this.staffAccount = staffAccount;
        this.branchManager = branchManager;
    }

    public void registerNewStaff() {
        System.out.println("Current branches:");
        Set<String> branches = branchManager.getBranches().keySet();
        branches.forEach(System.out::println);

        System.out.println("Enter Branch, or 'NA' to skip:");
        String branch = scanner.nextLine().trim();
        boolean branchExists = branches.contains(branch);
        boolean isNotApplicable = "NA".equalsIgnoreCase(branch);

        if (branchExists || isNotApplicable) {
            if (!isNotApplicable) {
                // Branch exists, continue to collect staff details
                System.out.println("Adding staff to " + branch + " branch.");
            } else {
                // No specific branch selected
                System.out.println("No branch selected. Proceeding without assigning to a specific branch.");
                branch = null; // or however you wish to handle the 'NA' case
            }

            System.out.println("Enter Full Name:");
            String name = scanner.nextLine();
            System.out.println("Enter Role (M for Manager or S for Staff or A for Admin):");
            String role = scanner.nextLine();
            System.out.println("Enter Gender (M for Male or F for Female):");
            String gender = scanner.nextLine();
            System.out.println("Enter Age:");
            String age = scanner.nextLine();
            

            // Use the branch variable, which may be null if 'NA' was chosen
            if (staffAccount.addStaff(branch, name, role, gender, age)) {
                System.out.println("Staff added successfully.");
            } else {
                System.out.println("Failed to add staff. Ensure all inputs are correct and meet the specified criteria.");
            }

        } else {
            // Branch doesn't exist
            System.out.println("Error: The branch does not exist. Please try again.");
            // Optionally, you might want to loop back and ask again.
        }
    }
}
