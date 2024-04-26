import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;

public class Summary {
    private static final String ORDER_TYPE_TAKEAWAY = "Takeaway";
    private static final String ORDER_TYPE_DINE_IN = "Dine-in";

    public static void displayOrderDetails(String orderID) {
        boolean foundOrder = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("Summary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID: " + orderID)) {
                    foundOrder = true;
                    System.out.println(line);
                    // Display the rest of the order details (items, total price, order time, order type, order status, completion time)
                    for (int i = 0; i < 9; i++) { // Adjusted loop to accommodate order type
                        System.out.println(reader.readLine());
                    }
                    break;
                }
            }
            if (!foundOrder) {
                System.out.println("Order not found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading Summary.txt: " + e.getMessage());
        }
    }

    private static String generateOrderID() {
        String lastOrderID = getLastOrderID();
        int orderIDCounter = Integer.parseInt(lastOrderID) + 1;
        return String.format("%06d", orderIDCounter);
    }

    private static String getLastOrderID() {
        String lastOrderID = "000000"; // Default starting order ID
        try (BufferedReader reader = new BufferedReader(new FileReader("Summary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID:")) {
                    lastOrderID = line.substring(10).trim(); // Extract the order ID from the line
                }
            }
        } catch (IOException e) {
            // Handle file reading errors
            e.printStackTrace();
        }
        return lastOrderID;
    }

    public static void run(List<Item> items, double totalPrice, String branch, String orderType) {
        // Generate Order ID
        String orderID = generateOrderID();
        System.out.println(orderID);

        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderTime = dateFormat.format(new Date());

        

        // Default order status and completion time
        String orderStatus = "Ordered";
        String completionTime = "Incomplete";
        System.out.println("Order Type: " + orderType);

        // Write order details to Summary.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("Summary.txt", true))) {
            writer.println("Order ID: " + orderID);
            writer.println("Branch: " + branch);
            writer.println("Items:");
            for (Item item : items) {
                writer.println("- " + item.getName() + " - $" + item.getPrice());
            }

            DecimalFormat df = new DecimalFormat("0.00");
            String formattedTotalPrice = df.format(totalPrice);

            writer.println("Total Price: $" + formattedTotalPrice);
            writer.println("Order Time: " + orderTime);
            writer.println("Order Type: " + orderType); // Include order type
            writer.println("Order Status: " + orderStatus);
            writer.println("Completion Time: " + completionTime);
            writer.println();
            System.out.println("Order successfully placed.");
            System.out.println("Order ID: " + orderID);
            System.out.println("Branch: " + branch);
            System.out.println("Items:");
            for (Item item : items) {
                System.out.println("- " + item.getName() + " - $" + item.getPrice());
            }
            System.out.println("Total Price: $" + formattedTotalPrice);
            System.out.println("Order Time: " + orderTime);
            System.out.println("Order Type: " + orderType); // Include order type
            System.out.println("Order Status: " + orderStatus);
            System.out.println("Completion Time: " + completionTime);
        } catch (IOException e) {
            System.out.println("Error writing to Summary.txt: " + e.getMessage());
        }

        // Write order ID, branch, order time, order status, and order type to AllOrders.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("AllOrders.txt", true))) {
            writer.println(orderID + ";" + branch + ";" + orderTime + ";" + orderStatus + ";" + orderType);
            System.out.println("Order information stored in AllOrders.txt.");
        } catch (IOException e) {
            System.out.println("Error writing to AllOrders.txt: " + e.getMessage());
        }

        // Close the scanner
        
    }
    public static String getOrderStatus(String orderID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Summary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID: " + orderID)) {
                    // Find the line that contains the order status
                    while ((line = reader.readLine()) != null && !line.contains("Order Status:")) {
                        continue; // Skip until the order status line is found
                    }
                    if (line != null) {
                        return line.split(":")[1].trim(); // Return the order status
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Summary.txt: " + e.getMessage());
        }
        return "Order not found"; // Return this if the order ID is not found or if there's an error
    }
    public static void updateOrderStatus(String orderID, String newStatus) {
        File tempFile = new File("Summary.tmp");
        File summaryFile = new File("Summary.txt");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(summaryFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            boolean foundOrder = false;
    
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID: " + orderID)) {
                    writer.write(line + "\n");
                    foundOrder = true;
                    // Process all lines until the next order ID or end of file
                    while ((line = reader.readLine()) != null && !line.startsWith("Order ID:")) {
                        if (line.startsWith("Order Status:")) {
                            line = "Order Status: " + newStatus; // Change the order status
                        }
                        writer.write(line + "\n");
                    }
                    if (line != null && line.startsWith("Order ID:")) {
                        writer.write(line); // Keep writing new orders
                    }
                } else {
                    writer.write(line + "\n");
                }
            }
    
            writer.close();
            reader.close();
    
            if (!foundOrder) {
                System.out.println("Order ID not found.");
                return;
            }
    
            // Use more robust file operations
            if (summaryFile.delete()) {
                if (!tempFile.renameTo(summaryFile)) {
                    System.out.println("Failed to rename updated summary file.");
                }
            } else {
                System.out.println("Failed to delete old summary file.");
            }
    
        } catch (IOException e) {
            System.out.println("Error updating Summary.txt: " + e.getMessage());
        }
    }
}
