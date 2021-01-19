package data;

import java.util.ArrayList;
import java.util.Random;

public class AlmostRandomPlayer extends RandomPlayer {

    private final Random rand = new Random();

    public AlmostRandomPlayer(boolean black) {
        super(black);
    }

    @Override
    public Action chooseAction(State currentState) {
        ArrayList<ActionWithStates<PlaceStoneAction>> validActions = currentState.validPlacementActionsWithStates();
        PlaceStoneAction action = null;
        boolean validAction = false;

        while (!(validAction || validActions.isEmpty())) {
            int index = selectRandomIndex(validActions);
            ActionWithStates<PlaceStoneAction> randomActionWithStates = validActions.get(index);
            action = randomActionWithStates.action;
            State stateAfter = randomActionWithStates.stateAfter;
            validAction = new Group(action.position, stateAfter).isAlive();
            if (!validAction) {
                validActions.remove(index);
            }
        }

        return action == null || validActions.isEmpty() ? new PassAction() : action;
    }

    private int selectRandomIndex(ArrayList<?> list) {
        return rand.nextInt(list.size());
    }

}
