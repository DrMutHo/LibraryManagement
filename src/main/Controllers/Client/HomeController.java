package main.Controllers.Client;

import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Models.Book;
import main.Models.BookRecommendation;
import main.Models.Model;

public class HomeController implements Initializable {
    @FXML
    private HBox highestRatedBooks;
    @FXML
    private HBox Recommended;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private Label bookTitle;
    @FXML
    private TextArea description;
    @FXML
    private HBox readingBook1;
    @FXML
    private Rectangle rec;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Book readingBook = Model.getInstance().getReadingBook();
        bookTitle.setText(readingBook.getTitle());
        description.setText(readingBook.getDescription());
        rec.setArcWidth(20);
        rec.setArcHeight(20);
        ImagePattern pattern = new ImagePattern(
                new Image(readingBook.getImagePath()));
        rec.setFill(pattern);
        rec.setStroke(Color.TRANSPARENT);
        ObservableList<String> genres = FXCollections.observableArrayList(
                "All", "Fiction", "Dystopian", "Romance", "Adventure", "Historical", "Fantasy", "Philosophical", "Epic",
                "Modernist", "Gothic", "Magic Realism", "Existential", "Literature", "War", "Science Fiction");
        genreComboBox.setItems(genres);

        genreComboBox.setValue("All");

        updateHighestRatedBooks("All");

        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateHighestRatedBooks(newValue);
        });
    }

    private void addBookToHighestRatedBooks(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/FXML/Client/Card.fxml"));
            VBox cardBox = loader.load();

            CardController cardController = loader.getController();
            cardController.setData(book);
            addOpenBookEffect(cardBox, cardController);
            addReadingBookEffect(readingBook1);
            readingBook1.setOnMouseClicked(event -> showBookDetails(Model.getInstance().getReadingBook()));
            cardBox.setOnMouseClicked(event -> showBookDetails(book));

            highestRatedBooks.getChildren().add(cardBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBookToRecommended(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/FXML/Client/Card.fxml"));
            VBox cardBox = loader.load();

            CardController cardController = loader.getController();
            cardController.setData(book);
            addOpenBookEffect(cardBox, cardController);
            addReadingBookEffect(readingBook1);
            readingBook1.setOnMouseClicked(event -> showBookDetails(Model.getInstance().getReadingBook()));
            cardBox.setOnMouseClicked(event -> showBookDetails(book));

            Recommended.getChildren().add(cardBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addReadingBookEffect(HBox readingBook) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), readingBook);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), readingBook);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        readingBook.setOnMouseEntered(event -> {
            scaleUp.playFromStart();
            readingBook.setStyle("-fx-cursor: hand;");
        });

        readingBook.setOnMouseExited(event -> {
            scaleDown.playFromStart();
            readingBook.setStyle("-fx-cursor: default;");
        });
    }

    private void addOpenBookEffect(VBox cardBox, CardController cardController) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), cardBox);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), cardBox);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        cardBox.setOnMouseEntered(event -> {
            scaleUp.playFromStart();
            cardController.ratingBar.setVisible(true);
        });

        cardBox.setOnMouseExited(event -> {
            scaleDown.playFromStart();
            cardController.ratingBar.setVisible(false);
        });

    }

    private void showBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/FXML/Client/BookDetailWithReview.fxml"));
            BorderPane bookDetailsRoot = loader.load();

            BookDetailWithReviewController controller = loader.getController();
            controller.setBook(book);

            Stage stage = new Stage();
            stage.setTitle("Book Details");
            stage.setScene(new Scene(bookDetailsRoot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateHighestRatedBooks(String genre) {
        highestRatedBooks.getChildren().clear();
        Recommended.getChildren().clear();

        Model.getInstance().setHighestRatedBooks(genre);

        BookRecommendation bookRecommendation = new BookRecommendation();
        List<Book> dak = bookRecommendation.Recommendation();

        for (Book book : Model.getInstance().getHighestRatedBook()) {
            addBookToHighestRatedBooks(book);
        }

        for(Book book : dak){
            addBookToRecommended(book);
        }
    }

}
