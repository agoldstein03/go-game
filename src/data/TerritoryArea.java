package data;

import data.exceptions.PlacementOutOfBoundsException;

import java.util.ArrayList;
import java.util.HashSet;

public class TerritoryArea extends ArrayList<Position> {

    public final State state;
    private int whiteAdjacent = 0;
    private int blackAdjacent = 0;

    public TerritoryArea(Position pos, State state) {
        this(pos, state, new HashSet<Position>());
    }

    public TerritoryArea(Position pos, State state, HashSet<Position> processedPositions) {
        this.state = state;
        recursivelyExpand(pos, state, processedPositions);
    }

    private void recursivelyExpand(Position pos, State state, HashSet<Position> processedPositions) {
        Stone stone = pos.stone;
        if (PlacementOutOfBoundsException.isValid(pos.x, pos.y, state.size) && !processedPositions.contains(pos)) {
            processedPositions.add(pos);
            switch (stone) {
                case WHITE:
                    whiteAdjacent++;
                    break;
                case BLACK:
                    blackAdjacent++;
                    break;
                case EMPTY:
                    this.add(pos);
                    int x = pos.x;
                    int y = pos.y;
                    recursivelyExpand(state.getPosition(x - 1, y), state, processedPositions);
                    recursivelyExpand(state.getPosition(x + 1, y), state, processedPositions);
                    recursivelyExpand(state.getPosition(x, y - 1), state, processedPositions);
                    recursivelyExpand(state.getPosition(x, y + 1), state, processedPositions);
                    break;
            }
        }
    }

    public int getBlackAdjacent() {
        return blackAdjacent;
    }

    public int getWhiteAdjacent() {
        return whiteAdjacent;
    }

    public boolean isWhite() {
        return whiteAdjacent > 0 && blackAdjacent == 0;
    }

    public boolean isBlack() {
        return blackAdjacent > 0 && whiteAdjacent == 0;
    }

    public boolean isNeutral() {
        return !(isWhite() || isBlack());
    }

}
