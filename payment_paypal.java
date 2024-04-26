import java.util.Scanner;

public class payment_paypal {
    private String accountEmail;
    private String accountPassword;

    // Constructor to initialize the PaymentPayPal object with PayPal account credentials
    public payment_paypal(String accountEmail, String accountPassword) {
        this.accountEmail = accountEmail;
        this.accountPassword = accountPassword;
    }

    // Process the payment and return true if successful
    public boolean processPayment(double amount) {
        if (authenticate()) {
            System.out.println("Processing PayPal payment...");
            
            return true;
        } else {
            System.out.println("PayPal authentication failed.");
            return false;
        }
    }

    // Authenticate the PayPal account details
    private boolean authenticate() {
    	// Regular expression to match email format
    	if(accountEmail != null && !accountEmail.isEmpty() && accountPassword != null && !accountPassword.isEmpty()) {
    		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

            // Check if the email matches the regex pattern
            if (accountEmail.matches(emailRegex)) {
                // Simple validation to check if the email and password are not null or empty
                return accountPassword != null && !accountPassword.isEmpty();
            } else {
                System.out.println("Invalid email format.");
                return false;
            }
    	}else {
    		return false;
    	}
        
        
    }
}

