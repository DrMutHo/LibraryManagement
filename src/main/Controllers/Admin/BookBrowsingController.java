package main.Controllers.Admin;

import java.awt.Button;
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
import main.Controllers.Admin.BookDetailWithReviewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookBrowsingController implements Initializable, Model.ModelListenerAdmin {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> colTitle, colAuthor, colGenre;
    @FXML
    private TableColumn<Book, Integer> colYear, colQuantity;
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

    private Button addBookButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeBookTable();
    }

    @Override
    public void onBorrowTransactionAdminCreated() {
        initializeBookTable();
    }

    @Override
    public void onBookReturnProcessed() {
        initializeBookTable();
    }

    @Override
    public void onAddBook() {
        initializeBookTable();
    }

    public void initializeBookTable() {
        Model.getInstance().setAllBook();
        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colAuthor.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        colGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        colYear.setCellValueFactory(cellData -> cellData.getValue().publication_yearProperty().asObject());
        colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        colYear.setCellFactory(column -> new TableCell<Book, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == -1) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                }
            }
        });
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

        bookTable.setOnMouseClicked(this::onBookSelect);
    }

    private void initializeRatingStars() {
        ratingStars.getChildren().clear();
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("☆");
            star.setStyle("-fx-font-size: 24; -fx-text-fill: gold;");
            ratingStars.getChildren().add(star);
        }

        int starValue = (int) selectedBook.getAverage_rating();
        for (int j = 0; j < starValue; j++) {
            ((Label) ratingStars.getChildren().get(j)).setText("★");
        }
        for (int j = starValue; j < 5; j++) {
            ((Label) ratingStars.getChildren().get(j)).setText("☆");
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
                initializeRatingStars();
            }
        }
    }

    @FXML
    private void openEditWindow() {
        if (selectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chưa Chọn Sách");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a book to edit.");
            alert.showAndWait();
            return;
        }

        try {
            // Load the FXML file for the new window
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/Fxml/Admin/BookEdit.fxml"));

            // Create a new Stage (Window)
            Stage stage = new Stage();
            stage.setTitle("Danh Sách Người Mượn Sách");

            // Set the scene for the stage
            stage.setScene(new Scene(loader.load()));

            // Get the controller and pass the selectedBook to it
            BookEdit controller = loader.getController();
            controller.setBook(selectedBook);

            // Show the new window without blocking the current one
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Can't open book editor.");
            alert.showAndWait();
        }
    }

    private void displayBookDetails(Book book) {
        labelTitle.textProperty().bind(Bindings.concat("Title: ", book.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat("Author: ", book.authorProperty()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ", book.genreProperty()));
        textDescription.setText(book.getDescription());

        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            try {
                Image image = new Image(book.getImagePath());
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
    private void openDetailWindow() {
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

    @FXML
    private void addBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Admin/AddBook.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Thêm Sách");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            AddBookController controller = loader.getController();

            stage.showAndWait();

            Model.getInstance().setAllBook();
            bookTable.refresh();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể mở cửa sổ thêm sách.");
            alert.showAndWait();
        }
    }

}