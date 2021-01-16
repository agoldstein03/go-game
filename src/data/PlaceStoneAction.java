package data;

import data.exceptions.PlacementOutOfBoundsException;
import data.exceptions.PlacingNoneException;

public class PlaceStoneAction implements Action {

    public final Position position;

    public PlaceStoneAction(Position position) {
        PlacementOutOfBoundsException.assertValid(position.x, position.y);
        if (position.stone == Stone.NONE) {
            throw new PlacingNoneException();
        }
        this.position = position;
    }

    @Override
    public State stateAfterAction(State stateBefore) throws PlacementOutOfBoundsException {
        return null;
    }

    @Override
    public String toString() {
        return "Placing: "+position.toString();
    }

}
