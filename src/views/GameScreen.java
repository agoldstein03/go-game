package views;

import com.sun.rowset.internal.Row;
import data.*;
import data.exceptions.KoException;
import data.exceptions.PlacementOutOfBoundsException;
import data.exceptions.PlacingEmptyException;
import data.exceptions.SelfCaptureException;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GameScreen extends GridPane {

    private Image blackStone = new Image("file:..\\..\\Graphics\\BlackStone.png");
    private Image whiteStone = new Image("file:..\\..\\Graphics\\WhiteStone.png");

    private Canvas board;
    private MainWindow mainWindow;
    private State state;
    public Game game;
    private int size;
    private int spacing;
    private int offset;
    private int thickness;
    private int blackCap;
    private int whiteCap;
    private GridPane bottom;
    private GraphicsContext gc;

    private Label header;
    private Label blackCapLabel;
    private Label whiteCapLabel;

    public GameScreen(MainWindow parent, Player whitePlayer, Player blackPlayer, int size, double komi, int handicap){
        mainWindow = parent;
        game = new Game(whitePlayer, blackPlayer, size, komi, handicap);
        state= new State(game);
        this.size = size;
        if(size == 19){
            offset = 42;
            spacing = 34;
            thickness = 4;
        }
        else if(size == 13){
            offset = 9;
            spacing = 54;
            thickness = 6;
        }
        else { //9x9
            offset = -5;
            spacing = 80;
            thickness = 8;
        }
        offset = offset+spacing/2+thickness*2;

        blackCap = state.blackCaptures;
        whiteCap = state.whiteCaptures;
        this.setBackground(TitleScreen.WOODBACKGROUND);

        setRowColumnSizing();

        header = new Label(state.currentPlayer.isBlack()? "BLACK turn":"WHITE turn");
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(new Font(45));
        add(header, 0, 0, 3, 1);

        board = new Canvas(750, 750);
        drawBoard();
        add(board, 0, 1, 3, 1);
        board.setOnMouseClicked(event -> {
            refreshBoard();
            double adjustedX = event.getX()+spacing/2;
            double adjustedY = event.getY()+spacing/2;
            int x = (int) ((adjustedX-offset)/spacing);
            int y = (int) ((adjustedY-offset)/spacing);
            boolean validMove = false;
            State newState = state;
            if(state.currentPlayer instanceof HumanPlayer){
                try{
                    newState = state.stateAfterAction(((HumanPlayer) state.currentPlayer).chooseAction(x>=0 ? x:0, y>=0 ? y:0));
                    validMove = true;
                } catch (Exception e) {
                    indicateInvalidMove(board.getGraphicsContext2D(), e);
                }
            }
            if(validMove){
                if(state.getPosition(x, y).stone==Stone.EMPTY){
                    state = newState;
                    game.states.add(state);
                    refreshBoard();
                }
            }
            computerTurn();
        });

        setupFooter();

        if(handicap>0){
            for(int i=0; i<handicap; i++){
                if(size==19)
                        state = state.stateWithSetPosition(Game.handicap19Positions[i], false);
                refreshBoard();
            }
            if(state.currentPlayer instanceof ComputerPlayer){
                computerTurn();
                refreshBoard();
            }
        }
    }

    private void indicateInvalidMove(GraphicsContext gc, Exception e){
        gc.setFill(Color.RED);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
        gc.setFont(new Font(20));
        gc.fillText("Invalid move: " + e.getMessage(), board.getWidth()/2, 0);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> refreshBoard(), 2500, TimeUnit.MILLISECONDS);

    }

    private void drawBoard(){
        gc = board.getGraphicsContext2D();
        gc.clearRect(0,0,board.getWidth(), board.getHeight());
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);

        for(int i=0; i<size; i++){
            gc.fillRect(offset, i*spacing+offset, (size-1)*spacing+thickness,thickness);
            gc.fillRect(i*spacing+offset, offset, thickness,(size-1)*spacing+thickness);
        }
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(state.getPosition(i, j).stone.equals(Stone.BLACK))
                    drawPiece(gc, false, i*spacing+offset-(spacing/2)+3, j*spacing+offset-(spacing/2)+3);
                else if(state.getPosition(i, j).stone.equals(Stone.WHITE))
                    drawPiece(gc, true, i*spacing+offset-(spacing/2)+3, j*spacing+offset-(spacing/2)+3);
            }
        }
    }

    private void drawPiece(GraphicsContext gc, boolean white, double x, double y) {
        gc.setFill(Color.BLACK);
        gc.fillArc(x, y, spacing-6, spacing-6, 0, 360, ArcType.CHORD);
        if(white){
            gc.setFill(Color.WHITE);
            gc.fillArc(x+2, y+2, spacing-10, spacing-10, 0, 360, ArcType.CHORD);
        }
    }

    private void refreshBoard() {
        header.setText(state.currentPlayer.isBlack()? "BLACK turn":"WHITE turn");
        blackCap = state.blackCaptures;
        whiteCap = state.whiteCaptures;

        whiteCapLabel.setText("White captured: " + whiteCap);
        blackCapLabel.setText("Black captured: " + blackCap);

        drawBoard();
    }



    private void setupFooter(){
        blackCapLabel = new Label("Black captured: " + blackCap);
        blackCapLabel.setFont(new Font(30));
        add(blackCapLabel, 0, 2, 2, 1);

        whiteCapLabel = new Label("White captured: " + whiteCap);
        whiteCapLabel.setFont(new Font(30));
        add(whiteCapLabel, 1, 2, 2, 1);

        Button resign = new Button("Resign");
        resign.setOnMouseClicked(event -> {
            mainWindow.changeScreen(new EndScreen(mainWindow, state.currentPlayer.isBlack() ? Stone.BLACK: Stone.WHITE, game, this));
        });
        resign.setFont(new Font(20));
        add(resign, 0, 3);

        Button pass = new Button("Pass");
        pass.setFont(new Font(20));
        pass.setOnMouseClicked(event -> {
            if(state.currentPlayer instanceof HumanPlayer){
                try{
                    state = ((HumanPlayer) state.currentPlayer).pass().stateAfterAction(state);
                    game.states.add(state);
                    refreshBoard();
                } catch (Exception e) {
                    indicateInvalidMove(board.getGraphicsContext2D(), e);
                }
            }
            if(game.isGameOver())
                mainWindow.changeScreen(new EndScreen(mainWindow, state, game, this));
            else
                computerTurn();
        });
        add(pass, 2,3);

        /*Button computeScore = new Button("Compute score");
        computeScore.setOnMouseClicked(event -> {
            State.Scoring score = state.calculateScore();
            System.out.println("White territory " + score.whiteTerritory);
            System.out.println("Black territory " + score.blackTerritory);
            System.out.println("Neutral territory " + score.neutralTerritory);
            System.out.println("White score " + score.whiteScore);
            System.out.println("Black score " + score.blackScore);
        });
        add(computeScore, 1, 3);*/
    }

    private void setRowColumnSizing() {
        getRowConstraints().addAll(new RowConstraints(50), new RowConstraints(750), new RowConstraints(50), new RowConstraints(50));
        for(int i=0; i<4; i++){
            getRowConstraints().get(i).setValignment(VPos.CENTER);
        }
        getRowConstraints().get(2).setValignment(VPos.TOP);

        getColumnConstraints().addAll(new ColumnConstraints(300), new ColumnConstraints(200), new ColumnConstraints(300));
        for(int i=0; i<3; i++){
            getColumnConstraints().get(i).setHalignment(HPos.CENTER);
        }
    }

    private void computerTurn(){
        if(state.currentPlayer instanceof ComputerPlayer){
            state = state.stateAfterAction(state.currentPlayer.chooseAction(state));
            game.states.add(state);
            refreshBoard();
            if(game.isGameOver())
                mainWindow.changeScreen(new EndScreen(mainWindow, state, game, this));
        }
    }

    public void clean(){
        ObservableList<Node> children = getChildren();
        for(int i=children.size()-1; i>=0; i--){
            if(!(children.get(i) instanceof Canvas)){
                children.remove(i);
            }
        }
    }

    public void clean(State.Scoring scoring){
        clean();
        ScoringBoardCell[][] finalBoard = scoring.scoringBoard;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                ScoringBoardCell cell = finalBoard[i][j];
                if(cell==ScoringBoardCell.BLACK_STONE)
                    drawPiece(gc, false, i*spacing+offset-(spacing/2)+3, j*spacing+offset-(spacing/2)+3);
                else if(cell==ScoringBoardCell.WHITE_STONE)
                    drawPiece(gc, true, i*spacing+offset-(spacing/2)+3, j*spacing+offset-(spacing/2)+3);
                else {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i*spacing+offset-(spacing/2)+3, j*spacing+offset-(spacing/2)+3, spacing-6, spacing-6);
                    if(cell!=ScoringBoardCell.BLACK_TERRITORY){
                        gc.setFill(cell==ScoringBoardCell.WHITE_TERRITORY ? Color.WHITE:Color.GRAY);
                        gc.fillRect(i*spacing+offset-(spacing/2)+5, j*spacing+offset-(spacing/2)+5, spacing-10, spacing-10);
                    }
                }
            }
        }
    }
}
