package data;

import data.exceptions.*;

public class ActionWithStates<ActionT extends Action> {

    public final State stateBefore;
    public final ActionT action;
    public final State stateAfter;

    public ActionWithStates(State stateBefore, ActionT action) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
        this.stateBefore = stateBefore;
        this.action = action;
        this.stateAfter = action.stateAfterAction(stateBefore);
    }

}
