package data.exceptions;

import data.Stone;

public class PlacingEmptyException extends IllegalArgumentException {

    public PlacingEmptyException() {
        super("Cannot place a \"Empty\" stone");
    }

    public static void assertValid(Stone stone) throws PlacingEmptyException {
        if (stone == Stone.EMPTY) {
            throw new PlacingEmptyException();
        }
    }


}
