package data;

public abstract class Player {

    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract Action chooseAction();

}
