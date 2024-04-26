import java.util.Scanner;
import java.util.concurrent.*;

public class Payment {
    private double amount;
    private String paymentMethod;

    // Constructor to initialize the payment details
    public Payment(double amount, String paymentMethod) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    // Run method to process the payment with a timeout
    public boolean run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            if ("MasterCard".equalsIgnoreCase(paymentMethod)) {
                return processMasterCard();
            } else if ("PayPal".equalsIgnoreCase(paymentMethod)) {
                return processPayPal();
            } else {
                // For other payment methods, we assume payment is processed immediately
                return processPayment();
            }
        });

        try {
            // Obtain the future result with a 30-second timeout
            boolean result = future.get(60, TimeUnit.SECONDS);
            if (result) {
                System.out.println("Payment processed successfully.");
            } else {
                System.out.println("Payment failed to process.");
            }
            return result;
        } catch (TimeoutException e) {
            System.out.println("Payment timed out. Failed to complete within 30 seconds.");
            future.cancel(true); // Cancel the running task
            return false;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error occurred during payment processing: " + e.getMessage());
            return false;
        } finally {
            executor.shutdownNow(); // Ensure all threads are shutdown
        }
    }

    // Helper method to process MasterCard payment
    private boolean processMasterCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Mastercard number: ");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your Mastercard password: ");
        String cardPassword = scanner.nextLine();
        scanner.close(); // Close the scanner to free up resources
        payment_mastercard masterCard = new payment_mastercard(cardNumber, cardPassword);
        return masterCard.processPayment(); // Process the payment and return the result
    }

    // Helper method to process PayPal payment
    private boolean processPayPal() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your PayPal email: ");
        String email = scanner.nextLine();
        System.out.println("Enter your PayPal password: ");
        String password = scanner.nextLine();
        scanner.close(); // Close the scanner to free up resources
        payment_paypal payPal = new payment_paypal(email, password);
        return payPal.processPayment(amount); // Process the payment and return the result
    }

    // Default method to process other payments
    private boolean processPayment() {
        System.out.println("Processing payment of $" + amount + " via " + paymentMethod);
        return true;
    }
}
