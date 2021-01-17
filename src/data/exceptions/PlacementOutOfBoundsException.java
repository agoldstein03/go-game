package data.exceptions;

public class PlacementOutOfBoundsException extends IllegalArgumentException {

    public PlacementOutOfBoundsException(String message) {
        super(message);
    }

    public static boolean isValid(int x, int y, int size) {
        return isValid(x, y) && x < size && y < size;
    }

    public static boolean isValid(int x, int y) {
        return x >= 0 && y >= 0;
    }

    public static void assertValid(int x, int y, int size) throws PlacementOutOfBoundsException {
        assertValid(x, y);
        if (x >= size) {
            throw new PlacementOutOfBoundsException("x (" + x + ") is greater/equal to the size (" + size + ")");
        }
        if (y >= size) {
            throw new PlacementOutOfBoundsException("y (" + y + ") is greater/equal to the size (" + size + ")");
        }
    }

    public static void assertValid(int x, int y) throws PlacementOutOfBoundsException {
        if (x < 0) {
            throw new PlacementOutOfBoundsException("x (" + x + ") is less than 0");
        }
        if (y < 0) {
            throw new PlacementOutOfBoundsException("y (" + y + ") is less than 0");
        }
    }

}
