package data;

public class ActionWithStates {

    public final State stateBefore;
    public final Action action;
    public final State stateAfter;

    public ActionWithStates(State stateBefore, Action action) {
        this.stateBefore = stateBefore;
        this.action = action;
        this.stateAfter = action.stateAfterAction(stateBefore);
    }

}
