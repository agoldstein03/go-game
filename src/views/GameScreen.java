package views;

import com.sun.rowset.internal.Row;
import data.*;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
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
import javafx.stage.Stage;

public class GameScreen extends BorderPane {

    private Image blackStone = new Image("file:..\\..\\Graphics\\BlackStone.png");
    private Image whiteStone = new Image("file:..\\..\\Graphics\\WhiteStone.png");

    private Canvas board;
    private MainWindow mainWindow;
    private State state;
    public static Game game;
    private int size;
    private int spacing;
    private int offset;
    private int thickness;
    private int blackCap;
    private int whiteCap;
    private GridPane bottom;

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
            offset = 73;
            spacing = 54;
            thickness = 6;
        }
        else { //9x9
            offset = 42;
            spacing = 32;
            thickness = 8;
        }
        offset = offset+spacing/2+thickness*2;

        blackCap = state.blackCaptures;
        whiteCap = state.whiteCaptures;
        this.setBackground(TitleScreen.WOODBACKGROUND);
        bottom = new GridPane();

        header = new Label(state.currentPlayer.isBlack()? "BLACK turn":"WHITE turn");
        setTop(header);

        board = new Canvas(750, 750);
        drawBoard();
        setCenter(board);
        board.setOnMouseClicked(event -> {
            double adjustedX = event.getX()+spacing/2;
            double adjustedY = event.getY()+spacing/2;
            int x = (int) ((adjustedX-offset)/spacing);
            int y = (int) ((adjustedY-offset)/spacing);
            if(state.currentPlayer instanceof HumanPlayer){
                state = state.stateAfterAction(((HumanPlayer) state.currentPlayer).chooseAction(x>=0 ? x:0, y>=0 ? y:0));
                refreshBoard();
            }
        });

        setupFooter();
        setBottom(bottom);
    }

    private void drawBoard(){
        GraphicsContext gc = board.getGraphicsContext2D();
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
        bottom.add(blackCapLabel, 0, 0, 3, 1);

        whiteCapLabel = new Label("White captured: " + whiteCap);
        bottom.add(whiteCapLabel, 0, 1, 3, 1);

        Button resign = new Button("Resign");
        bottom.add(resign, 0, 2);

        Button pass = new Button("Pass");
        bottom.add(pass, 2,2);

        Button computeScore = new Button("Compute score");
        computeScore.setOnMouseClicked(event -> {

        });
        bottom.add(computeScore, 2, 3);

        bottom.getColumnConstraints().addAll(new ColumnConstraints(300), new ColumnConstraints(200), new ColumnConstraints(300));
        bottom.getRowConstraints().addAll(new RowConstraints(33), new RowConstraints(33), new RowConstraints(33));
        for(int i=0; i<3; i++){
            bottom.getColumnConstraints().get(i).setHalignment(HPos.CENTER);
            bottom.getRowConstraints().get(i).setValignment(VPos.CENTER);
        }
    }

    public static Game getGame() {
        return game;
    }
}