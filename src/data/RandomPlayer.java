package data;

import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer extends Player {

    private final Random rand = new Random();
    private final boolean black;

    public RandomPlayer(boolean black) {
        this.black = black;
    }

    @Override
    public Action chooseAction(State state) {
        ArrayList<ActionWithStates> validActions = state.validActionsWithStates();
        int size = validActions.size();
        return selectRandom(validActions).action;
    }

    @Override
    public boolean isBlack() {
        return black;
    }

    private <T> T selectRandom(ArrayList<T> list) {
        return list.get(rand.nextInt(list.size()));
    }

}
