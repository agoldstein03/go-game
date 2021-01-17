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
        ArrayList<Action> validActions = state.validActions();
        int size = validActions.size();
        return validActions.get(rand.nextInt(size));
    }

    @Override
    public boolean isBlack() {
        return black;
    }

}
