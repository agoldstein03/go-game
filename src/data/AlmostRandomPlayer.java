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
        ArrayList<PlaceStoneActionWithStates> validActions = currentState.validPlacementActionsWithStates();
        PlaceStoneAction action;
        boolean validAction;

        do {
            int index = selectRandomIndex(validActions);
            PlaceStoneActionWithStates randomActionWithStates = validActions.get(index);
            action = randomActionWithStates.action;
            State stateAfter = randomActionWithStates.stateAfter;
            validAction = new Group(action.position, stateAfter).isAlive();
            if (!validAction) {
                validActions.remove(index);
            }
        } while (!(validAction || validActions.isEmpty()));

        return validActions.isEmpty() ? new PassAction() : action;
    }

    private int selectRandomIndex(ArrayList<?> list) {
        return rand.nextInt(list.size());
    }

}
