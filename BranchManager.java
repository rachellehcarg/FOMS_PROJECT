import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class BranchManager {
    private HashMap<String, Branch> branches;

    public BranchManager() {
        this.branches = new HashMap<>();
        loadBranchesFromFile(); // Load branches from file
    }
    
    public boolean addBranch(String branchName, String branchLocation) {
        if ( branches.containsKey(branchName)) {
            return false; // Branch already exists
        }
        branches.put(branchName, new Branch(branchName, branchLocation));
        saveBranchesToFile(); // Save branches to file
        return true;
    }
    
    public boolean deleteBranch(String branchName) {
        if (!branches.containsKey(branchName)) {
            return false; // Branch does not exist
        }
        branches.remove(branchName);
        saveBranchesToFile(); // Save branches to file
        return true;
    }

    public Branch getBranch(String branchName) {
        return branches.get(branchName);
    }

    public HashMap<String, Branch> getBranches() {
        return branches;
    }

    private void saveBranchesToFile() {
        try (PrintWriter writer = new PrintWriter(new File("branches.txt"))) {
            for (String branchName : branches.keySet()) {
                String branchLocation = branches.get(branchName).getLocation();
                writer.println(branchName + "," + branchLocation);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error saving branches to file.");
        }
    }

    private void loadBranchesFromFile() {
        try (Scanner scanner = new Scanner(new File("branches.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String branchName = parts[0];
                String branchLocation = parts[1];
                branches.put(branchName, new Branch(branchName, branchLocation));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing branches found. Starting fresh.");
        }
    }

    public String getBranchNameFromStaffID(String staffID) {
        String[] parts = staffID.split("_");
        return parts[0];
    }

	

	
}
