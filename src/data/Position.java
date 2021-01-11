package data;

import java.util.Objects;

public class Position {

    public final int x;
    public final int y;
    public final Stone stone;

    public Position(int x, int y, Stone stone) {
        this.x = x;
        this.y = y;
        this.stone = stone;
    }

    public Stone getStone(){
        return stone;
    }

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
