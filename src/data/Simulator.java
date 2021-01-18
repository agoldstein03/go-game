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

    public void display(Game game, State.Scoring score){
        System.out.println("Turns: " + game.states.size());
        System.out.printf("%17s%3s%17s%n", "Black territory: ", "", "White territory: ");
        System.out.printf("%17d%3s%17d%n", score.blackTerritory, "   ", score.whiteTerritory);
        System.out.printf("%17s%3s%17s%n", "Black captures: ", "", "White captures: ");
        System.out.printf("%17d%3s%17d%n", score.blackCaptures, "   ", score.whiteCaptures);
        State lastState = game.states.get(game.states.size()-1);




        for(int i=0; i<game.size; i++){
            for(int j=0; j<game.size; j++){
                if(lastState.getPosition(i, j).stone==Stone.BLACK)
                    System.out.print("@ ");
                else
                    System.out.print(lastState.getPosition(i, j).stone==Stone.WHITE ? "| ":"- ");
            }
            System.out.print("\n");
        }
    }

    public Game simulate() {
        return simulate(DEFAULT_MAX_TURNS);
    }

    public Game simulate(int maxTurns) {
        Game game = new Game(this.game);
        State currentState = new State(game);
        game.states.add(currentState.currentPlayer.chooseAction(currentState).stateAfterAction(currentState));
        for (int i = 1; i < maxTurns-1 && !game.isGameOver(); i++) {
            currentState = game.currentState();
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

    public static void main(String[] args){
        int size = 19;
        Player blackPlayer = new AlmostRandomPlayer(true);
        Player whitePlayer = new AlmostRandomPlayer(false);
        int maxTurns = DEFAULT_MAX_TURNS;
        Simulator sim = new Simulator(new Game(blackPlayer, whitePlayer, size));
        Game simGame = sim.simulate(maxTurns);
        sim.display(simGame, simGame.currentState().new Scoring());
    }
}
