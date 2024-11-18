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
        // rating.setText(book.getAverage_rating() + " (" + book.getReview_count() +
        // ")");

        addOpenBookEffect();
    }

    private void addOpenBookEffect() {
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.5), imageView);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.5), imageView);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        imageView.setOnMouseEntered(event -> scaleUp.playFromStart());

        imageView.setOnMouseExited(event -> scaleDown.playFromStart());
    }
}