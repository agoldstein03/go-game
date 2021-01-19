package data;

import data.exceptions.*;

import java.util.Objects;

public class PassAction extends Action {

    @Override
    public State stateAfterAction(State stateBefore) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
        State stateAfter;
        if (stateBefore.currentPlayer.isBlack()) {
            stateAfter = new State(stateBefore, true, stateBefore.whiteCaptures + 1, stateBefore.blackCaptures,
                    stateBefore.whitePass, true);
        } else {
            stateAfter = new State(stateBefore, true, stateBefore.whiteCaptures, stateBefore.blackCaptures + 1,
                    true, stateBefore.blackPass);
        }
        return stateAfter;
    }

    @Override
    public String toString() {
        return "Pass";
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override
    public int hashCode() {
        return 2;
    }

}
