package uk.co.transferx.app.errors;

/**
 * Created by sergey on 22/03/2018.
 */

public class ErrorPinException extends Exception {

    @Override
    public String getMessage() {
        return "Error pin";
    }
}
