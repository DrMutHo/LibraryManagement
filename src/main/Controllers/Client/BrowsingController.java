package main.Controllers.Client;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.Models.Book;
import main.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowsingController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> colTitle, colAuthor, colGenre;
    @FXML
    private TableColumn<Book, Integer> colYear, colId, colQuantity;
    @FXML
    private TableColumn<Book, Double> colRating;

    private Book selectedBook;

    private FilteredList<Book> filteredData;
    private SortedList<Book> sortedData;

    /**
     * Initializes the controller by setting up table columns, applying filters,
     * sorting the books, and setting event listeners for the table and search field.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load all books from the model
        Model.getInstance().setAllBook();

        // Set cell value factories for each table column
        colId.setCellValueFactory(cellData -> cellData.getValue().book_idProperty().asObject());
        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colAuthor.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        colGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        colYear.setCellValueFactory(cellData -> cellData.getValue().publication_yearProperty().asObject());
        colRating.setCellValueFactory(cellData -> cellData.getValue().average_ratingProperty().asObject());
        colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        // Set up filtering for the search field
        filteredData = new FilteredList<>(Model.getInstance().getAllBook(), p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Filter by book title or author
                return book.getTitle().toLowerCase().contains(lowerCaseFilter) || 
                       book.getAuthor().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Set up sorting for the table
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedData);

        // Add click event handler to the table
        bookTable.setOnMouseClicked(this::onBookSelect);
    }

    /**
     * This method is called when the search button is clicked.
     * The search functionality is implemented in the listener for the search field.
     */
    @FXML
    private void onSearch() {
        // The search logic is handled in the searchField textProperty listener
    }

    /**
     * Handles the event when a book is selected in the table.
     * Stores the selected book to be used later for viewing its details.
     * 
     * @param event The MouseEvent triggered by clicking on a book in the table.
     */
    @FXML
    private void onBookSelect(MouseEvent event) {
        if (event.getClickCount() == 1) {
            selectedBook = bookTable.getSelectionModel().getSelectedItem();
        }
    }

    /**
     * Opens a new window or view to display the details of the selected book.
     * If no book is selected, an alert is shown to prompt the user to select a book.
     */
    @FXML
    private void onViewDetails() {
        if (selectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chưa Chọn Sách");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một cuốn sách để xem chi tiết.");
            alert.showAndWait();
            return;
        }
        // Set the selected book to be viewed in another part of the application
        Model.getInstance().setSelectedBook(selectedBook);
    }
}
