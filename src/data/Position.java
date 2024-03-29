package data;

import java.util.Objects;

public class Position {

    // TODO: See if performance would be improved by caching Positions, so the constructor would be private and a public static method like Position.at(int x, int y, Stone stone) would be used instead

    public final int x;
    public final int y;
    public final Stone stone;

    public Position(int x, int y, Stone stone) {
        this.x = x;
        this.y = y;
        this.stone = stone;
    }

    public String toString() {
        return String.format("%s at (%d, %d)", stone, x, y);
    }

    /* Auto-generated equals/hashCode by IntelliJ IDEA */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y &&
                stone == position.stone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, stone);
    }

}
