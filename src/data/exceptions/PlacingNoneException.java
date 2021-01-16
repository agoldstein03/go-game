package data.exceptions;

public class PlacingNoneException extends IllegalArgumentException {

    public PlacingNoneException() {
        super("Cannot place a \"None\" stone");
    }
}
