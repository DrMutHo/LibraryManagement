package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

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
import main.Models.Book;

/**
 * Controller for managing the display of a book card, including the book's
 * image, title, author, and rating.
 */
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

    /**
     * Initializes the controller, hiding the rating bar by default.
     * This method is called after the FXML is loaded to set up initial state.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ratingBar.setVisible(false);
    }

    /**
     * Populates the book card with data from the given {@link Book}.
     * This includes setting the image, title, author, and rating for the book card.
     * 
     * @param book The {@code Book} to display on the card.
     */
    public void setData(Book book) {
        // Set the image with rounded corners
        imageView.setArcWidth(20);
        imageView.setArcHeight(20);
        imageView.setFill(new ImagePattern(new Image(book.getImagePath())));
        imageView.setStroke(Color.TRANSPARENT);

        // Set the title and author labels
        title.setText(book.getTitle());
        author.setText("By " + book.getAuthor());

        // Set the rating text
        rating.setText(book.getAverage_rating() + " ★");
    }

    // Getter methods for the view components

    /**
     * Gets the image view for the book card.
     * 
     * @return the Rectangle that holds the book image.
     */
    public Rectangle getImageView() {
        return imageView;
    }

    /**
     * Gets the title label for the book card.
     * 
     * @return the Label that displays the book title.
     */
    public Label getTitle() {
        return title;
    }

    /**
     * Gets the author label for the book card.
     * 
     * @return the Label that displays the book author.
     */
    public Label getAuthor() {
        return author;
    }

    /**
     * Gets the rating text for the book card.
     * 
     * @return the Text element that displays the book's rating.
     */
    public Text getRating() {
        return rating;
    }

    /**
     * Gets the HBox containing the rating bar.
     * 
     * @return the HBox that holds the book's rating bar.
     */
    public HBox getRatingBar() {
        return ratingBar;
    }
}
