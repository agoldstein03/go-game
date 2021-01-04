package data;

import java.util.ArrayList;

public class State {

    public final int size;
    private final Position[][] board;
    public final boolean humanIsWhite;
    public final int blackCaptures;
    public final int whiteCaptures;
    public final boolean blackPass;
    public final boolean whitePass;

    public final Stone humanStone;
    public final Stone computerStone;


    public State() {
        this(19, true);
    }

    public State(int size, boolean humanIsWhite) {
        this(size, humanIsWhite, 0, 0, false, false);
    }

    public State(int size, boolean humanIsWhite, int blackCaptures, int whiteCaptures, boolean blackPass, boolean whitePass) {
        this(size, humanIsWhite, blackCaptures, whiteCaptures, blackPass, whitePass, true);
    }

    private State(int size, boolean humanIsWhite, int blackCaptures, int whiteCaptures, boolean blackPass, boolean whitePass, boolean initToNone) {
        this.size = size;
        this.board = new Position[size][size];
        this.blackCaptures = blackCaptures;
        this.whiteCaptures = whiteCaptures;
        this.blackPass = blackPass;
        this.whitePass = whitePass;
        this.humanIsWhite = humanIsWhite;
        this.humanStone = humanIsWhite ? Stone.WHITE : Stone.BLACK;
        this.computerStone = humanIsWhite ? Stone.BLACK : Stone.WHITE;
        if (initToNone) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    board[x][y] = new Position(x, y, Stone.NONE);
                }
            }
        }
    }

    public State(State oldBoard) {
        this(oldBoard.size, oldBoard.humanIsWhite, oldBoard.blackCaptures, oldBoard.whiteCaptures, oldBoard.blackPass, oldBoard.whitePass, false);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                board[x][y] = oldBoard.getPosition(x, y);
            }
        }
    }

    public Position getPosition(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return board[x][y];
        } else {
            // TODO: Optimize
            return new Position(x, y, Stone.NONE);
        }
    }

    public State stateWithSetPosition(Position position) {
        return stateWithSetPosition(position.x, position.y, position.stone);
    }

    public State stateWithSetPosition(int x, int y, Stone stone) {
        if (x < 0) {
            throw new IllegalArgumentException("x ("+x+") is less than 0");
        } else if (x >= size) {
            throw new IllegalArgumentException("x ("+x+") is greater/equal to the size ("+size+")");
        }else if (y < 0) {
            throw new IllegalArgumentException("y ("+y+") is less than 0");
        } else if (y >= size) {
            throw new IllegalArgumentException("y ("+y+") is greater/equal to the size ("+size+")");
        } else {
            State newBoard = new State(this);
            newBoard.setPosition(x, y, stone);
            return newBoard;
        }
    }

    private void setPosition(int x, int y, Stone stone) {
        board[x][y] = new Position(x, y, stone);
    }

    public int countLiberties(Position pos) {
        return countLiberties(pos.x, pos.y);
    }

    public int countLiberties(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (!(dx == 0 && dx == 0) && board[x][y].stone == Stone.NONE) {
                    count++;
                }
            }
        }
        return count;
    }

    public ArrayList<Position> getLiberties(Position pos) {
        return getLiberties(pos.x, pos.y);
    }

    private ArrayList<Position> getLiberties(int x, int y) {
        ArrayList<Position> liberties = new ArrayList<Position>(8);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (!(dx == 0 && dy == 0)) {
                    Position position = board[x][y];
                    if (position.stone == Stone.NONE) {
                        liberties.add(position);
                    }
                }
            }
        }
        return liberties;
    }

    public State stateAfterAction(Action action) {
        return action.stateAfterAction(this);
    }

}
