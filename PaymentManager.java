import java.util.*;
import java.io.File;
import java.io.IOException;
public class PaymentManager  {
    private static final String FILENAME_PREFIX = "payment_";
    private static final String FILE_EXTENSION = ".java";

    
    public boolean addPaymentMethod(String method) {
        if (paymentFileExists(method)) {
            System.out.println("Payment method already exists.");
            return false;
        } else {
            if (createPaymentFile(method)) {
                System.out.println("Payment method added successfully.");
                return true;
            } else {
                System.out.println("Failed to add payment method.");
                return false;
            }
        }
    }

   
    public boolean deletePaymentMethod(String method) {
        if (!paymentFileExists(method)) {
            System.out.println("Payment method does not exist.");
            return false;
        } else {
            if (deletePaymentFile(method)) {
                System.out.println("Payment method deleted successfully.");
                return true;
            } else {
                System.out.println("Failed to delete payment method.");
                return false;
            }
        }
    }

   
    public Set<String> getPaymentMethods() {
        Set<String> methods = new HashSet<>();
        File directory = new File(".");
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.getName().startsWith(FILENAME_PREFIX) && file.getName().endsWith(FILE_EXTENSION)) {
                String method = file.getName().substring(FILENAME_PREFIX.length(), file.getName().length() - FILE_EXTENSION.length());
                methods.add(method);
            }
        }

        return methods;
    }

  
    public boolean createPaymentFile(String method) {
        String filename = FILENAME_PREFIX + method.toLowerCase() + FILE_EXTENSION;
        try {
            File file = new File(filename);
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deletePaymentFile(String method) {
        String filename = FILENAME_PREFIX + method.toLowerCase() + FILE_EXTENSION;
        File file = new File(filename);
        return file.delete();
    }

    private boolean paymentFileExists(String method) {
        String filename = FILENAME_PREFIX + method.toLowerCase() + FILE_EXTENSION;
        File file = new File(filename);
        return file.exists();
    }

   
    public void startMenu() {
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("1. View current payment methods");
            System.out.println("2. Add a payment method");
            System.out.println("3. Delete a payment method");
            System.out.println("0. Back to Home");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Current payment methods:");
                    Set<String> methods = getPaymentMethods();
                    for (String method : methods) {
                        System.out.println(method);
                    }
                    break;
                case 2:
                    System.out.print("Enter the payment method to add: ");
                    scanner.nextLine(); // Consume newline
                    String newMethod = scanner.nextLine();
                    addPaymentMethod(newMethod);
                    break;
                case 3:
                    System.out.print("Enter the payment method to delete: ");
                    scanner.nextLine(); // Consume newline
                    String deleteMethod = scanner.nextLine();
                    deletePaymentMethod(deleteMethod);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please enter again.");
            }
        } while (choice != 0);

    }

}
