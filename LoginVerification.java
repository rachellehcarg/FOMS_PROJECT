import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginVerification {
    private HashMap<String, String[]> credentials;

    private StaffAccount staffAccount;
    private String returnBranch;

    public void setReturnBranch(String username) {
        this.returnBranch = username;
    }

    public String getReturnBranch() {
        return returnBranch;
    }

    public LoginVerification(String fileName) {
        this.credentials = loadCredentialsFromTextFile(fileName);
        this.staffAccount = new StaffAccount(); // Initialize StaffAccount
    }

    public String AdminattemptLogin() {
        String staffID = askForStaffID();
        String password = askForPassword();
        String goingtoprintrole;
        if (verifyLogin(staffID, password)) {
            String[] accountInfo = credentials.get(staffID);
            if (accountInfo[3].equals("A")) {
                goingtoprintrole = "Admin";
                if (accountInfo[6].equals("00000000")) {
                    changePasswordPrompt(staffID); // Call the new changePasswordPrompt method
                }
            } else {
                return null;
            }
            String goingtoprint = "Successful Login: " + accountInfo[1] + "\nYour role is: " + goingtoprintrole;
            return goingtoprint;

        } else {
            return null;
        }
    }


    public String ManagerattemptLogin() {
        String staffID = askForStaffID();
        String password = askForPassword();
        if (verifyLogin(staffID, password)) {
            String[] accountInfo = credentials.get(staffID);
            if (accountInfo[6].equals("00000000")) {
                changePasswordPrompt(staffID); // Call the changePasswordPrompt method
            }
            if (accountInfo[3].equals("M")) {
                setReturnBranch(accountInfo[0]);
                String goingtoprintrole = "Manager";
                String goingtoprint = "Successful Login: " + accountInfo[1] + "\nYour role is: " + goingtoprintrole;
                return goingtoprint;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String StaffattemptLogin() {
        String staffID = askForStaffID();
        String password = askForPassword();
        if (verifyLogin(staffID, password)) {
            String[] accountInfo = credentials.get(staffID);
            if (accountInfo[6].equals("00000000")) {
                changePasswordPrompt(staffID); // Call the changePasswordPrompt method
            }
            if (accountInfo[3].equals("S")) {
                setReturnBranch(accountInfo[0]);
                String goingtoprintrole = "Staff";
                String goingtoprint = "Successful Login: " + accountInfo[1] + "\nYour role is: " + goingtoprintrole;
                return goingtoprint;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    private void changePasswordPrompt(String staffID) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your current password is the default. Do you want to change it? (Y/N)");
        String choice = scanner.nextLine().toUpperCase();
    
        if (choice.equalsIgnoreCase("Y")) {
            System.out.println("Enter a new password (at least 8 characters, including numbers and letters):");
            String newPassword = scanner.nextLine();
            if (Validation.isValidNewPassword(newPassword)) {
                String hashedpassword = hashPassword(newPassword);
                changePassword(staffID, hashedpassword);
                System.out.println("Password changed successfully.");
            } else {
                System.out.println("Invalid password format. Password must be at least 8 characters long and include both numbers and letters.");
            }
        } else if (choice.equalsIgnoreCase("N")) {
            System.out.println("Proceeding with the default password.");
        } else {
            System.out.println("Invalid choice. Proceeding with the default password.");
        }
    }

    private void changePassword(String staffID, String newPassword) {
     // Hash the password using StaffAccount's hashPassword method
        String[] accountInfo = credentials.get(staffID);
        if (accountInfo != null) {
            accountInfo[6] = newPassword; // Update the password in the account information array with the hashed password
            credentials.put(staffID, accountInfo); // Update the credentials map
            saveCredentialsToFile(); // Save changes to the credentials file
        } else {
            System.out.println("Staff ID not found.");
        }
    }
    

    private void saveCredentialsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("staffAccounts.txt"))) {
            for (Map.Entry<String, String[]> entry : credentials.entrySet()) {
                String[] accountInfo = entry.getValue();
                writer.println(String.join(", ", accountInfo));
            }
        } catch (IOException e) {
            System.out.println("Error saving staff accounts.");
        }
    }
    

    private boolean verifyLogin(String staffID, String inputPassword) {
        String[] accountInfo = credentials.get(staffID);
        if (accountInfo != null) {
            // Retrieve the stored hashed password
            String storedPasswordHash = accountInfo[6]; // Assuming password hash is stored at index 6
            
            // Hash the input password using the same hashing algorithm
            String inputPasswordHash = hashPassword(inputPassword);
            
            if (accountInfo[6].equals("00000000")){
                return true;
            }
            // Check if the hashed input password matches the stored password hash
            else if (inputPasswordHash != null && inputPasswordHash.equals(storedPasswordHash)) {
                // Check if the password is the default password
                return true; // Return true for successful login
            }
        }
        return false; // Return false for incorrect password
    }
    
    
    
    
    

    private String askForStaffID() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your staff ID:");
        return scanner.nextLine();
    }

    private String askForPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your password:");
        return scanner.nextLine();
    }

    private HashMap<String, String[]> loadCredentialsFromTextFile(String fileName) {
        HashMap<String, String[]> credentials = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] accountInfo = line.split(", ");
                if (accountInfo.length >= 6) { // Check if there are at least 6 fields
                    String password = accountInfo.length == 7 ? accountInfo[6] : "00000000"; // Get the password or use the default if not present
                    String[] modifiedAccountInfo = new String[7];
                    System.arraycopy(accountInfo, 0, modifiedAccountInfo, 0, accountInfo.length);
                    modifiedAccountInfo[6] = password; // Set the password field
                    credentials.put(accountInfo[2], modifiedAccountInfo);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
        return credentials;
    }
    

    public void updatePassword(String staffID, String newPassword) {
        String[] accountInfo = credentials.get(staffID);
        if (accountInfo != null) {
            accountInfo[6] = newPassword; // Update the password
            credentials.put(staffID, accountInfo); // Update credentials in memory

            // Write updated credentials back to file
            try {
                PrintWriter writer = new PrintWriter(new FileWriter("staffAccounts.txt"));
                for (String[] info : credentials.values()) {
                    writer.println(String.join(", ", info));
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.println("Staff ID not found.");
        }
    }





    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Update the digest with the password bytes
            md.update(password.getBytes());
            
            // Get the hashed bytes
            byte[] hashedBytes = md.digest();
            
            // Convert the hashed bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            
            // Return the hashed password as a hexadecimal string
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the case where the SHA-256 algorithm is not available
            e.printStackTrace();
            return null;
        }
    }

}
