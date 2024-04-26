import java.util.regex.Pattern;

public class Validation {

    public static boolean isValidRole(String role) {
        return role.equalsIgnoreCase("M") || role.equalsIgnoreCase("S") || role.equalsIgnoreCase("A");
    }

    public static boolean isValidGender(String gender) {
        return gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F");
    }

    public static boolean isValidAge(String age) {
        try {
            int parsedAge = Integer.parseInt(age);
            return parsedAge > 0 && parsedAge < 200;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        boolean hasLetter = Pattern.compile("[a-zA-Z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        return hasLetter && hasDigit && password.length() >= 8;
    }

    public static boolean isValidNewPassword(String password) {
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Check if the password contains at least one number and one letter
        boolean hasNumber = false;
        boolean hasLetter = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (Character.isLetter(c)) {
                hasLetter = true;
            }
        }

        return hasNumber && hasLetter;
    }


}
