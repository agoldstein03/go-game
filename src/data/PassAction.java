package data;

import data.exceptions.*;

public class PassAction implements Action {

    @Override
    public State stateAfterAction(State stateBefore) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
        return new State(stateBefore, true, stateBefore.whiteCaptures, stateBefore.blackCaptures,
                stateBefore.whitePass || !stateBefore.currentPlayer.isBlack(), stateBefore.blackPass || stateBefore.currentPlayer.isBlack());
    }

    @Override
    public String toString() {
        return "Pass";
    }

}
