package data;

import io.github.nejc92.mcts.MctsDomainAgent;

import java.util.Objects;

public abstract class Player implements MctsDomainAgent<State> {
    /*
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
    */
    public abstract Action chooseAction(State state);

    public abstract boolean isBlack();


    @Override
    public State getTerminalStateByPerformingSimulationFromState(State state) {
        return new Simulator(Objects.requireNonNull(state.game.get())).simulate().currentState();
    }

    @Override
    public double getRewardFromTerminalState(State terminalState) {
        return terminalState.new Scoring().didWhiteWin ^ isBlack() ? 1 : 0;
    }

}
