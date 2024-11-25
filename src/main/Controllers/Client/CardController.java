package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.Models.Book;

public class CardController implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private Label title;

    @FXML
    private Label author;

    @FXML
    private Label rating;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Book book) {
        String image_path = getClass().getResource(book.getImagePath()).toExternalForm();
        Image bookCoverImage = new Image(image_path, true);
        imageView.setImage(bookCoverImage);
        title.setText(book.getTitle());
        author.setText("By " + book.getAuthor());
        rating.setText("★ " + book.getAverage_rating() + " (" + book.getReview_count() +
                ")");

    }
}