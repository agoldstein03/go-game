package data;

import data.exceptions.*;

public class ActionWithStates {

    public final State stateBefore;
    public final Action action;
    public final State stateAfter;

    public ActionWithStates(State stateBefore, Action action) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
        this.stateBefore = stateBefore;
        this.action = action;
        this.stateAfter = action.stateAfterAction(stateBefore);
    }

}
