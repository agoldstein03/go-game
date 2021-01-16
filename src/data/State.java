package data;

import data.exceptions.PlacementOutOfBoundsException;

import java.util.ArrayList;

public class State {

    public final Game game;
    public final int size;
    private final Position[][] board;
    public final Player currentPlayer;
    public final int whiteCaptures;
    public final int blackCaptures;
    public final boolean whitePass;
    public final boolean blackPass;

    public State(Game game) {
        this(game, game.whitePlayer, 0, 0, false, false);
    }

    public State(Game game, Player currentPlayer, int whiteCaptures, int blackCaptures, boolean whitePass, boolean blackPass) {
        this(game, currentPlayer, whiteCaptures, blackCaptures, whitePass, blackPass, true);
    }

    private State(Game game, Player currentPlayer, int whiteCaptures, int blackCaptures, boolean whitePass, boolean blackPass, boolean initToNone) {
        this.game = game;
        this.size = game.size;
        this.board = new Position[size][size];
        this.currentPlayer = currentPlayer;
        this.whiteCaptures = whiteCaptures;
        this.blackCaptures = blackCaptures;
        this.whitePass = whitePass;
        this.blackPass = blackPass;
        if (initToNone) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    board[x][y] = new Position(x, y, Stone.NONE);
                }
            }
        }
    }

    public State(State oldBoard) {
        this(oldBoard, oldBoard.currentPlayer, oldBoard.whiteCaptures, oldBoard.blackCaptures, oldBoard.whitePass, oldBoard.blackPass);
    }

    public State(State oldBoard, Player currentPlayer, int whiteCaptures, int blackCaptures, boolean whitePass, boolean blackPass) {
        this(oldBoard, currentPlayer, whiteCaptures, blackCaptures, whitePass, blackPass, false);
    }

    public State(State oldBoard, Player currentPlayer, int whiteCaptures, int blackCaptures, boolean whitePass, boolean blackPass, boolean initToNone) {
        this(oldBoard.game, currentPlayer, whiteCaptures, blackCaptures, whitePass, blackPass, initToNone);
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

    public State stateWithSetPosition(Position position) throws PlacementOutOfBoundsException {
        return stateWithSetPosition(position.x, position.y, position.stone);
    }

    public State stateWithSetPosition(Position position, boolean advanceTurn) throws PlacementOutOfBoundsException {
        return stateWithSetPosition(position.x, position.y, position.stone, advanceTurn);
    }

    public State stateWithSetPosition(int x, int y, Stone stone) throws PlacementOutOfBoundsException {
        return stateWithSetPosition(x, y, stone, true);
    }

    public State stateWithSetPosition(int x, int y, Stone stone, boolean advanceTurn) throws PlacementOutOfBoundsException {
        PlacementOutOfBoundsException.assertValid(x, y, size);
        State newBoard = new State(this, advanceTurn ? currentPlayer : (game.whitePlayer == currentPlayer ? game.blackPlayer : game.whitePlayer),
                whiteCaptures, blackCaptures, whitePass, blackPass);
        newBoard.setPosition(x, y, stone);
        return newBoard;
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
