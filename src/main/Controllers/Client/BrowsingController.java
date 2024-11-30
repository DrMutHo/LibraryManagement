package main.Controllers.Client;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Models.Book;
import main.Models.BookReview;
import main.Models.Model;

import java.io.IOException;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setAllBook();
        colId.setCellValueFactory(cellData -> cellData.getValue().book_idProperty().asObject());
        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colAuthor.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        colGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        colYear.setCellValueFactory(cellData -> cellData.getValue().publication_yearProperty().asObject());
        colRating.setCellValueFactory(cellData -> cellData.getValue().average_ratingProperty().asObject());
        colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        filteredData = new FilteredList<>(Model.getInstance().getAllBook(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bookTable.comparatorProperty());

        bookTable.setItems(sortedData);

        bookTable.setOnMouseClicked(this::onBookSelect);
    }

    @FXML
    private void onSearch() {
    }

    @FXML
    private void onBookSelect(MouseEvent event) {
        if (event.getClickCount() == 1) {
            selectedBook = bookTable.getSelectionModel().getSelectedItem();
        }
    }

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
        Model.getInstance().setSelectedBook(selectedBook);
    }
}
