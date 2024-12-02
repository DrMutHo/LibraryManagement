package main.Controllers.Client;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Models.Book;
import main.Models.BookReview;
import main.Models.BorrowTransaction;
import main.Models.Model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.Mode;

public class BorrowTransactionController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<BorrowTransaction> bookTable;

    @FXML
    private TableColumn<BorrowTransaction, Integer> transactionIdColumn;
    @FXML
    private TableColumn<BorrowTransaction, String> titleColumn;
    @FXML
    private TableColumn<BorrowTransaction, Integer> copyIdColumn;
    @FXML
    private TableColumn<BorrowTransaction, LocalDate> borrowDateColumn;
    @FXML
    private TableColumn<BorrowTransaction, LocalDate> returnDateColumn;
    @FXML
    private TableColumn<BorrowTransaction, String> statusColumn;

    @FXML
    private ImageView bookImageView;

    @FXML
    private Label labelTitle, labelAuthor, labelISBN, labelGenre, labelLanguage, labelPublicationYear,
            labelAverageRating, labelReviewCount;

    @FXML
    private TextArea textDescription;
    @FXML
    private HBox ratingStars;

    private FilteredList<BorrowTransaction> filteredData;
    private SortedList<BorrowTransaction> sortedData;

    private BorrowTransaction selectedTransaction;
    private Book selectedBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load data and set up table columns
        Model.getInstance().setBorrowTransaction();
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Apply filtering and sorting
        filteredData = new FilteredList<>(Model.getInstance().getBorrowTransaction(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(transaction -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (transaction.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedData);

        initializeRatingStars();

        bookTable.setOnMouseClicked(this::onTransactionSelect);
    }

    @FXML
    private void onSearch() {
    }

    @FXML
    private void onTransactionSelect(MouseEvent event) {
        if (event.getClickCount() == 1) {
            selectedTransaction = bookTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null) {
                this.selectedBook = Model.getInstance().getBookDataByCopyID(selectedTransaction.getCopyId());
                displayTransactionDetails(selectedBook);
            }
        }
    }

    private void initializeRatingStars() {
        ratingStars.getChildren().clear();
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("☆");
            star.setStyle("-fx-font-size: 24; -fx-text-fill: gold;");
            final int starValue = i;

            star.setOnMouseClicked(event -> {
                if (selectedBook != null) {
                    rateBook(selectedBook, starValue);
                }
            });

            star.setOnMouseEntered(event -> {
                for (int j = 0; j < starValue; j++) {
                    ((Label) ratingStars.getChildren().get(j)).setText("★");
                }
                for (int j = starValue; j < 5; j++) {
                    ((Label) ratingStars.getChildren().get(j)).setText("☆");
                }
            });

            star.setOnMouseExited(event -> {
                updateRatingStarsBasedOnUserReview(selectedBook);
            });

            ratingStars.getChildren().add(star);
        }
    }

    private void updateRatingStarsBasedOnUserReview(Book book) {
        if (book == null)
            return;

        int userRating = 0;
        BookReview userReview = Model.getInstance().getDatabaseDriver().getUserReview(book.getBook_id(),
                Model.getInstance().getClient().getClientId());
        if (userReview != null && userReview.getRating() != 0) {
            userRating = (int) Math.round(userReview.getRating());
        }

        for (int i = 0; i < 5; i++) {
            Label star = (Label) ratingStars.getChildren().get(i);
            if (i < userRating) {
                star.setText("★");
            } else {
                star.setText("☆");
            }
        }
    }

    private void rateBook(Book book, int rating) {
        if (book == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể đánh giá một cuốn sách không tồn tại.");
            alert.showAndWait();
            return;
        }

        BookReview existingReview = Model.getInstance().getDatabaseDriver().getUserReview(
                book.getBook_id(),
                Model.getInstance().getClient().getClientId());
        boolean isNewReview = (existingReview == null);

        boolean success = Model.getInstance().getDatabaseDriver().upsertBookReview(
                book.getBook_id(),
                Model.getInstance().getClient().getClientId(),
                (double) rating,
                null);

        if (success) {
            double newAverageRating = calculateNewAverageRating(book);
            book.setAverage_rating(newAverageRating);

            if (isNewReview) {
                book.setReview_count(book.getReview_count() + 1);
            }

            if (selectedBook != null && selectedBook.getBook_id() == book.getBook_id()) {
                displayTransactionDetails(book);
            }

            updateRatingStarsBasedOnUserReview(book);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Đánh Giá Thành Công");
            alert.setHeaderText(null);
            alert.setContentText("Cảm ơn bạn đã đánh giá \"" + book.getTitle() + "\" với " + rating + " sao.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể lưu đánh giá. Vui lòng thử lại sau.");
            alert.showAndWait();
        }
    }

    private double calculateNewAverageRating(Book book) {
        int totalReviews = Model.getInstance().getDatabaseDriver().getReviewCount(book.getBook_id());
        double sumRatings = Model.getInstance().getDatabaseDriver().getSumRatings(book.getBook_id());
        if (totalReviews == 0)
            return 0.0;
        return sumRatings / totalReviews;
    }

    private void displayTransactionDetails(Book book) {
        labelTitle.textProperty().bind(Bindings.concat("Title: ", book.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat("Author: ", book.getAuthor()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ", book.getGenre()));
        textDescription.setText(book.getDescription());

        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(book.getImagePath()));
                bookImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Image not found: " + book.getImagePath());
                bookImageView.setImage(null);
            }
        } else {
            bookImageView.setImage(null);
        }
    }

    @FXML
    private void exportExcel() {
        File dir = new File("D:/javaaa/oop/");

        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                System.out.println("Failed to create directory: " + dir.getAbsolutePath());
                return;
            }
        }

        if (!dir.canWrite()) {
            System.out.println("No write permission for the directory: " + dir.getAbsolutePath());
            return;
        }

        String filePath = "D:/javaaa/oop/borrow_transactions.xlsx";
        try {
            Model.getInstance().exportClientBorrowTransactionsToExcel(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exporting to Excel: " + e.getMessage());
        }
    }

    @FXML
    private void openDetailWindow() {
        if (selectedTransaction == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Transaction Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a transaction to view details.");
            alert.showAndWait();
            return;
        }
        Model.getInstance().setSelectedBook(selectedBook);
    }
}
