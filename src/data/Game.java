package data;

import java.util.ArrayList;

public class Game {

    public final Player whitePlayer;
    public final Player blackPlayer;
    public final Player firstTurn;
    public final int size;
    public final double komi;
    public final int handicap;
    public final ArrayList<State> states = new ArrayList<State>();
    public static final Position[] handicap19Positions = new Position[]{new Position(15, 3, Stone.BLACK), new Position(3, 15, Stone.BLACK), new Position(15, 15, Stone.BLACK),
    new Position(3, 3, Stone.BLACK), new Position(9,9, Stone.BLACK), new Position(3,  9, Stone.BLACK),
    new Position(15, 9, Stone.BLACK), new Position(9, 3, Stone.BLACK), new Position(9, 15, Stone.BLACK)};

    public Game(Player whitePlayer, Player blackPlayer) {
        this(whitePlayer, blackPlayer, 19);
    }

    public Game(Player whitePlayer, Player blackPlayer, int size) {
        this(whitePlayer, blackPlayer, size, 7, 0);
    }

    public Game(Player whitePlayer, Player blackPlayer, int size, double komi, int handicap) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        if(handicap>=2)
            firstTurn = whitePlayer;
        else
            firstTurn = blackPlayer;
        this.size = size;
        this.komi = komi;
        this.handicap = handicap;
    }

}
