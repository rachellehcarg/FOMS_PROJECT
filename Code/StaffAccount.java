import java.io.*;
import java.util.*;


public class StaffAccount {
    private HashMap<String, Staff> staffAccounts; // StaffID maps to a Staff object
    private final String STORAGE_FILE = "staffAccounts.txt";
    private BranchStaffManagerCount branchStaffManagerCount;

    public StaffAccount() {
        this.staffAccounts = new HashMap<>();
        this.branchStaffManagerCount = new BranchStaffManagerCount(staffAccounts);
        loadStaffAccounts(); // Call loadStaffAccounts method to load data from the file
    }
    

    private void loadStaffAccounts() {
        try (Scanner scanner = new Scanner(new File(STORAGE_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] accountInfo = line.split(", ");
                if (accountInfo.length == 7) { // Adjusted to account for the password field
                    Staff staff = new Staff(accountInfo[0], accountInfo[1], accountInfo[2], accountInfo[3], accountInfo[4], accountInfo[5], accountInfo[6]);
                    staffAccounts.put(accountInfo[2], staff);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing staff accounts found. Creating a new storage.");
        }
    }
    

    private void saveStaffAccounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STORAGE_FILE))) {
            // Write existing staff members to the file
            for (Staff staff : staffAccounts.values()) {
                writer.println(String.join(", ", staff.getBranch(), staff.getName(), staff.getStaffID(), staff.getRole().substring(0, 1), staff.getGender().substring(0, 1), staff.getAge(), staff.getPassword()));
            }
        } catch (IOException e) {
            System.out.println("Error saving staff accounts.");
        }
    }
    public void updateStaffPassword(String staffID, String newPassword) {
        Staff staff = staffAccounts.get(staffID);
        if (staff != null) {
            staff.setPassword(newPassword); // Update the password in the Staff object
            saveStaffAccounts(); // Save the updated staff accounts to the file
        } else {
            System.out.println("Staff ID not found.");
        }
    }
    

    
    
    


    public boolean addStaff(String branch, String name, String role, String gender, String age) {
        // Create a new Staff object
        String defaultPassword = "00000000";
        Staff newStaff = new Staff(branch, name, generateStaffID(name), role.toUpperCase(), gender.toUpperCase(), age, defaultPassword);
    
        // Check if the new role is Manager
        if (role.equalsIgnoreCase("M")) {
            // Check if adding another manager exceeds the quota
            if (!branchStaffManagerCount.canAddManager(branch)) {
                System.out.println("Cannot add manager due to manager quota.");
                return false;
            }
        }
    
        // Add the new staff to the staffAccounts HashMap
        staffAccounts.put(newStaff.getStaffID(), newStaff);
    
        // Save the updated staff accounts to the file
        saveStaffAccounts();
    
        return true;
    }
    
    

    public boolean deleteStaffID(String staffID) {
        if (staffAccounts.remove(staffID) != null) {
            saveStaffAccounts();
            return true;
        }
        return false;
    }

    public boolean changeStaffRole(String staffID, String newRole, String branch) {
        // Validate the new role
        if (!Validation.isValidRole(newRole)) {
            System.out.println("Invalid role. Only 'M' for Manager, 'S' for Staff, or 'A' for Admin are accepted.");
            return false;
        }
    
        // Retrieve the staff member
        Staff staff = staffAccounts.get(staffID);
        if (staff == null) {
            System.out.println("Staff ID does not exist.");
            return false;
        }
    
        // Check if the new role is manager and if adding a manager exceeds the quota
        if (newRole.equalsIgnoreCase("M")) {
            // Check if adding another manager exceeds the quota
            if (!branchStaffManagerCount.canAddManager(branch)) {
                System.out.println("Cannot add manager due to manager quota.");
                return false;
            }
        }
    
        // Update the staff member's role
        staff.setRole(newRole.toUpperCase());
    
        // Save the changes to the staff accounts file
        saveStaffAccounts();
    
        System.out.println("Information updated successfully.");
        return true;
    }
    
    
    
    
    
    
    
    
    

    // Example of how changeStaffInformation might be adjusted
    public boolean changeStaffInformation(String staffID, String infoChoice, String newValue) {
        Staff staff = staffAccounts.get(staffID);
        if (staff == null) {
            System.out.println("Staff ID does not exist.");
            return false;
        }

        switch (infoChoice) {
            case "1": // Change branch
                staff.setBranch(newValue);
                break;
                case "2": // Change role
                if (!Validation.isValidRole(newValue)) {
                    System.out.println("Invalid role. Only 'M' for Manager, 'S' for Staff, or 'A' for Admin are accepted.");
                    return false;
                }
    
                // Extract branch name from staffID
                BranchManager branchManager = new BranchManager();
                String branchName = branchManager.getBranchNameFromStaffID(staffID);
    
                // Check manager quota before changing the role
                if (newValue.equalsIgnoreCase("M")) {
                    if (!branchStaffManagerCount.canAddManager(branchName)) {
                        System.out.println("Cannot change role to Manager due to manager quota.");
                        return false;
                    }
                }
    
                staff.setRole(newValue.toUpperCase());
                break;
            case "3": // Change age
                if (!Validation.isValidAge(newValue)) {
                    System.out.println("Invalid age.");
                    return false;
                }
                staff.setAge(newValue);
                break;
            default:
                System.out.println("Invalid choice for information update.");
                return false;
        }

        saveStaffAccounts();
        return true;
    }


    private String generateStaffID(String name) {
        String normalized = name.trim().replaceAll("\\s+", " ");
        if (normalized.isEmpty()) return "";
    
        String[] parts = normalized.split("\\s+");
        // Check if there are at least two parts to the name (first and last)
        String staffID;
        if (parts.length >= 2) {
            // Combine the first name with the first letter of the last name
            staffID = parts[0] + parts[parts.length - 1].substring(0, 1);
        } else {
            // If only one part is available, just use that part as the staff ID
            staffID = parts[0];
        }
        return staffID; // Return the staff ID in uppercase
    }
    
    
    public void displayStaffIDs() {
        System.out.println("Current Staff Accounts:");
        for (Staff staff : staffAccounts.values()) {
            // Exclude the password from the displayed information
            System.out.println(String.join(", ", staff.getBranch(), staff.getName(), staff.getStaffID(), staff.getRole(), staff.getGender(), staff.getAge()));
        }
    }
    
    
    public void displayFilteredStaff(String filterChoice, String filterDetail) {
        System.out.println("Filtered Staff List:");
        boolean anyMatchesFound = false;
        for (Staff staff : staffAccounts.values()) {
            boolean matchesFilter = false;
            switch (filterChoice) {
                case "1":
                    matchesFilter = staff.getBranch().equalsIgnoreCase(filterDetail);
                    break;
                case "2":
                    matchesFilter = staff.getRole().equalsIgnoreCase(filterDetail);
                    break;
                case "3":
                    matchesFilter = staff.getGender().equalsIgnoreCase(filterDetail);
                    break;
                case "4":
                    matchesFilter = staff.getAge().equals(filterDetail);
                    break;
                case "5":
                    matchesFilter = true; // Display all
                    break;
            }
            if (matchesFilter) {
                // Exclude the password from the displayed information
                System.out.println(String.join(", ", staff.getBranch(), staff.getName(), staff.getStaffID(), staff.getRole(), staff.getGender(), staff.getAge()));
                anyMatchesFound = true;
            }
        }
        if (!anyMatchesFound && !filterChoice.equals("5")) {
            System.out.println("No staff members match the selected filter.");
        }
    }

    
    


 
    
 

    public HashMap<String, Staff> getStaffAccounts() {
        return this.staffAccounts;
    }

    // Retrieve a specific staff member by staff ID
    public Staff getStaff(String staffID) {
        return staffAccounts.get(staffID);
    }


	

    // Existing code...


    
    // Assuming the implementation of displayStaffIDs, generateStaffID, and displayFilteredStaff methods remain unchanged
    // Implement these methods as needed, ensuring they integrate with the rest of your system.

    // Staff inner class and other utility methods (e.g., generateStaffID) would also be part of this class.
}
