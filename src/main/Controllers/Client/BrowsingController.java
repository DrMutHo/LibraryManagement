package main.Controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;
import main.Models.Book;
import main.Models.Model;

public class BrowsingController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> colTitle, colAuthor, colGenre;
    @FXML
    private TableColumn<Book, Integer> colYear;
    @FXML
    private TableColumn<Book, Double> colRating;
    @FXML
    private ImageView bookImageView;
    @FXML
    private Label labelTitle, labelAuthor, labelISBN, labelGenre, labelLanguage, labelPublicationYear,
            labelAverageRating, labelReviewCount;
    @FXML
    private TextArea textDescription;

    public void initialize() {
        Model.getInstance().setAllBook();

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("averageRating"));

        bookTable.setItems(Model.getInstance().getAllBook());
    }

    @FXML
    private void onSearch() {
        String keyword = searchField.getText().toLowerCase();
        ObservableList<Book> filteredList = Model.getInstance().getAllBook()
                .filtered(book -> book.getTitle().toLowerCase().contains(keyword) ||
                        book.getAuthor().toLowerCase().contains(keyword));
        bookTable.setItems(filteredList);
    }

    @FXML
    private void onBookSelect(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                displayBookDetails(selectedBook);
            }
        }
    }

    private void displayBookDetails(Book book) {
        labelTitle.setText(book.getTitle());
        labelAuthor.setText(book.getAuthor());
        labelISBN.setText(book.getIsbn());
        labelGenre.setText(book.getGenre());
        labelLanguage.setText(book.getLanguage());
        labelPublicationYear.setText(String.valueOf(book.getPublication_year()));
        labelAverageRating.setText(String.format("%.2f", book.getAverage_rating()));
        labelReviewCount.setText(String.valueOf(book.getReview_count()));
        textDescription.setText(book.getDescription());

        if (book.getImage_path() != null) {
            Image image = new Image(getClass().getResourceAsStream(book.getImage_path()));
            ;
            bookImageView.setImage(image);
        } else {
            bookImageView.setImage(null);
        }
    }
}
