package uk.co.transferx.app.util;

/**
 * Created by sergey on 15.12.17.
 */

public final class Util {

    private static final String VALIDATE_PATTERN_EMAIL = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

    private static final String VALIDATE_PATTERN_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d+)[0-9a-zA-Z!@#$%]{8,}$";

    public static boolean validateEmail(String email) {
        return email.matches(VALIDATE_PATTERN_EMAIL);
    }

    public static boolean validatePassword(String password) {
        return password.matches(VALIDATE_PATTERN_PASSWORD);
    }
}
