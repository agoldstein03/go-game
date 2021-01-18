package data;

import data.exceptions.*;

public class PlaceStoneActionWithStates extends ActionWithStates {

    public final PlaceStoneAction action;

    public PlaceStoneActionWithStates(State stateBefore, PlaceStoneAction action) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
        super(stateBefore, action);
        this.action = action;
    }

}
