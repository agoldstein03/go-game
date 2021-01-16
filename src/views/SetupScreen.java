package views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SetupScreen extends GridPane {

    public static final Font pt15 = new Font(15);

    private ComboBox handicapSelector;
    private ComboBox boardSelector;
    private boolean vsAI;
    private MainWindow mainStage;

    public SetupScreen(MainWindow parent, boolean AIGame){
        setBackground(TitleScreen.WOODBACKGROUND);
        mainStage = parent;
        vsAI = AIGame;
        //this.setGridLinesVisible(true);
        setupGridConstraints();
        setupGraphics();
    }

    private void setupGraphics(){
        Label screenTitle = new Label("Settings");
        add(screenTitle, 0, 0, 2, 1);
        screenTitle.setTextAlignment(TextAlignment.CENTER);
        screenTitle.setFont(new Font(125));


        Label handicap = new Label("Handicap");
        handicap.setFont(new Font(60));
        handicap.setPadding(new Insets(0,0,0,148));
        add(handicap, 0, 1, 1, 2);

        ObservableList<Integer> handicapOptions =FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        handicapSelector = new ComboBox(handicapOptions);
        handicapSelector.getSelectionModel().selectFirst();
        handicapSelector.setStyle("-fx-font-size: 15pt;");
        add(handicapSelector, 1, 2);

        Label boardSize = new Label("Board size");
        boardSize.setFont(new Font(60));
        boardSize.setPadding(new Insets(0,0,0,128));
        add(boardSize, 0, 3);

        ObservableList<String> boardSizes = FXCollections.observableArrayList("9x9", "13x13", "19x19");
        boardSelector = new ComboBox(boardSizes);
        boardSelector.getSelectionModel().select(2);
        boardSelector.setStyle("-fx-font-size: 15pt;");
        add(boardSelector, 1, 4);




        Label komi = new Label("Komi");
        komi.setFont(new Font(60));
        komi.setPadding(new Insets(0,0,0,262));
        add(komi, 0, 5, 1, 2);


        TextField komiEntry = new TextField();
        komiEntry.setPromptText("Enter a multiple of 0.5 (7.5 recommended)");
        komiEntry.setMaxWidth(300);
        komiEntry.setFont(pt15);
        //komiEntry.setStyle("-fx-background-color:transparent;");
        //komiEntry.setPadding(new Insets(30,0,0,0));
        komiEntry.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}([\\.]\\d{0,1})?")) {
                    komiEntry.setText(oldValue);
                }else if(!newValue.equals("") && !(Double.parseDouble(newValue)%.5==0)){
                    komiEntry.setText(oldValue);
                }
            }
        });
        add(komiEntry, 1, 6);

        Button back = new Button("Back");
        back.setFont(new Font(45));
        //back.setPadding(new Insets(0,123,0,0));
        back.setOnMouseClicked(event -> {
            mainStage.changeScreen(new TitleScreen(mainStage));
        });


        Button start = new Button("Start");
        start.setFont(new Font(45));
        start.setOnMouseClicked(event -> {

        });


        if(vsAI){
            Label color = new Label("Color");
            color.setFont(new Font(60));
            color.setPadding(new Insets(0,0,0,256));
            add(color, 0,7);

            ToggleGroup playerColor = new ToggleGroup();

            RadioButton playerBlack = new RadioButton("Black");
            playerBlack.setFont(new Font(25));
            //playerBlack.setPadding(new Insets(30,0,0,0));
            playerBlack.setToggleGroup(playerColor);

            RadioButton playerWhite = new RadioButton("White");
            playerWhite.setFont(new Font(25));
            playerWhite.setPadding(new Insets(0,0,0,8));
            playerWhite.setToggleGroup(playerColor);

            playerColor.selectToggle(playerBlack);

            add(playerBlack, 1, 8);
            add(playerWhite, 1, 9);

            add(start, 1, 11);
            add(back, 0, 11);
        } else {
            add(start, 1, 10);
            add(back, 0, 10);
        }

        for(Node node : this.getChildren()){
            node.setStyle(node.getStyle()+"-fx-faint-focus-color: transparent;" + "-fx-focus-color: transparent;");
        }



    }

    private void setupGridConstraints(){
        this.getColumnConstraints().addAll(new ColumnConstraints(400), new ColumnConstraints(400));
        ObservableList<RowConstraints> rowConstraints = this.getRowConstraints();
        rowConstraints.addAll(new RowConstraints(128),
                new RowConstraints(30), new RowConstraints(98),
                new RowConstraints(25), new RowConstraints(103),
                new RowConstraints(30), new RowConstraints(98),
                new RowConstraints(30), new RowConstraints(50), new RowConstraints(48),
                new RowConstraints(128));

        for(RowConstraints constraints: rowConstraints){
            constraints.setValignment(VPos.TOP);
        }
        rowConstraints.get(0).setValignment(VPos.CENTER);

        getColumnConstraints().get(0).setHalignment(HPos.CENTER);
        getColumnConstraints().get(1).setHalignment(HPos.CENTER);

    }

}
