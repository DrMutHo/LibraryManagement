package main.Controllers.Admin;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import main.Models.Book;
import main.Models.BookReview;
import main.Models.Model;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


public class BookEdit {

    // FXML Injected Elements
    @FXML
    private ImageView bookImageView;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelAuthor;
    @FXML
    private Label labelISBN;
    @FXML
    private Label labelGenre;
    @FXML
    private Label labelLanguage;
    @FXML
    private Label labelPublicationYear;
    @FXML
    private TextArea textDescription;

    // Fields for editing book details
    @FXML
    private ImageView bookImageView1;
    @FXML
    private TextField textTitle1;
    @FXML
    private TextField textAuthor1;
    @FXML
    private TextField textISBN1;
    @FXML
    private TextField textGenre1;
    @FXML
    private TextField textLanguage1;
    @FXML
    private TextField textPublicationYear1;
    @FXML
    private TextField imageurl;

    // Buttons
    @FXML
    private Button saveChangesButton;
    @FXML
    private Button cancelButton;

    private Book currentBook;
    private BookReview userReview = null;
    private int selectedRating = 0;

    public void setBook(Book book) {
        this.currentBook = book;
        displayBookDetails();
    }

    private void displayBookDetails() {
        labelTitle.textProperty().bind(Bindings.concat("Title: ", currentBook.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat("Author: ", currentBook.authorProperty()));
        labelISBN.textProperty().bind(Bindings.concat("ISBN: ", currentBook.isbnProperty()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ", currentBook.genreProperty()));
        labelLanguage.textProperty().bind(Bindings.concat("Language: ", currentBook.languageProperty()));
        labelPublicationYear.textProperty()
                .bind(Bindings.concat("Publication Year: ", currentBook.publication_yearProperty().asString()));
       
        if (currentBook.getImagePath() != null && !currentBook.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(currentBook.getImagePath()));
                bookImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Image not found: " + currentBook.getImagePath());
                bookImageView.setImage(null);
            }
        } else {
            bookImageView.setImage(null);
        }
    }

    // Method to handle saving changes
    @FXML
    public void onSubmitReview() {
        // Here, you would process the new book details entered by the user
        String title = textTitle1.getText();
        String author = textAuthor1.getText();
        String isbn = textISBN1.getText();
        String genre = textGenre1.getText();
        String language = textLanguage1.getText();
        String publicationYear = textPublicationYear1.getText();
        String description = textDescription.getText();
        String url = imageurl.getText();
        Image imagedak = new Image(url);
        bookImageView1.setImage(imagedak);
        currentBook.setTitle(title);
        currentBook.setAuthor(author);
        currentBook.setIsbn(isbn);
        currentBook.setGenre(genre);
        currentBook.setLanguage(language);
        currentBook.setPublication_year(selectedRating);
        currentBook.setImage_path(url);

        Model.getInstance().getDatabaseDriver()
                    .updateBook(currentBook);

        // Here you can also save the changes to a database, or a file, or process the data
        System.out.println("Changes Saved: " + title + ", " + author);
    }

    // Method to handle canceling changes
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

    // Additional methods can be added here to load book data into the UI when the page loads
    public void initialize() {
        // Initialize with default data or from database if necessary
        
    }
}
