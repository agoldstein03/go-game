package data;

import data.exceptions.*;

public abstract class Action {

    public abstract State stateAfterAction(State stateBefore) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException;
    public abstract String toString();

    public abstract boolean equals(Object obj);
    public abstract int hashCode();

}
