package data;

import data.exceptions.*;

import java.util.Objects;

public class PlaceStoneAction extends Action {

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

    /* Modified from auto-generated equals/hashCode by IntelliJ IDEA */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceStoneAction that = (PlaceStoneAction) o;
        return position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

}
