package views;

import javafx.application.Application;
import javafx.stage.Stage;

import data.Game;

public class Main {

    private static Game game;

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Main.game = game;
    }

    public static class MainApplication extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            Stage stage = new MainWindow();
            stage.show();
        }
    }

    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }

}
