import java.util.HashMap;

public class BranchStaffManagerCount {
	
    private HashMap<String, Staff> staffAccounts;

    public BranchStaffManagerCount(HashMap<String, Staff> staffAccounts) {
        this.staffAccounts = staffAccounts;
    }

    public BranchStaffManagerCount() {
		// TODO Auto-generated constructor stub
	}

	public int calculateManagerQuota(int nonManagerCount) {
        if (nonManagerCount >= 1 && nonManagerCount <= 4) {
            return 1;
        } else if (nonManagerCount >= 5 && nonManagerCount <= 8) {
            return 2;
        } else if (nonManagerCount >= 9 && nonManagerCount <= 15) {
            return 3;
        }
        return 3; // Assuming a maximum of 3 managers for more than 15 non-manager staff members.
    }

    public int countManagersInBranch(String branch) {
    	//HashMap<String, Staff> staffAccounts = staffAccounts.getStaffAccounts();
    	if (this.staffAccounts == null) {
            return 0; // Return 0 if staffAccounts is not initialized
        }
        int managerCount = 0;
        for (Staff staff : staffAccounts.values()) {
            if (staff.getBranch().equalsIgnoreCase(branch) && staff.getRole().equalsIgnoreCase("M")) {
                managerCount++;
            }
        }
        return managerCount;
    }

    public int countNonManagerStaffInBranch(String branch) {
    	//HashMap<String, Staff> staffAccounts = staffAccounts.getStaffAccounts();
    	if (this.staffAccounts == null) {
            return 0; // Return 0 if staffAccounts is not initialized
        }
        int nonManagerCount = 0;
        for (Staff staff : staffAccounts.values()) {
            if (staff.getBranch().equalsIgnoreCase(branch) && !staff.getRole().equalsIgnoreCase("S")) {
                nonManagerCount++;
            }
        }
        return nonManagerCount;
    }

    public boolean canAddManager(String branch) {
        int nonManagerCount = countNonManagerStaffInBranch(branch);
        int currentManagers = countManagersInBranch(branch);
        int allowedManagers = calculateManagerQuota(nonManagerCount);
        return currentManagers < allowedManagers;
    }
}
