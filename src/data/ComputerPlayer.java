package data;

import io.github.nejc92.mcts.Mcts;

import java.util.ArrayList;
import java.util.Objects;

public class ComputerPlayer extends Player {

    private static final double EXPLORATION_PARAMETER = 0.4;

    private int numberOfIterations;// = 250;//500;
    private boolean black;
    private Mcts<State, Action, Player> mcts;

    public ComputerPlayer(boolean isBlack, int size) {
        black = isBlack;
        if (size > 16) {
            numberOfIterations = 100;
        } else if (size > 12){
            numberOfIterations = 150;
        } else {
            numberOfIterations = 200;
        }
        initMCTS();
    }

    public boolean isBlack(){
        return black;
    }

    @Override
    public Action chooseAction(State state) {
        Game newGame = new Game(Objects.requireNonNull(state.game.get()), new RandomPlayer(false), new RandomPlayer(true));
        State newState = new State(state, newGame);
        long start = System.currentTimeMillis();
        Action resultingAction;
        try {
            resultingAction = mcts.uctSearchWithExploration(newState, EXPLORATION_PARAMETER);
        } catch (Exception e) {
            resultingAction = new AlmostRandomPlayer(black).chooseAction(state);
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        if (3000 < time) {
            numberOfIterations *= .8;
            initMCTS();
        } else if (time < 500){
            numberOfIterations *= 1.1;
            initMCTS();
            mcts = Mcts.initializeIterations(numberOfIterations);
        }
        newState.discardGame();
        return resultingAction;
        //return new AlmostRandomPlayer(black).chooseAction(state);
    }

    private void initMCTS() {
        mcts = Mcts.initializeIterations(numberOfIterations);
    }

}
