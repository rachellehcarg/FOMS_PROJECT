import java.util.Scanner;

public class payment_mastercard {
    private String cardNumber;
    private String cardPassword;

    // Constructor to initialize the PaymentMasterCard object with given card number and password
    public payment_mastercard(String cardNumber, String cardPassword) {
        this.cardNumber = cardNumber;
        this.cardPassword = cardPassword;
    }

    // Process the payment and return true if successful
    public boolean processPayment() {
        if (validateMasterCard()) {
            System.out.println("Validation succeed. Processing Mastercard payment...");
            // Here you would ideally integrate with a payment gateway
            return true;
        } else {
            System.out.println("Mastercard validation failed.");
            return false;
        }
    }

    // Validates the Mastercard details
    private boolean validateMasterCard() {
        // Simple validation to check if the card number and password are not null and card number is of correct length
        return cardNumber != null && cardNumber.length() == 16 && cardNumber.startsWith("5") && cardPassword != null && !cardPassword.isEmpty();
        // The condition cardNumber.startsWith("5") ensures that the card number starts with "5", which is specific to Mastercard.
    }
}
