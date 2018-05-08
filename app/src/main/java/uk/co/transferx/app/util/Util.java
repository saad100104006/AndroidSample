package uk.co.transferx.app.util;

/**
 * Created by sergey on 15.12.17.
 */

public final class Util {

    private static final String VALIDATE_PATTERN_EMAIL = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

    private static final String VALIDATE_PATTERN_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d+)[0-9a-zA-Z!@#$%]{8,}$";

    private static final String VALIDATE_PATTERN_PHONE = "^\\+(?:[0-9]●?){6,14}[0-9]$";

    private static final String VALIDATE_PATTERN_NAME = "^[a-zA-Z\\s]+$";

    public static boolean validateEmail(String email) {
        return email.matches(VALIDATE_PATTERN_EMAIL);
    }

    public static boolean validatePassword(String password) {
        return password != null && password.matches(VALIDATE_PATTERN_PASSWORD);
    }

    public static boolean validatePhone(String phone) {
        return phone != null && phone.matches(VALIDATE_PATTERN_PHONE);
    }

    public static boolean validateName(String name) {
        return name != null && name.matches(VALIDATE_PATTERN_NAME);
    }

    public static boolean isNullorEmpty(String text) {
        return text == null || text.isEmpty();
    }
}
