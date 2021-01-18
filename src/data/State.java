package data;

import data.exceptions.*;

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
        this(game, game.firstTurn, 0, 0, false, false);
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

    // TODO: Need to verify that passes are reset correctly after a placement is made;

    public State(State oldBoard) {
        this(oldBoard, false);
    }

    public State(State oldBoard, boolean advanceTurn) {
        this(oldBoard, advanceTurn, oldBoard.whiteCaptures, oldBoard.blackCaptures);
    }

    public State(State oldBoard, boolean advanceTurn, int whiteCaptures, int blackCaptures) {
        this(oldBoard, advanceTurn, whiteCaptures, blackCaptures, oldBoard.whitePass, oldBoard.blackPass);
    }

    public State(State oldBoard, boolean advanceTurn, int whiteCaptures, int blackCaptures, boolean whitePass, boolean blackPass) {
        this(oldBoard, advanceTurn ? (oldBoard.game.whitePlayer == oldBoard.currentPlayer ? oldBoard.game.blackPlayer : oldBoard.game.whitePlayer) : oldBoard.currentPlayer,
                whiteCaptures, blackCaptures, whitePass, blackPass);
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

    public State stateWithSetPosition(Position position) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
        return stateWithSetPosition(position, true);
    }

    public State stateWithSetPosition(Position pos, boolean advanceTurn) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
        int x = pos.x;
        int y = pos.y;
        Stone stone = pos.stone;

        PlacingEmptyException.assertValid(stone);
        PlacementOutOfBoundsException.assertValid(x, y, size);
        OccupiedPlacementException.assertValid(pos, this);
        State newState = new State(this, advanceTurn, whiteCaptures, blackCaptures, false, false);
        newState.setPosition(x, y, stone);
        // It looks like separate processedPositions are not necessary, nor actually keeping track of the groups
        /*
        HashSet<Position> whiteProcessedPositions = new HashSet<Position>();
        HashSet<Position> blackProcessedPositions = new HashSet<Position>();
        ArrayList<Group> whiteGroups = new ArrayList<Group>();
        ArrayList<Group> blackGroups = new ArrayList<Group>();
         */
        HashSet<Position> processedPositions = new HashSet<Position>();

        int captured = 0;
        captured += newState.removeGroupIfNeeded(x - 1, y, stone, processedPositions);
        captured += newState.removeGroupIfNeeded(x + 1, y, stone, processedPositions);
        captured += newState.removeGroupIfNeeded(x, y - 1, stone, processedPositions);
        captured += newState.removeGroupIfNeeded(x, y + 1, stone, processedPositions);

        if (captured > 0) {
            boolean isBlack = currentPlayer.isBlack();
            newState = new State(newState, false, isBlack ? whiteCaptures : whiteCaptures + captured, isBlack ? blackCaptures + captured : blackCaptures);
        }

        SelfCaptureException.assertValid(pos, newState); //, processedPositions //, stone == Stone.WHITE ? whiteProcessedPositions : blackProcessedPositions);
        KoException.assertValid(newState, game);

        return newState;
    }

    private int removeGroupIfNeeded(int x, int y, Stone stone, HashSet<Position> processedPositions) {
        int captured = 0;
        if (PlacementOutOfBoundsException.isValid(x, y)) {
            Position newPos = getPosition(x, y);
            //HashSet<Position> processedPositions = newPos.stone == Stone.WHITE ? whiteProcessedPositions : blackProcessedPositions;
            if (!processedPositions.contains(newPos)) {
                //ArrayList<Group> groups = newPos.stone == Stone.WHITE ? whiteGroups : blackGroups;
                Group group = new Group(newPos, this, processedPositions);
                if (group.stone != stone && group.countLiberties() == 0) {
                    int area = group.size();
                    this.removeGroup(group);
                    captured += area;
                }
            }
        }
        return captured;
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
    public State stateAfterAction(Action action) throws PlacementOutOfBoundsException, KoException, SelfCaptureException, PlacingEmptyException, OccupiedPlacementException {
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

    public class Scoring {

        public final State finalState = State.this;
        public final State scoringState = new State(finalState); // This will have "dead" groups removed
        public final Game game = scoringState.game;

        public final ScoringBoardCell[][] scoringBoard = new ScoringBoardCell[scoringState.size][scoringState.size];

        public final int whiteStones;
        public final int blackStones;
        public final int whiteTerritory;
        public final int blackTerritory;
        public final int neutralTerritory;
        public final int whiteCaptures;
        public final int blackCaptures;

        public final double whiteScore;
        public final double blackScore;

        public final boolean didWhiteWin;
        public final double adjustedWhiteScore;
        public final double adjustedBlackScore;
        public final Stone winningStone;
        public final Player winningPlayer;


        public Scoring() {

            int whiteStones = 0;
            int blackStones = 0;
            int whiteTerritory = 0;
            int blackTerritory = 0;
            int neutralTerritory = 0;
            int whiteCaptures = scoringState.whiteCaptures;
            int blackCaptures = scoringState.blackCaptures;

            ArrayList<Group> groups = scoringState.findAllGroups();
            for (Group group: groups) {
                if (!group.isAlive()) {
                    int area = group.size();
                    if (group.stone == Stone.WHITE) {
                        blackCaptures += area;
                    } else {
                        whiteCaptures += area;
                    }
                    scoringState.removeGroup(group);
                }
            }

            HashSet<Position> processedPositions = new HashSet<Position>();
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    Position pos = scoringState.getPosition(x, y);
                    if (pos.stone == Stone.WHITE) {
                        whiteStones++;
                        scoringBoard[x][y] = ScoringBoardCell.WHITE_STONE;
                    } else if (pos.stone == Stone.BLACK) {
                        blackStones++;
                        scoringBoard[x][y] = ScoringBoardCell.BLACK_STONE;
                    } else if (!processedPositions.contains(pos)) {
                        TerritoryArea territoryArea = new TerritoryArea(pos, scoringState);
                        processedPositions.addAll(territoryArea);

                        int area = territoryArea.size();
                        ScoringBoardCell territoryType;
                        if (territoryArea.isWhite()) {
                            whiteTerritory += area;
                            territoryType = ScoringBoardCell.WHITE_TERRITORY;
                        } else if (territoryArea.isBlack()) {
                            blackTerritory += area;
                            territoryType = ScoringBoardCell.BLACK_TERRITORY;
                        } else {
                            neutralTerritory += area;
                            territoryType = ScoringBoardCell.NEUTRAL_TERRITORY;
                        }

                        for (Position position : territoryArea) {
                            scoringBoard[position.x][position.y] = territoryType;
                        }
                    }
                }
            }

            this.whiteStones = whiteStones;
            this.blackStones = blackStones;
            this.whiteTerritory = whiteTerritory;
            this.blackTerritory = blackTerritory;
            this.neutralTerritory = neutralTerritory;
            this.whiteCaptures = whiteCaptures;
            this.blackCaptures = blackCaptures;

            this.whiteScore = this.whiteTerritory + this.whiteCaptures + game.komi;
            this.blackScore = this.blackTerritory + this.blackCaptures;

            this.didWhiteWin = whiteScore > blackScore;
            if (didWhiteWin) {
                this.adjustedWhiteScore = whiteScore - blackScore;
                this.adjustedBlackScore = 0;
                this.winningStone = Stone.WHITE;
                this.winningPlayer = game.whitePlayer;
            } else {
                this.adjustedWhiteScore = 0;
                this.adjustedBlackScore = blackScore - whiteScore;
                this.winningStone = Stone.BLACK;
                this.winningPlayer = game.blackPlayer;
            }

        }

    }

    public ArrayList<Action> validActions() {
        ArrayList<Action> actions = new ArrayList<Action>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Position pos = new Position(x, y, currentPlayer.isBlack() ? Stone.BLACK : Stone.WHITE);
                try {
                    stateWithSetPosition(pos);
                    actions.add(new PlaceStoneAction(pos)); // Will not be reached if the placement is invalid
                } catch (PlacementOutOfBoundsException | KoException | SelfCaptureException | PlacingEmptyException | OccupiedPlacementException exception) {}
            }
        }
        actions.add(new PassAction());
        return actions;
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
