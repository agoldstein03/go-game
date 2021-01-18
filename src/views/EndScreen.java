package views;

import com.sun.rowset.internal.Row;
import data.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.text.DecimalFormat;

public class EndScreen extends StackPane {

    private MainWindow mainWindow;
    private Label winTitle;
    private Game game;
    private State finalState;
    private double whiteScore;
    private double blackScore;
    public final Font scoringFont = new Font(45);
    public final int rightOffset = 50;
    private GridPane foreground = new GridPane();
    private GridPane buttons = new GridPane();
    private GameScreen background;
    public final DecimalFormat df = new DecimalFormat("##0.0");

    public EndScreen(MainWindow parent, State finalState, Game game, GameScreen background){
        mainWindow = parent;
        this.game = game;
        this.finalState = finalState;

        //setBackground(TitleScreen.WOODBACKGROUND);

        State.Scoring finalScore = finalState.new Scoring();
        blackScore = finalState.blackCaptures+finalScore.blackTerritory;
        whiteScore = finalState.whiteCaptures+finalScore.whiteTerritory+ game.komi;

        Label blackCaps = new Label("Captures: " + finalScore.blackCaptures);
        blackCaps.setPadding(new Insets(0, rightOffset, 50, 0));
        formatScore(blackCaps);
        Label whiteCaps = new Label("Captures: " + finalScore.whiteCaptures);
        whiteCaps.setPadding(new Insets(0, rightOffset, 50, 0));
        formatScore(whiteCaps);

        Label blackTerr = new Label("Territory: " + finalScore.blackTerritory);
        blackTerr.setPadding(new Insets(0, rightOffset, 150, 0));
        formatScore(blackTerr);
        Label whiteTerr = new Label("Territory: " + finalScore.whiteTerritory);
        whiteTerr.setPadding(new Insets(0, rightOffset, 150, 0));
        formatScore(whiteTerr);

        Label komi = new Label("Komi: " + df.format(game.komi));
        komi.setPadding(new Insets(50, rightOffset, 0, 0));
        formatScore(komi);

        Label totalBlack = new Label("Black total: " + df.format(blackScore));
        totalBlack.setPadding(new Insets(150, rightOffset, 0, 0));
        formatScore(totalBlack);
        Label totalWhite = new Label("White total: " + df.format(whiteScore));
        totalWhite.setPadding(new Insets(150, rightOffset, 0, 0));
        formatScore(totalWhite);

        setupButtons();

        setTitle();
        winTitle.setFont(new Font(60));
        padTitle();

        foreground.add(winTitle, 0, 0, 2, 1);
        foreground.add(blackTerr, 0,1);
        foreground.add(whiteTerr, 1, 1);
        foreground.add(blackCaps, 0, 1);
        foreground.add(whiteCaps, 1, 1);
        foreground.add(komi, 1, 1);
        foreground.add(totalBlack, 0, 1);
        foreground.add(totalWhite, 1, 1);
        foreground.add(new ImageView(new Image("file:..\\..\\Graphics\\TerritoryKey.png")), 0,2, 2, 1);
        foreground.add(buttons, 0, 3, 2,1);

        setupGridConstraints();

        foreground.setBackground(new Background(new BackgroundFill(new Color(.6, .6, .6, .75), null, null)));
        this.getChildren().addAll(background,foreground);
        background.clean(finalScore);
    }

    public EndScreen(MainWindow parent, Stone resigned, Game game, GameScreen background){
        mainWindow = parent;
        this.game = game;

        if(game.blackPlayer instanceof ComputerPlayer || game.whitePlayer instanceof ComputerPlayer){
            winTitle = new Label("You lose.");
            winTitle.setTextFill(Color.RED);
        } else {
            winTitle = new Label(resigned==Stone.BLACK ? "White wins!": "Black wins!");
            winTitle.setTextFill(resigned==Stone.BLACK ? Color.WHITE: Color.BLACK);
        }

        Label byResignation = new Label("by resignation");
        byResignation.setPadding(new Insets(30, 0, 0, 0));
        byResignation.setFont(scoringFont);

        setupButtons();

        winTitle.setFont(new Font(60));
        padTitle();

        foreground.add(winTitle, 0, 0, 2, 1);
        foreground.add(byResignation, 0, 1, 2, 1);
        foreground.add(buttons, 0, 2, 2, 1);

        foreground.getColumnConstraints().addAll(new ColumnConstraints(400), new ColumnConstraints(400));
        foreground.getColumnConstraints().get(0).setHalignment(HPos.CENTER);
        foreground.getColumnConstraints().get(1).setHalignment(HPos.CENTER);

        foreground.getRowConstraints().addAll(new RowConstraints(80), new RowConstraints(705), new RowConstraints(67+48));
        foreground.getRowConstraints().get(1).setValignment(VPos.TOP);

        background.clean();
        foreground.setBackground(new Background(new BackgroundFill(new Color(.6, .6, .6, .75), null, null)));
        this.getChildren().addAll(background,foreground);
    }

