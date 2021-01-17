package data;

import data.exceptions.KoException;
import data.exceptions.PlacementOutOfBoundsException;
import data.exceptions.PlacingEmptyException;
import data.exceptions.SelfCaptureException;

public class ActionWithStates {

    public final State stateBefore;
    public final Action action;
    public final State stateAfter;

    public ActionWithStates(State stateBefore, Action action) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException {
        this.stateBefore = stateBefore;
        this.action = action;
        this.stateAfter = action.stateAfterAction(stateBefore);
    }

}
