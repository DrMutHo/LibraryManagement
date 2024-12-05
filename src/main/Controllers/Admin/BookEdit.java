package main.Controllers.Admin;

import java.awt.Dialog;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import main.Models.Book;
import main.Models.BookReview;
import main.Models.Model;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.net.URL;
import java.net.URI;

/**
 * Controller class for editing book details in the admin panel.
 * Handles updating book information and interfacing with the UI components.
 */
public class BookEdit {

    // FXML Injected Elements

    /** ImageView for displaying the book's image */
    @FXML
    private ImageView bookImageView;

    /** Label for the book's title */
    @FXML
    private Label labelTitle;

    /** Label for the book's author */
    @FXML
    private Label labelAuthor;

    /** Label for the book's ISBN */
    @FXML
    private Label labelISBN;

    /** Label for the book's genre */
    @FXML
    private Label labelGenre;

    /** Label for the book's language */
    @FXML
    private Label labelLanguage;

    /** Label for the book's publication year */
    @FXML
    private Label labelPublicationYear;

    /** Label for the book's original quantity */
    @FXML
    private Label originalquantity;

    /** TextArea for the book's description */
    @FXML
    private TextArea textDescription;

    // Fields for editing book details

    /** ImageView for displaying the new image of the book */
    @FXML
    private ImageView bookImageView1;

    /** TextField for entering the new title */
    @FXML
    private TextField textTitle1;

    /** TextField for entering the new author */
    @FXML
    private TextField textAuthor1;

    /** TextField for entering the new ISBN */
    @FXML
    private TextField textISBN1;

    /** TextField for entering the new genre */
    @FXML
    private TextField textGenre1;

    /** TextField for entering the new language */
    @FXML
    private TextField textLanguage1;

    /** TextField for entering the new publication year */
    @FXML
    private TextField textPublicationYear1;

    /** TextField for entering the new quantity */
    @FXML
    private TextField Quantity;

    /** TextField for entering the image URL */
    @FXML
    private TextField imageurl;

    // Buttons

    /** Button to save changes */
    @FXML
    private Button saveChangesButton;

    /** Button to cancel changes */
    @FXML
    private Button cancelButton;

    /** The current book being edited */
    private Book currentBook;

    /** The user's review of the book (if applicable) */
    private BookReview userReview = null;

    /** The selected rating for the book */
    private int selectedRating = 0;

    /**
     * Sets the book to be edited and initializes the UI components with its details.
     *
     * @param book The book to edit.
     */
    public void setBook(Book book) {
        this.currentBook = book;
        textDescription.setPromptText(this.currentBook.getDescription());
        displayBookDetails();
    }

    /**
     * Displays the details of the current book in the UI components.
     */
    private void displayBookDetails() {
        labelTitle.textProperty().bind(Bindings.concat("Title: ", currentBook.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat("Author: ", currentBook.authorProperty()));
        labelISBN.textProperty().bind(Bindings.concat("ISBN: ", currentBook.isbnProperty()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ", currentBook.genreProperty()));
        labelLanguage.textProperty().bind(Bindings.concat("Language: ", currentBook.languageProperty()));
        labelPublicationYear.textProperty()
                .bind(Bindings.concat("Publication Year: ", currentBook.publication_yearProperty().asString()));

        originalquantity.textProperty()
                .bind(Bindings.concat("Quantity: ", currentBook.quantityProperty().asString()));

        if (currentBook.getImagePath() != null && !currentBook.getImagePath().isEmpty()) {
            try {
                Image image = new Image(currentBook.getImagePath());
                bookImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Image not found: " + currentBook.getImagePath());
                bookImageView.setImage(null);
            }
        } else {
            bookImageView.setImage(null);
        }
    }

    /**
     * Handles the action when the save changes button is clicked.
     * Updates the book's details based on user input and saves changes to the database.
     */
    @FXML
    public void onSubmitReview() {
        // Process the new book details entered by the user
        String title = textTitle1.getText();
        String author = textAuthor1.getText();
        String isbn = textISBN1.getText();
        String genre = textGenre1.getText();
        String language = textLanguage1.getText();
        String publicationYear = textPublicationYear1.getText();
        String description = textDescription.getText();
        String urls = imageurl.getText();
        String quantity = Quantity.getText();

        String imagePath = currentBook.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            bookImageView.setImage(new Image(imagePath));
        } else {
            bookImageView.setImage(new Image("/resources/Images/default.png"));
        }

        if (!title.isEmpty()) {
            currentBook.setTitle(title);
        }

        if (!author.isEmpty()) {
            currentBook.setAuthor(author);
        }

        if (!isbn.isEmpty()) {
            currentBook.setIsbn(isbn);
        }

        if (!genre.isEmpty()) {
            currentBook.setGenre(genre);
        }

        if (!language.isEmpty()) {
            currentBook.setLanguage(language);
        }

        if (!publicationYear.isEmpty()) {
            try {
                // Convert publicationYear to an integer
                int publicationYearInt = Integer.parseInt(publicationYear);
                currentBook.setPublication_year(publicationYearInt);
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                e.printStackTrace(); // Optionally show a user-friendly message or log the error
            }
        }

        if (!description.isEmpty()) {
            currentBook.setDescription(description);
        }

        if (!quantity.isEmpty()) {
            try {
                int quantityint = Integer.parseInt(quantity);
                int bookleft = Model.getInstance().getDatabaseDriver().adjustBookCopies(currentBook.getBook_id(), quantityint);
                currentBook.setQuantity(bookleft);
                quantityint = bookleft;

                if (quantityint == 0) {
                    Model.getInstance().getAllBook().remove(currentBook);
                }
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                e.printStackTrace(); // Optionally show a user-friendly message or log the error
            }
        }

        Model.getInstance().getDatabaseDriver().updateBook(currentBook);

        showSuccess("Changes Saved");
        // Here you can also save the changes to a database, or a file, or process the data
        System.out.println("Changes Saved: " + title + ", " + author);
    }

    /**
     * Displays a success message in an alert dialog.
     *
     * @param message The message to display.
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Success");
        alert.showAndWait();
    }

    /**
     * Handle the action when the cancel button is clicked.
     * Clears the input fields and resets any changes.
     */
    @FXML
    public void onCancel() {
        // This will reset the text fields to their previous state or empty them
        textTitle1.clear();
        textAuthor1.clear();
        textISBN1.clear();
        textGenre1.clear();
        textLanguage1.clear();
        textPublicationYear1.clear();
        textDescription.clear();

        System.out.println("Changes Canceled.");
    }

    /**
     * Initializes the controller.
     * Additional initialization logic can be added here.
     */
    public void initialize() {
        // Initialize with default data or from database if necessary
    }
}
