package data;

import data.exceptions.*;

public class PlaceStoneAction implements Action {

    public final Position position;

    public PlaceStoneAction(Position position) {
        PlacementOutOfBoundsException.assertValid(position.x, position.y);
        if (position.stone == Stone.EMPTY) {
            throw new PlacingEmptyException();
        }
        this.position = position;
    }

    @Override
    public State stateAfterAction(State stateBefore) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
        return stateBefore.stateWithSetPosition(position, true);
    }

    @Override
    public String toString() {
        return "Placing: "+position.toString();
    }

}
