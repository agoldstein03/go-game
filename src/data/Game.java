package data;

import java.util.ArrayList;

public class Game {

    public final PlayerType player1;
    public final PlayerType player2;
    public final int size;
    public final int komi;
    public final int handicap;
    public final ArrayList<State> states = new ArrayList<State>();

    public Game(PlayerType player1, PlayerType player2) {
        this(player1, player2, 19);
    }

    public Game(PlayerType player1, PlayerType player2, int size) {
        this(player1, player2, size, 7, 0);
    }

    public Game(PlayerType player1, PlayerType player2, int size, int komi, int handicap) {
        this.player1 = player1;
        this.player2 = player2;
        this.size = size;
        this.komi = komi;
        this.handicap = handicap;
    }

}
