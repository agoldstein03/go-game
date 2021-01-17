package data;

import data.exceptions.PlacementOutOfBoundsException;

import java.util.ArrayList;
import java.util.HashSet;

public class Group extends ArrayList<Position> {

    public final Stone stone;
    public final State state;

    // TODO: Optimize initial HashMap size
    public final HashSet<Position> liberties = new HashSet<Position>();


    public Group(Position pos, State state) {
        this(pos, state, new HashSet<Position>());
    }
    public Group(Position pos, State state, HashSet<Position> processedPositions) {
        this.stone = pos.stone;
        this.state = state;
        recursivelyExpand(pos, state, processedPositions);
    }

    private void recursivelyExpand(Position pos, State state, HashSet<Position> processedPositions) {
        Stone stone = pos.stone;
        if (PlacementOutOfBoundsException.isValid(pos.x, pos.y, state.size)) {
            if (stone == Stone.EMPTY) {
                this.liberties.add(pos);
            } else if (stone == this.stone && !processedPositions.contains(pos)) {
                processedPositions.add(pos);
                this.add(pos);
                int x = pos.x;
                int y = pos.y;
                recursivelyExpand(state.getPosition(x - 1, y), state, processedPositions);
                recursivelyExpand(state.getPosition(x + 1, y), state, processedPositions);
                recursivelyExpand(state.getPosition(x, y - 1), state, processedPositions);
                recursivelyExpand(state.getPosition(x, y + 1), state, processedPositions);
            }
        }
    }

    public int countLiberties() {
        return liberties.size();
    }

    public boolean isAlive() {
        // TODO: Replace with something more accurate, but this is probably the best we will get (maybe we could calculate eyes)
        return countLiberties() > 1;
    }

}
