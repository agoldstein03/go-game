package data;

import data.exceptions.*;

public interface Action {

    public abstract State stateAfterAction(State stateBefore) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException;
    public abstract String toString();

}
