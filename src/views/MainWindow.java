package views;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainWindow extends Stage {

    private BorderPane mainPane;

    public MainWindow(){
        mainPane = new BorderPane();
        this.setResizable(false);
        Scene mainScene = new Scene(mainPane, 800, 900);
        this.setScene(mainScene);
        this.setTitle("GO");
        TitleScreen screen = new TitleScreen(this);
        mainPane.setCenter(screen);
    }

    public void changeScreen(Pane newPane){
        mainPane.setCenter(newPane);
    }
}
