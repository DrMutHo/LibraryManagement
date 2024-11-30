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
        labelPublicationYear.setText(String.valueOf(book.getPublicationYear()));
        labelAverageRating.setText(String.format("%.2f", book.getAverageRating()));
        labelReviewCount.setText(String.valueOf(book.getReviewCount()));
        textDescription.setText(book.getDescription());

        if (book.getImagePath() != null) {
            Image image = new Image(getClass().getResourceAsStream(book.getImagePath()));
            bookImageView.setImage(image);
        } else {
            bookImageView.setImage(null);
        }

        updateRatingStarsBasedOnUserReview(book);
    }

    @FXML
    private void openDetailWindow() {
        if (selectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chưa Chọn Sách");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một cuốn sách để xem chi tiết.");
            alert.showAndWait();
            return;
        }   

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/Fxml/Client/BookDetailWithReview.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Chi Tiết Sách");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            BookDetailWithReviewController controller = loader.getController();
            controller.setBook(selectedBook);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể mở cửa sổ chi tiết sách.");
            alert.showAndWait();
        }
    }

    @FXML
    private void openReviewWindow() {
        if (selectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chưa Chọn Sách");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một cuốn sách để viết đánh giá.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/Fxml/Client/BookDetailWithReview.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Viết Đánh Giá");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            BookDetailWithReviewController controller = loader.getController();
            controller.setBook(selectedBook);

            stage.showAndWait();

            Model.getInstance().setAllBook();
            displayBookDetails(selectedBook);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể mở cửa sổ viết đánh giá.");
            alert.showAndWait();
        }
    }
}
