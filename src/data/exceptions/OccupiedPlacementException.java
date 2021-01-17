package data.exceptions;

import data.Group;
import data.Position;
import data.State;
import data.Stone;

import java.util.HashSet;

public class OccupiedPlacementException extends IllegalArgumentException {

    public OccupiedPlacementException(Position pos) {
        super("There is already a stone of "+pos.toString());
    }

    public static void assertValid(Position pos, State state) throws OccupiedPlacementException {
        if (state.getPosition(pos.x, pos.y).stone != Stone.EMPTY) {
            throw new OccupiedPlacementException(pos);
        }
    }

}
