import java.io.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class Staff {
    private String branch;
    private String name;
    private String staffID;
    private String role;
    private String gender;
    private String age;
    private String password;

    public Staff(String branch, String name, String staffID, String role, String gender, String age, String password) {
        this.branch = branch;
        this.name = name;
        this.staffID = staffID;
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.password = password;
    }

    public String getBranch() { return branch; }
    public String getName() { return name; }
    public String getStaffID() { return staffID; }
    public String getRole() { return role; }
    public String getGender() { return gender; }
    public String getAge() { return age; }
    public String getPassword() {return password;}

    public void setBranch(String branch) { this.branch = branch.toUpperCase(); }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role.toUpperCase(); }
    public void setGender(String gender) { this.gender = gender.toUpperCase(); }
    public void setAge(String age) { this.age = age; }
    public void setPassword(String password) { this.password = password;}

    public Staff(String branch) {
        this.branch = branch;
    }


    // Getters and setters for member variables
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            try {
                System.out.println("Staff Menu for Branch " + branch + ":");
                System.out.println("1. View Orders");
                System.out.println("2. Process Order");
                System.out.println("3. Complete Order");
                System.out.println("4. Log out");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        viewOrders(branch);
                        break;

                    case 2:
                        processOrder();
                        break;
                    case 3:
                        completeOrder();
                        break;
                    case 4:
                        System.out.println("Logging out...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
        scanner.close();
    }
    
    public void viewOrders() {
        try (BufferedReader reader = new BufferedReader(new FileReader("AllOrders.txt"))) {
            String line;
            System.out.println("Orders for Branch " + branch + ":");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2 && parts[1].trim().equals(branch)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading AllOrders.txt: " + e.getMessage());
        }
    }

    public void processOrder() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Order ID to process: ");
        String orderID = scanner.nextLine();

        // Check if order ID exists (implementation not shown)
        updateOrderStatusInSummary(orderID, "Processing");
        updateOrderStatusInAllOrders(orderID, "Processing");
        // Update order status to "Being Processed" in AllOrders.txt and Summary.txt

    }


    public void viewOrders(String branch) {
        try (BufferedReader reader = new BufferedReader(new FileReader("AllOrders.txt"))) {
            String line;
            System.out.println("Orders for Branch " + branch + ":");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2 && parts[1].trim().equals(branch)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading AllOrders.txt: " + e.getMessage());
        }
    }
    

    public static void updateOrderStatusInAllOrders(String orderID, String newStatus) {
        // File path
        String filePath = "AllOrders.txt";
    
        try {
            // Open the original file for reading and writing
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            String line;
            StringBuilder fileContent = new StringBuilder();
            boolean orderFound = false;
    
            // Loop through the file line by line
            while ((line = file.readLine()) != null) {
                // Check if the line contains the Order ID
                if (line.startsWith(orderID + ";")) {
                    // Append the updated order status
                    line = orderID + ";" + line.split(";")[1] + ";" + line.split(";")[2] + ";" + newStatus;
                    orderFound = true;
                }
                // Append the line to the StringBuilder
                fileContent.append(line).append("\n");
            }
    
            // Write the updated content back to the file
            file.seek(0);
            file.setLength(0); // Clear the file content
            file.writeBytes(fileContent.toString());
    
            // Close the file
            file.close();
    
            if (orderFound) {
                System.out.println("Order status updated in AllOrders.txt successfully.");
            } else {
                System.out.println("Order ID not found in AllOrders.txt.");
            }
    
        } catch (IOException e) {
            System.out.println("Error updating order status in AllOrders.txt: " + e.getMessage());
        }
    }
    
    private void updateOrderStatusInSummary(String orderID, String newStatus) {
        // File path
        String filePath = "Summary.txt";
    
        try {
            // Open the file for reading and writing
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            String line;
            StringBuilder fileContent = new StringBuilder();
            boolean orderFound = false;
    
            // Loop through the file line by line
            while ((line = file.readLine()) != null) {
                // Check if the line contains the Order ID
                if (line.startsWith("Order ID: " + orderID)) {
                    // Indicate that the order was found
                    orderFound = true;
                    // Update the order status directly
                    fileContent.append(line).append("\n"); // Append Order ID line
                    // Read the next lines until the next Order ID
                    while ((line = file.readLine()) != null && !line.startsWith("Order ID: ")) {
                        // Update the Order Status line
                        if (line.startsWith("Order Status:")) {
                            line = "Order Status: " + newStatus;
                        }
                        fileContent.append(line).append("\n");
                    }
                } else {
                    // Append the line as is
                    fileContent.append(line).append("\n");
                }
            }
    
            // Write the updated content back to the file
            file.seek(0);
            file.setLength(0); // Clear the file content
            file.writeBytes(fileContent.toString());
    
            // Close the file
            file.close();
    
            if (orderFound) {
                System.out.println("Order status updated in Summary.txt successfully.");
            } else {
                System.out.println("Order ID not found in Summary.txt.");
            }
    
        } catch (IOException e) {
            System.out.println("Error updating order status in Summary.txt: " + e.getMessage());
        }
    }
    
    
    

    private void removeOrderFromAllOrders(String orderID) {
        // File path
        String filePath = "AllOrders.txt";
    
        try {
            // Open the original file for reading and writing
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            StringBuilder fileContent = new StringBuilder();
            boolean orderFound = false;
    
            // Loop through the file line by line
            String line;
            while ((line = file.readLine()) != null) {
                // Check if the line contains the Order ID
                if (line.startsWith(orderID + ";")) {
                    orderFound = true;
                } else {
                    // Append the line to the StringBuilder
                    fileContent.append(line).append("\n");
                }
            }
    
            // Clear the file content
            file.setLength(0);
    
            // Write the updated content back to the file
            file.writeBytes(fileContent.toString());
    
            // Close the file
            file.close();
    
            if (orderFound) {
                System.out.println("Order removed from AllOrders.txt successfully.");
            } else {
                System.out.println("Order ID not found in AllOrders.txt.");
            }
    
        } catch (IOException e) {
            System.out.println("Error removing order from AllOrders.txt: " + e.getMessage());
        }
    }
    


    public void completeOrder() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Order ID to complete: ");
        String orderID = scanner.nextLine();
    
        // Update order status to "Ready for Pickup" in Summary.txt
        updateOrderStatusInSummary(orderID, "Ready for Pickup");
    
        // Update completion time in Summary.txt
        updateCompletionTimeInSummary(orderID);
    
        // Remove the corresponding row from AllOrders.txt
        removeOrderFromAllOrders(orderID);

        MarkAsUncollected(orderID);
    }


    public void MarkAsUncollected(final String orderID) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Check if the order status is "Ready for Pickup"
                String currentStatus = Summary.getOrderStatus(orderID);
                if (currentStatus.equals("Ready for Pickup")) {
                    // If the status is "Ready for Pickup", update it to "Uncollected"
                    Summary.updateOrderStatus(orderID, "Uncollected");
                    System.out.println("Order status changed to 'Uncollected'.");
                } else {
                    // If the status is not "Ready for Pickup", do nothing
                    System.out.println("Order status is not 'Ready for Pickup'.");
                }
            }
        }, 500000); // 5000 milliseconds = 5 seconds
    }

    private void updateCompletionTimeInSummary(String orderID) {
        // File path
        String filePath = "Summary.txt";

        try {
            // Open the file for reading and writing
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            String line;
            StringBuilder fileContent = new StringBuilder();
            boolean orderFound = false;

            // Loop through the file line by line
            while ((line = file.readLine()) != null) {
                // Check if the line contains the Order ID
                if (line.startsWith("Order ID: " + orderID)) {
                    // Indicate that the order was found
                    orderFound = true;
                    // Update the completion time directly
                    fileContent.append(line).append("\n"); // Append Order ID line
                    // Read the next lines until the next Order ID
                    while ((line = file.readLine()) != null && !line.startsWith("Order ID: ")) {
                        // Update the Completion Time line
                        if (line.startsWith("Completion Time:")) {
                            // Get current time
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String currentTime = dateFormat.format(new Date());
                            line = "Completion Time: " + currentTime;
                        }
                        fileContent.append(line).append("\n");
                    }
                } else {
                    // Append the line as is
                    fileContent.append(line).append("\n");
                }
            }

            // Write the updated content back to the file
            file.seek(0);
            file.setLength(0); // Clear the file content
            file.writeBytes(fileContent.toString());

            // Close the file
            file.close();

            if (orderFound) {
                System.out.println("Completion time updated in Summary.txt successfully.");
            } else {
                System.out.println("Order ID not found in Summary.txt.");
            }

        } catch (IOException e) {
            System.out.println("Error updating completion time in Summary.txt: " + e.getMessage());
        }
    }
}
