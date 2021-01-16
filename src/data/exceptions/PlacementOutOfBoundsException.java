package data.exceptions;

public class PlacementOutOfBoundsException extends IllegalArgumentException {

    public PlacementOutOfBoundsException(String message) {
        super(message);
    }

    public static void assertValid(int x, int y, int size) {
        assertValid(x, y);
        if (x >= size) {
            throw new PlacementOutOfBoundsException("x (" + x + ") is greater/equal to the size (" + size + ")");
        }
        if (y >= size) {
            throw new PlacementOutOfBoundsException("y (" + y + ") is greater/equal to the size (" + size + ")");
        }
    }

    public static void assertValid(int x, int y) {
        if (x < 0) {
            throw new PlacementOutOfBoundsException("x (" + x + ") is less than 0");
        }
        if (y < 0) {
            throw new PlacementOutOfBoundsException("y (" + y + ") is less than 0");
        }
    }

}
