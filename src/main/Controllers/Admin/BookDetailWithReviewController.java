package main.Controllers.Admin;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Models.Book;
import main.Models.BookReview;
import main.Models.Model;
import main.Models.Notification;
import main.Models.NotificationRequest;
import main.Views.NotificationType;
import main.Views.RecipientType;
import main.Models.BookCopy;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller class for displaying book details and reviews in the admin panel.
 * Handles the display of book information and associated reviews.
 */
public class BookDetailWithReviewController {

    /** ImageView for displaying the book's image */
    @FXML
    private ImageView bookImageView;

    /** Label for displaying the book's title */
    @FXML
    private Label labelTitle;

    /** Label for displaying the book's author */
    @FXML
    private Label labelAuthor;

    /** Label for displaying the book's ISBN */
    @FXML
    private Label labelISBN;

    /** Label for displaying the book's genre */
    @FXML
    private Label labelGenre;

    /** Label for displaying the book's language */
    @FXML
    private Label labelLanguage;

    /** Label for displaying the book's publication year */
    @FXML
    private Label labelPublicationYear;

    /** Label for displaying the book's average rating */
    @FXML
    private Label labelAverageRating;

    /** Label for displaying the book's review count */
    @FXML
    private Label labelReviewCount;

    /** Label for displaying the book's description */
    @FXML
    private Label labelDescription;

    /** TableView for displaying the book reviews */
    @FXML
    private TableView<BookReview> reviewTable;

    /** TableColumn for the rating in reviews */
    @FXML
    private TableColumn<BookReview, String> colRating;

    /** TableColumn for the comment in reviews */
    @FXML
    private TableColumn<BookReview, String> colComment;

    /** TableColumn for the review date */
    @FXML
    private TableColumn<BookReview, String> colReviewDate;

    /** TableColumn for the reviewer */
    @FXML
    private TableColumn<BookReview, String> colReviewer;

    /** The current book being displayed */
    private Book currentBook;

    /**
     * Sets the current book and initializes the display.
     *
     * @param book The book to display.
     */
    public void setBook(Book book) {
        this.currentBook = book;
        displayBookDetails();
        loadReviews();
    }

    /**
     * Displays the details of the current book.
     * Binds the book properties to the UI components.
     */
    private void displayBookDetails() {
        labelTitle.textProperty().bind(Bindings.concat(currentBook.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat(currentBook.authorProperty()));
        labelISBN.textProperty().bind(Bindings.concat("ISBN: ", currentBook.isbnProperty()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ",
                currentBook.genreProperty()));
        labelLanguage.textProperty().bind(Bindings.concat("Language: ", currentBook.languageProperty()));
        if (currentBook.getPublication_year() == -1) {
            labelPublicationYear.textProperty()
                    .bind(Bindings.concat("Publication Year: No Publication Year",
                            currentBook.publication_yearProperty().asString()));
        } else {
            labelPublicationYear.textProperty()
                    .bind(Bindings.concat("Publication Year: ",
                            currentBook.publication_yearProperty().asString()));
        }
        labelAverageRating.textProperty()
                .bind(Bindings.createStringBinding(() -> getStars(currentBook.average_ratingProperty().get()),
                        currentBook.average_ratingProperty()));
        labelAverageRating.setStyle("-fx-text-fill: gold;");
        labelReviewCount.textProperty()
                .bind(Bindings.concat("(", currentBook.review_countProperty().asString(), " ratings)"));

        labelDescription.textProperty().bind(Bindings.concat(currentBook.descriptionProperty()));

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

    /**
     * Loads the reviews for the current book into the review table.
     * Customizes the table cells to display stars for ratings and uses TextFlow for
     * long comments.
     */
    private void loadReviews() {
        ObservableList<BookReview> reviews = FXCollections.observableArrayList(
                Model.getInstance().getDatabaseDriver().getAllReviewsForBook(currentBook.getBook_id()));
        reviewTable.setItems(reviews);

        // Set cell value factories for the rating column
        colRating.setCellValueFactory(cellData -> {
            double rating = cellData.getValue().getRating();
            String stars = getStars(rating);
            return new javafx.beans.property.SimpleStringProperty(stars);
        });

        // Set cell value factories for the reviewer column
        colReviewer.setCellValueFactory(cellData -> {
            String clientName = Model.getInstance().getDatabaseDriver()
                    .getClientNameById(cellData.getValue().getClientId());
            return new javafx.beans.property.SimpleStringProperty(clientName);
        });

        // Set cell value factories for the comment column
        colComment.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getComment()));

        // Customize the cell factory for the comment column to handle long comments
        // using TextFlow
        colComment.setCellFactory(column -> {
            return new TableCell<BookReview, String>() {
                private final TextFlow textFlow = new TextFlow(); // Create TextFlow for the comment

                {
                    textFlow.setMaxWidth(Double.MAX_VALUE); // Ensure TextFlow occupies full width of the cell
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        Text text = new Text(item); // Create Text from the comment
                        text.setWrappingWidth(300); // Set wrapping width to automatically wrap text
                        textFlow.getChildren().clear(); // Clear old elements in TextFlow
                        textFlow.getChildren().add(text); // Add Text to TextFlow
                        setGraphic(textFlow); // Set TextFlow as the graphic of the cell

                        // Calculate the height of TextFlow and adjust the height of the TableCell
                        double height = getTextHeight(item);
                        setPrefHeight(height); // Update the cell's height
                    }
                }

                /**
                 * Calculates the required height for the comment.
                 *
                 * @param comment The comment text.
                 * @return The calculated height.
                 */
                private double getTextHeight(String comment) {
                    Text text = new Text(comment);
                    text.setWrappingWidth(300); // Set wrapping width for automatic line wrapping
                    text.setFont(Font.getDefault()); // Use default font
                    double height = text.getLayoutBounds().getHeight();
                    return Math.max(40, height); // Ensure a minimum height for the cell
                }
            };
        });

        // Set cell value factories for the review date column, formatting the date
        colReviewDate.setCellValueFactory(cellData -> {
            String formattedDate = cellData.getValue().getReviewDate()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });
    }

    /**
     * Converts a numeric rating into a string of star characters.
     *
     * @param rating The numeric rating.
     * @return A string representing the rating in stars.
     */
    private String getStars(double rating) {
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating;
        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        for (int i = fullStars; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
}