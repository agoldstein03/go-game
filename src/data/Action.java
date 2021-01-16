package data;

import data.exceptions.PlacementOutOfBoundsException;

public interface Action {

    public abstract State stateAfterAction(State stateBefore) throws PlacementOutOfBoundsException;
    public abstract String toString();

}
