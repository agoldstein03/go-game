package data;

public interface Action {

    public abstract State stateAfterAction(State stateBefore);
    public abstract String toString();

}
