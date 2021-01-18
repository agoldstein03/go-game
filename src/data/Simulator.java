package data;

public class Simulator {

    public static final int DEFAULT_MAX_TURNS = 1000;

    public final Game game;
    public final Player whitePlayer;
    public final Player blackPlayer;

    public Simulator(Game game) {
        this(game, new AlmostRandomPlayer(false), new AlmostRandomPlayer(true));
    }

    public Simulator(Game game, Player whitePlayer, Player blackPlayer) {
        this.game = game;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public Game simulate() {
        return simulate(DEFAULT_MAX_TURNS);
    }

    public Game simulate(int maxTurns) {
        Game game = new Game(this.game);
        for (int i = 0; i < maxTurns && !game.isGameOver(); i++) {
            State currentState = game.currentState();
            game.states.add(currentState.currentPlayer.chooseAction(currentState).stateAfterAction(currentState));
        }
        return game;
    }

    public State.Scoring simulateAndScore() {
        return simulateAndScore(DEFAULT_MAX_TURNS);
    }

    public State.Scoring simulateAndScore(int maxTurns) {
        return simulate(maxTurns).currentState().new Scoring();
    }

}
