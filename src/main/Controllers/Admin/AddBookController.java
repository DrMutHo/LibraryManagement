package main.Controllers.Admin;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Models.GoogleBooksAPI;
import main.Models.Book;

/**
 * Controller class for adding books in the admin panel.
 * Handles searching for books using the Google Books API and displaying results.
 */
public class AddBookController {

    /** TextField for entering search query */
    @FXML
    private TextField searchEdt;

    /** Button to initiate search */
    @FXML
    private Button searchBtn;

    /** VBox to hold search results */
    @FXML
    private VBox bookContainer;

    /** ObservableList to hold book information */
    private ObservableList<Book> bookInfoList = FXCollections.observableArrayList();

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up event handlers and initial configurations.
     */
    @FXML
    public void initialize() {
        // Set the search button's action
        searchBtn.setOnAction(event -> searchBooks());
    }

    /**
     * Performs a search for books using the query from the search text field.
     * Fetches data from GoogleBooksAPI and displays the results in the book container.
     */
    private void searchBooks() {
        String query = searchEdt.getText().trim();

        // Input validation
        if (query.isEmpty()) {
            searchEdt.setStyle("-fx-border-color: red;");
            return;
        }

        // Clear previous results
        bookContainer.getChildren().clear();

        try {
            // Fetch books from the API
            List<Book> books = GoogleBooksAPI.ParseData(query);

            // Check if books were found
            if (books.isEmpty()) {
                showError("No books found.");
            } else {
                // Create and display each book's information as a card using CardController
                for (Book book : books) {
                    // Use FXMLLoader to load the FXML for the book card
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/resources/Fxml/Admin/HboxBookCard.fxml"));
                    HBox card = loader.load(); // Load the FXML and get the HBox (card)

                    // Get the controller and set the book data
                    CardController2 cardController = loader.getController();

                    if (cardController != null) {
                        cardController.setBookData(book);
                    } else {
                        showError("Failed to load card controller.");
                    }

                    // Add the card to the container
                    bookContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            // Show error if an issue occurs
            e.printStackTrace();
            showError("Failed to fetch data: sieu dak" + e.getMessage());
        }
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
