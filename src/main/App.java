package main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.stage.Stage;
import main.Models.GoogleBooksAPI;
import main.Models.Model;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        // Chạy ứng dụng JavaFX
        // String isbn = "9780140449136";
        // GoogleBooksAPI.searchBookByISBN(isbn);
        launch(args);

    }
}
