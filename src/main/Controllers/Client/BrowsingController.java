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
    private TableColumn<Book, Integer> colYear, colId;
    @FXML
    private TableColumn<Book, Double> colRating;
    @FXML
    private ImageView bookImageView;
    @FXML
    private Label labelTitle, labelAuthor, labelISBN, labelGenre, labelLanguage, labelPublicationYear,
            labelAverageRating, labelReviewCount;
    @FXML
    private TextArea textDescription;
    @FXML
    private HBox ratingStars;

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

        initializeRatingStars();

        bookTable.setOnMouseClicked(this::onBookSelect);
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
                displayBookDetails(book);
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

    @FXML
    private void onSearch() {
    }

    @FXML
    private void onBookSelect(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                this.selectedBook = selectedBook;
                displayBookDetails(selectedBook);
                updateRatingStarsBasedOnUserReview(selectedBook);
            }
        }
    }

    private void displayBookDetails(Book book) {
        labelTitle.textProperty().bind(Bindings.concat("Title: ", book.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat("Author: ", book.authorProperty()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ", book.genreProperty()));
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
