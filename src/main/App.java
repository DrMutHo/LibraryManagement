package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.Models.Model;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        // Chạy ứng dụng JavaFX
        launch(args);
    }
}