    private void setTitle(){
        if(game.blackPlayer instanceof ComputerPlayer || game.whitePlayer instanceof ComputerPlayer){
            if(game.blackPlayer instanceof HumanPlayer) {
                if(blackScore > whiteScore){
                    winTitle = new Label("You win!");
                    winTitle.setTextFill(Color.GREEN);
                } else if(blackScore==whiteScore){
                    winTitle = new Label("Tie!");
                    winTitle.setTextFill(Color.BLACK);
                } else {
                    winTitle = new Label("You lose.");
                    winTitle.setTextFill(Color.RED);
                }
            } else {
                if(blackScore < whiteScore){
                    winTitle = new Label("You win!");
                    winTitle.setTextFill(Color.GREEN);
                } else if(blackScore==whiteScore){
                    winTitle = new Label("Tie!");
                    winTitle.setTextFill(Color.BLACK);
                } else {
                    winTitle = new Label("You lose.");
                    winTitle.setTextFill(Color.RED);
                }
            }
        } else {
            winTitle = new Label(whiteScore>blackScore ? "White wins!": (whiteScore==blackScore? "Tie!":"Black wins!"));
            winTitle.setTextFill(whiteScore>blackScore ? Color.WHITE: Color.BLACK);
        }
    }

    private void formatScore(Label label){
        label.setTextAlignment(TextAlignment.RIGHT);
        label.setFont(scoringFont);
        label.setTextFill(Color.BLACK);
    }

    private void setupGridConstraints() {
        foreground.getColumnConstraints().addAll(new ColumnConstraints(400), new ColumnConstraints(400));
        foreground.getColumnConstraints().get(0).setHalignment(HPos.RIGHT);
        foreground.getColumnConstraints().get(1).setHalignment(HPos.RIGHT);

        foreground.getRowConstraints().addAll(new RowConstraints(80), new RowConstraints(705), new RowConstraints(48), new RowConstraints(67));
        foreground.getRowConstraints().get(0).setValignment(VPos.TOP);
        foreground.getRowConstraints().get(1).setValignment(VPos.CENTER);
        foreground.getRowConstraints().get(2).setValignment(VPos.CENTER);
        foreground.getRowConstraints().get(3).setValignment(VPos.BOTTOM);
    }

    private void padTitle(){
        String text = winTitle.getText();
        if(text.equals("Tie!"))
            winTitle.setPadding(new Insets(0, 350.5, 0, 350.5));
        else if(text.equals("You win!"))
            winTitle.setPadding(new Insets(0, 283.5, 0, 283.5));
        else if(text.equals("You lose."))
            winTitle.setPadding(new Insets(0, 277, 0, 277));
        else if(text.equals("White wins!"))
            winTitle.setPadding(new Insets(0, 244, 0, 244));
        else if(text.equals("Black wins!"))
            winTitle.setPadding(new Insets(0, 256, 0, 256));
    }

    private void setupButtons(){
        Button home = new Button("Home");
        home.setFont(scoringFont);
        home.setPadding(new Insets(0, 108.5, 0, 108.5));
        home.setOnMouseClicked(event ->{
            mainWindow.changeScreen(new TitleScreen(mainWindow));
        });
        Button playAgain = new Button("Play again");
        playAgain.setFont(scoringFont);
        playAgain.setPadding(new Insets(0, 69, 0,69));
        playAgain.setOnMouseClicked(event -> {
            mainWindow.changeScreen(new GameScreen(mainWindow, game.whitePlayer, game.blackPlayer, game.size, game.komi, game.handicap));
        });

        buttons.getColumnConstraints().addAll(new ColumnConstraints(400), new ColumnConstraints(400));
        buttons.getColumnConstraints().get(0).setHalignment(HPos.CENTER);
        buttons.getColumnConstraints().get(1).setHalignment(HPos.CENTER);

        buttons.add(home, 0, 0);
        buttons.add(playAgain, 1, 0);
    }
}
