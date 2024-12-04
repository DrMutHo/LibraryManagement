package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.Models.Model;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().setAllBooksAverageRating();
    }

    public static void main(String[] args) {
        // Chạy ứng dụng JavaFX
        // String isbn = "9780140449136";
        // GoogleBooksAPI.searchBookByISBN(isbn);

        launch(args);

    }
}
