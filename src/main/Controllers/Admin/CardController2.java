package main.Controllers.Admin;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import main.Models.Book;
import java.net.URL;
import main.Models.Model;

/**
 * Controller class for the card view used in the admin panel.
 * Manages the display and interaction of book information within a card UI component.
 */
public class CardController2 implements Initializable {

    /** Label for the book title */
    @FXML
    private Label titleLabel;

    /** ImageView for the book image */
    @FXML
    private ImageView bookImage;

    /** Label for the book ISBN */
    @FXML
    private Label isbnLabel;

    /** Button to add the book */
    @FXML
    private Button AddBook;

    /** TextField to input the quantity of books to add */
    @FXML
    private TextField Quantity;

    /** VBox containing ISBN and title labels */
    @FXML
    private VBox vBoxIsbnAndTitle;

    /** VBox containing quantity field and add button */
    @FXML
    private VBox vBoxQuantityAndButton;

    /** HBox representing the card layout */
    @FXML
    private HBox card;

    /** The current book associated with this card */
    private Book currentBook;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up layout properties and initializes the current book.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox.setHgrow(vBoxIsbnAndTitle, Priority.ALWAYS);

        vBoxQuantityAndButton.setStyle("-fx-alignment: CENTER_RIGHT;");
        currentBook = new Book();
    }

    /**
     * Sets the book data to be displayed in the card.
     *
     * @param book The book whose data is to be displayed
     */
    public void setBookData(Book book) {
        this.currentBook = book;
        String imagePath = book.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            bookImage.setImage(new Image(imagePath));
        } else {
            bookImage.setImage(new Image("/resources/Images/default.png"));
        }

        if (isbnLabel != null) {
            isbnLabel.setText("ISBN: " + book.getIsbn());
        }

        if (titleLabel != null) {
            titleLabel.setText(book.getTitle());
        }
    }

    /**
     * Handles the action of adding the book with the specified quantity.
     * Parses the quantity input, updates the model, and shows success or error messages.
     */
    public void AddBookCTL() {
        try {
            // Get the quantity from the Quantity text field
            int quantity = Integer.parseInt(Quantity.getText());

            if (quantity <= 0) {
                showError("Quantity must be greater than 0.");
                return;
            }

            // Call the method in Model to add the book with the specified quantity
            Model.getInstance().AddBookCTL(currentBook, quantity);

            // Notify the model to inform about the addition of the book
            Model.getInstance().notifyAddBookEvent();

            // Display success message
            showSuccess("Book added successfully with " + quantity + " copies.");
        } catch (NumberFormatException e) {
            // Handle the case where the input quantity is not a valid number
            showError("Please enter a valid quantity.");
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            showError("An error occurred while adding the book.");
        }
    }

    /**
     * Displays a success message in an alert dialog.
     *
     * @param message The success message to display
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Success");
        alert.showAndWait();
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.showAndWait();
    }

    /**
     * Return the HBox representing the card.
     *
     * @return The card HBox
     */
    public HBox getCard() {
        return card;
    }
}
