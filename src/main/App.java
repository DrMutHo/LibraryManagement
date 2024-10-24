package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Models.GoogleBooksAPI;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/Fxml/Login.fxml"));

            Scene scene = new Scene(root, 850, 750);

            primaryStage.setTitle("Library Management System");

            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Chạy ứng dụng JavaFX
        // String isbn = "9780140449136";
        // GoogleBooksAPI.searchBookByISBN(isbn);
        launch(args);

    }
}
