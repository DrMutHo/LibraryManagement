package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.RotateTransition;
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
        RotateTransition rotateCover = new RotateTransition(Duration.seconds(0.5), imageView);
        rotateCover.setFromAngle(0);
        rotateCover.setToAngle(-30);

        TranslateTransition translateCover = new TranslateTransition(Duration.seconds(0.5), imageView);
        translateCover.setFromX(0);
        translateCover.setToX(-10);

        imageView.setOnMouseEntered(event -> {
            rotateCover.playFromStart();
            translateCover.playFromStart();
        });

        imageView.setOnMouseExited(event -> {
            rotateCover.setRate(-1); // Đảo ngược hiệu ứng
            translateCover.setRate(-1);
            rotateCover.play();
            translateCover.play();
        });
    }
}