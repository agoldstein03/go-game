package data;

import data.exceptions.KoException;
import data.exceptions.PlacementOutOfBoundsException;
import data.exceptions.PlacingEmptyException;
import data.exceptions.SelfCaptureException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

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

    private State(Game game, Player currentPlayer, int whiteCaptures, int blackCaptures, boolean whitePass, boolean blackPass, boolean initToEmpty) {
        this.game = game;
        this.size = game.size;
        this.board = new Position[size][size];
        this.currentPlayer = currentPlayer;
        this.whiteCaptures = whiteCaptures;
        this.blackCaptures = blackCaptures;
        this.whitePass = whitePass;
        this.blackPass = blackPass;
        if (initToEmpty) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    board[x][y] = new Position(x, y, Stone.EMPTY);
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

    public State(State oldBoard, Player currentPlayer, int whiteCaptures, int blackCaptures, boolean whitePass, boolean blackPass, boolean initToEmpty) {
        this(oldBoard.game, currentPlayer, whiteCaptures, blackCaptures, whitePass, blackPass, initToEmpty);
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
            // TODO: Optimize by caching out-of-bounds-by-one positions
            return new Position(x, y, Stone.EMPTY);
        }
    }

    public State stateWithSetPosition(Position position) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException {
        return stateWithSetPosition(position, true);
    }

    public State stateWithSetPosition(Position pos, boolean advanceTurn) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException {
        int x = pos.x;
        int y = pos.y;
        Stone stone = pos.stone;

        PlacingEmptyException.assertValid(stone);
        PlacementOutOfBoundsException.assertValid(x, y, size);
        State newState = new State(this, advanceTurn ? currentPlayer : (game.whitePlayer == currentPlayer ? game.blackPlayer : game.whitePlayer),
                whiteCaptures, blackCaptures, whitePass, blackPass);
        newState.setPosition(x, y, stone);
        KoException.assertValid(newState);
        // It looks like separate processedPositions are not necessary, nor actually keeping track of the groups
        /*
        HashSet<Position> whiteProcessedPositions = new HashSet<Position>();
        HashSet<Position> blackProcessedPositions = new HashSet<Position>();
        ArrayList<Group> whiteGroups = new ArrayList<Group>();
        ArrayList<Group> blackGroups = new ArrayList<Group>();
         */
        HashSet<Position> processedPositions = new HashSet<Position>();
        SelfCaptureException.assertValid(pos, newState, processedPositions);//stone == Stone.WHITE ? whiteProcessedPositions : blackProcessedPositions);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = x + dx;
                int newY = y + dy;
                if (!(dx == 0 && dx == 0) && PlacementOutOfBoundsException.isValid(newX, newY)) {
                    Position newPos = getPosition(newX, newY);
                    //HashSet<Position> processedPositions = newPos.stone == Stone.WHITE ? whiteProcessedPositions : blackProcessedPositions;
                    if (!processedPositions.contains(newPos)) {
                        //ArrayList<Group> groups = newPos.stone == Stone.WHITE ? whiteGroups : blackGroups;
                        Group group = new Group(newPos, newState, processedPositions);
                        if (group.countLiberties() == 0) {
                            newState.removeGroup(group);
                        }
                    }
                }
            }
        }

        return newState;
    }

    private void setPosition(int x, int y, Stone stone) {
        board[x][y] = new Position(x, y, stone);
    }

    /*
    public int countLiberties(Position pos) {
        return countLiberties(pos.x, pos.y);
    }

    public int countLiberties(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = x + dx;
                int newY = y + dy;
                if (!(dx == 0 && dx == 0) && PlacementOutOfBoundsException.isValid(newX, newY) && board[newX][newY].stone == Stone.EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }

    public HashSet<Position> findLiberties(Position pos) {
        return findLiberties(pos.x, pos.y);
    }

    private HashSet<Position> findLiberties(int x, int y) {
        HashSet<Position> liberties = new HashSet<Position>(8);
        findLiberties(x, y, liberties);
        return liberties;
    }

    private void findLiberties(int x, int y, HashSet<Position> libertiesStorage) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = x + dx;
                int newY = y + dy;
                if (!(dx == 0 && dy == 0) && PlacementOutOfBoundsException.isValid(newX, newY) ) {
                    Position position = board[newX][newY];
                    if (position.stone == Stone.EMPTY) {
                        libertiesStorage.add(position);
                    }
                }
            }
        }
    }
    */
    public State stateAfterAction(Action action) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException {
        return action.stateAfterAction(this);
    }
    /*
    private class GroupsAndEmptyPositions {
        public final Groups groups = new Groups();
        public final HashSet<Position> emptyPositions = new HashSet<Position>();
    }

    private class Groups {

        public final ArrayList<Group> allGroups = new ArrayList<Group>();
        public final ArrayList<Group> whiteGroups = new ArrayList<Group>();
        public final ArrayList<Group> blackGroups = new ArrayList<Group>();

        public void add(Group group) {
            allGroups.add(group);
            if (group.stone == Stone.WHITE) {
                whiteGroups.add(group);
            } else {
                blackGroups.add(group);
            }
        }

    }*/

    private ArrayList<Group> findAllGroups() {
        //GroupsAndEmptyPositions groupsAndEmptyPositions = new GroupsAndEmptyPositions();
        ArrayList<Group> groups = new ArrayList<Group>();//groupsAndEmptyPositions.groups;
        //HashSet<Position> emptyPositions = groupsAndEmptyPositions.emptyPositions;
        // NOTE: processedPositions only contains processed non-empty (non-EMPTY) Positions
        // TODO: Is it worth it to make the initial size game.size ** 2?
        HashSet<Position> processedPositions = new HashSet<Position>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Position pos = board[x][y];
                if (pos.stone != Stone.EMPTY && !processedPositions.contains(pos)) {
                    groups.add(new Group(pos, this, processedPositions));
                }
            }
        }
        return groups;
    }

    private void removeGroup(Group group) {
        for (Position pos : group) {
            setPosition(pos.x, pos.y, Stone.EMPTY);
        }
    }

    // TODO: I don't know exactly how these 6 should relate nor which will be needed, but we'll see


    public class Scoring {

        public final int whiteTerritory;
        public final int blackTerritory;
        public final int neutralTerritory;
        public final double whiteScore;
        public final double blackScore;
        public final boolean didWhiteWin;
        public final double adjustedWhiteScore;
        public final double adjustedBlackScore;
        public final Stone winningStone;
        public final Player winningPlayer;

        public Scoring(int whiteTerritory, int blackTerritory, int neutralTerritory) {
            this.whiteTerritory = whiteTerritory;
            this.blackTerritory = blackTerritory;
            this.neutralTerritory = neutralTerritory;
            whiteScore = whiteTerritory + (neutralTerritory / 2) + whiteCaptures + game.komi;
            blackScore = blackTerritory + (neutralTerritory / 2) + blackCaptures;
            didWhiteWin = whiteScore > blackScore;
            if (didWhiteWin) {
                adjustedWhiteScore = whiteScore - blackScore;
                adjustedBlackScore = 0;
                winningStone = Stone.WHITE;
                winningPlayer = game.whitePlayer;
            } else {
                adjustedWhiteScore = 0;
                adjustedBlackScore = blackScore - whiteScore;
                winningStone = Stone.BLACK;
                winningPlayer = game.blackPlayer;
            }

        }

    }

    public Scoring calculateScore() {
        State state = new State(this);

        ArrayList<Group> groups = findAllGroups();
        for (Group group: groups) {
            if (!group.isAlive()) {
                state.removeGroup(group);
            }
        }

        int whiteArea = 0;
        int blackArea = 0;
        int neutralArea = 0;
        HashSet<Position> processedPositions = new HashSet<Position>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Position pos = board[x][y];
                if (pos.stone == Stone.EMPTY && !processedPositions.contains(pos)) {
                    TerritoryArea territoryArea = new TerritoryArea(pos, this);
                    processedPositions.addAll(territoryArea);

                    int area = territoryArea.size();
                    if (territoryArea.isWhite()) {
                        whiteArea += area;
                    } else if (territoryArea.isBlack()) {
                        blackArea += area;
                    } else {
                        neutralArea += area;
                    }
                }
            }
        }

        return new Scoring(whiteArea, blackArea, neutralArea);
    }

    /* Auto-generated equals/hashCode by IntelliJ IDEA */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return size == state.size &&
                whiteCaptures == state.whiteCaptures &&
                blackCaptures == state.blackCaptures &&
                whitePass == state.whitePass &&
                blackPass == state.blackPass &&
                game.equals(state.game) &&
                Arrays.equals(board, state.board) &&
                currentPlayer.equals(state.currentPlayer);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(game, size, currentPlayer, whiteCaptures, blackCaptures, whitePass, blackPass);
        result = 31 * result + Arrays.hashCode(board);
        return result;
    }
}
