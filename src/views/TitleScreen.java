package views;

import com.sun.rowset.internal.Row;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TitleScreen extends GridPane {

    private MainWindow mainStage;
    private GridPane pane;

    public static final Background WOODBACKGROUND = new Background(new BackgroundImage(new Image(TitleScreen.class.getResourceAsStream("/WoodBackground.png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

    public TitleScreen(MainWindow parent){
        super();
        mainStage = parent;
        this.getColumnConstraints().add(new ColumnConstraints(250));
        this.getColumnConstraints().add(new ColumnConstraints(300));
        this.getColumnConstraints().add(new ColumnConstraints(250));
        this.getColumnConstraints().get(0).setHalignment(HPos.CENTER);
        this.getColumnConstraints().get(1).setHalignment(HPos.CENTER);
        this.getColumnConstraints().get(2).setHalignment(HPos.CENTER);

        this.getRowConstraints().addAll(new RowConstraints(400), new RowConstraints(300), new RowConstraints(200));
        //this.setGridLinesVisible(true);

        initializeGraphics();
    }

    private void initializeGraphics(){
        this.setBackground(WOODBACKGROUND);

        Label mainTitle = new Label("GO");
        mainTitle.setFont(new Font(150));
        mainTitle.setTextFill(Color.WHITE);
        mainTitle.setAlignment(Pos.CENTER);
        this.add(mainTitle, 1, 0);

        Label chineseTitle = new Label("\n\n\n围棋");
        chineseTitle.setFont(new Font(60));
        chineseTitle.setTextFill(Color.WHITE);
        chineseTitle.setAlignment(Pos.CENTER);
        this.add(chineseTitle, 1, 0);

        Button vsAI = new Button("Play vs AI");
        vsAI.setAlignment(Pos.CENTER);
        vsAI.setFont(new Font(25));
        this.add(vsAI, 0,1, 2, 1);
        vsAI.setOnMouseClicked(event -> {
            mainStage.changeScreen(new SetupScreen(mainStage, true));
        });
        vsAI.setStyle("-fx-focus-color: transparent;");

        Button vsHuman = new Button("Play vs Human");
        vsHuman.setAlignment(Pos.CENTER);
        vsHuman.setFont(new Font(25));
        this.add(vsHuman, 1, 1, 2, 1);
        vsHuman.setOnMouseClicked(event -> {
            mainStage.changeScreen(new SetupScreen(mainStage, false));
        });
        vsHuman.setStyle("-fx-focus-color: transparent;");

        Label credits = new Label("Created by\nAdam Goldstein and Carter Matties");
        credits.setTextAlignment(TextAlignment.CENTER);
        credits.setTextFill(Color.WHITE);
        credits.setFont(new Font(20));
        this.add(credits, 0, 2, 3, 1);

        for(Node node : this.getChildren()){
            node.setStyle("-fx-faint-focus-color: transparent;" + "-fx-focus-color: transparent");
        }
    }
}
