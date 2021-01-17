package data;

import java.util.ArrayList;

public class Game {

    public final Player whitePlayer;
    public final Player blackPlayer;
    public final int size;
    public final double komi;
    public final int handicap;
    public final ArrayList<State> states = new ArrayList<State>();

    public Game(Player whitePlayer, Player blackPlayer) {
        this(whitePlayer, blackPlayer, 19);
    }

    public Game(Player whitePlayer, Player blackPlayer, int size) {
        this(whitePlayer, blackPlayer, size, 7, 0);
    }

    public Game(Player whitePlayer, Player blackPlayer, int size, double komi, int handicap) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.size = size;
        this.komi = komi;
        this.handicap = handicap;
    }

}
