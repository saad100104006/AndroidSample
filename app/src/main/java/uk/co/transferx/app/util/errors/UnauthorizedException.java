package uk.co.transferx.app.util.errors;

public class UnauthorizedException extends Exception{

    @Override
    public String getMessage() {
        return "Error token";
    }
}
