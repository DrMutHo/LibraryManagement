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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Models.Book;

public class CardController implements Initializable {
    @FXML
    private Rectangle imageView;

    @FXML
    private Label title;

    @FXML
    private Label author;

    @FXML
    private Text rating;

    @FXML
    public HBox ratingBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ratingBar.setVisible(false);

    }

    public void setData(Book book) {
        imageView.setArcWidth(20);
        imageView.setArcHeight(20);
        ImagePattern pattern = new ImagePattern(
                new Image(book.getImagePath()));
        imageView.setFill(pattern);
        imageView.setStroke(Color.TRANSPARENT);
        title.setText(book.getTitle());
        author.setText("By " + book.getAuthor());
        rating.setText(book.getAverage_rating() + " ★");

    }
}