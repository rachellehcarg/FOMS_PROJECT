import java.util.ArrayList;
import java.util.List;

public class Branch {
    private String branchName;
    private String location; // New attribute for branch location
    private List<Staff> staffMembers;

    public Branch(String branchName, String location) {
        this.branchName = branchName;
        this.location = location;
        this.staffMembers = new ArrayList<>();
    }

    // Getters and setters for branchName, location, and staffMembers
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Staff> getStaffMembers() {
        return staffMembers;
    }

    public void setStaffMembers(List<Staff> staffMembers) {
        this.staffMembers = staffMembers;
    }

    // Other methods...
    public void addStaff(Staff staff) {
        staffMembers.add(staff);
    }

    public boolean removeStaff(String staffId) {
        return staffMembers.removeIf(staff -> staff.getStaffID().equals(staffId));
    }

    // Other methods related to branch management
    // ...
}
