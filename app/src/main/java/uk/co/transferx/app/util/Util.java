package uk.co.transferx.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

import static uk.co.transferx.app.util.Constants.EMPTY;

/**
 * Created by sergey on 15.12.17.
 */

public final class Util {

    private static final String VALIDATE_PATTERN_EMAIL = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

    private static final String VALIDATE_PATTERN_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d+)[0-9a-zA-Z!@#$%]{8,}$";

    private static final String VALIDATE_PATTERN_PHONE = "^(?:[0-9]●?){6,14}[0-9]$";

    private static final String VALIDATE_PATTERN_NAME = "^[a-zA-Z\\s]+$";

    private static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.'000Z'";

    public static boolean validateEmail(String email) {
        return email != null && email.matches(VALIDATE_PATTERN_EMAIL);
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

    public static String formattedDateForSend(Date date) {
        SimpleDateFormat formatDateToSend =
                new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        return formatDateToSend.format(date);
    }

    public static String formattedDate(String time) {
        Date date;
        try {
           date = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault()).parse(time);
        } catch (ParseException e) {
            Timber.e(e);
            return EMPTY;
        }
        return  new SimpleDateFormat("dd MMMM yyyy 'at' hh:mm a", Locale.getDefault()).format(date);
    }

    public static String formatEndDate(String time){
        Date date;
        try {
            date = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault()).parse(time);
        } catch (ParseException e) {
            Timber.e(e);
            return EMPTY;
        }
        return  new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date);
    }
}
