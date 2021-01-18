package views;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage stage = new MainWindow();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}