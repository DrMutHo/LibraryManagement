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
import javafx.stage.DirectoryChooser;
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

    /**
     * Initializes the controller by setting up table columns, applying filters,
     * sorting the transactions,
     * and setting event listeners. This method also initializes the rating stars
     * for book rating interactions.
     * 
     * <p>
     * This method is called during the initialization phase of the controller. It
     * performs the following tasks:
     * </p>
     * <ul>
     * <li>Loads the borrow transactions data from the model and populates the table
     * columns.</li>
     * <li>Sets up a text filter on the search field to filter transactions by book
     * title.</li>
     * <li>Applies sorting to the filtered data, ensuring the table content remains
     * up-to-date with user inputs.</li>
     * <li>Initializes the rating stars for book rating interactions.</li>
     * <li>Sets up an event listener for selecting transactions when a row in the
     * table is clicked.</li>
     * </ul>
     *
     * <p>
     * It is assumed that the necessary data and table columns are already
     * configured in the view (FXML).
     * </p>
     * 
     * @param url            the location used to resolve relative paths for the
     *                       root object, or null if the location is not known.
     * @param resourceBundle the resource bundle used to localize the controller, or
     *                       null if no resources are available.
     */
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

    /**
     * Handles the search action. Filters the borrow transactions based on the
     * entered text in the search field.
     */
    @FXML
    private void onSearch() {
        // The search functionality is handled by the listener on the search field
        // textProperty.
    }

    /**
     * Handles the event when a transaction is selected in the table. This method is
     * triggered when the user clicks on a row in the table.
     * It displays the details of the selected book from the transaction.
     * 
     * <p>
     * The method performs the following actions:
     * </p>
     * <ul>
     * <li>Checks if the click count is 1 (single click) to avoid handling multiple
     * clicks.</li>
     * <li>Retrieves the selected transaction from the table.</li>
     * <li>If a transaction is selected, it fetches the associated book details
     * based on the book copy ID.</li>
     * <li>Displays the details of the selected book, such as title, borrow status,
     * etc.</li>
     * </ul>
     *
     * <p>
     * This method assumes that the transaction data in the table includes a
     * reference to the book copy, and that
     * a method to fetch the book details by its copy ID is available in the model.
     * </p>
     *
     * @param event the mouse event triggered by clicking on a row in the table.
     */
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

    /**
     * Initializes the rating stars for rating the selected book. This method sets
     * up the UI elements (stars)
     * and assigns event handlers for user interaction with the stars. The stars
     * represent a rating system
     * where users can hover over and click to set a rating for the selected book.
     * 
     * <p>
     * The method performs the following actions:
     * </p>
     * <ul>
     * <li>Clears any existing content from the rating stars container.</li>
     * <li>Generates 5 stars (using Label elements) and adds them to the
     * container.</li>
     * <li>Assigns mouse event handlers for each star to handle user
     * interaction.</li>
     * <li>When a user clicks on a star, the rating is recorded for the selected
     * book.</li>
     * <li>When a user hovers over a star, the stars are visually updated to show
     * the current rating.</li>
     * <li>When a user stops hovering, the stars are reset to reflect the current
     * rating of the book.</li>
     * </ul>
     * 
     * <p>
     * This method assumes that the stars container (`ratingStars`) is a UI
     * component (e.g., a `HBox` or `VBox`)
     * and that the book being rated is stored in the `selectedBook` variable.
     * </p>
     * 
     * <p>
     * The method also invokes the following methods:
     * </p>
     * <ul>
     * <li><code>rateBook</code> to save the user's rating when a star is
     * clicked.</li>
     * <li><code>updateRatingStarsBasedOnUserReview</code> to reset the stars when
     * the hover action ends.</li>
     * </ul>
     */
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

    /**
     * Updates the rating stars based on the user's review for the selected book.
     * This method is called to visually reflect the user's rating for the book
     * when the stars are hovered or the view is initialized.
     * 
     * <p>
     * The method performs the following steps:
     * </p>
     * <ul>
     * <li>Checks if the provided book is valid (non-null).</li>
     * <li>Retrieves the user's review for the book from the database using the
     * client ID and book ID.</li>
     * <li>If the review exists and the rating is non-zero, it updates the stars to
     * match the user's rating.</li>
     * <li>Displays filled stars (★) for the rating value and empty stars (☆) for
     * the remainder.</li>
     * </ul>
     * 
     * <p>
     * If no review exists for the user or if the rating is zero, the stars will be
     * displayed as empty (☆) by default.
     * </p>
     * 
     * @param book The book whose rating stars need to be updated. This parameter
     *             cannot be null.
     */
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

    /**
     * Rates the selected book based on the user's input and updates the book's
     * rating information.
     * 
     * <p>
     * This method performs the following:
     * </p>
     * <ul>
     * <li>Checks if the book is valid (non-null).</li>
     * <li>Retrieves the user's existing review for the book (if any) and determines
     * whether this is a new review.</li>
     * <li>Upserts (inserts or updates) the user's rating for the book in the
     * database.</li>
     * <li>If the rating is successfully saved, it updates the book's average rating
     * and review count.</li>
     * <li>If the book is selected, it refreshes the displayed transaction details
     * with the updated rating.</li>
     * <li>Displays a success message or an error message based on the result of the
     * rating operation.</li>
     * </ul>
     * 
     * @param book   The book being rated. Must not be null.
     * @param rating The rating given by the user (1 to 5).
     */
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

    /**
     * Calculates the new average rating for a book after a review is added or
     * updated.
     * 
     * <p>
     * This method retrieves the total number of reviews and the sum of all ratings
     * for the given book,
     * then calculates and returns the new average rating.
     * </p>
     * 
     * <p>
     * If there are no reviews, it returns 0.0 as the average rating.
     * </p>
     * 
     * @param book The book whose average rating needs to be calculated. Must not be
     *             null.
     * @return The new average rating for the book.
     */
    private double calculateNewAverageRating(Book book) {
        int totalReviews = Model.getInstance().getDatabaseDriver().getReviewCount(book.getBook_id());
        double sumRatings = Model.getInstance().getDatabaseDriver().getSumRatings(book.getBook_id());
        if (totalReviews == 0)
            return 0.0;
        return sumRatings / totalReviews;
    }

    /**
     * Displays the details of a selected book in the user interface.
     * 
     * <p>
     * This method updates the UI components with information about the book, such
     * as its title,
     * author, genre, description, and cover image.
     * </p>
     * <ul>
     * <li>Sets the title, author, and genre labels using data bound properties from
     * the book object.</li>
     * <li>Sets the description text field with the book's description.</li>
     * <li>If the book has an associated image path, it tries to load and display
     * the book's cover image.</li>
     * <li>If the image is not found or the path is invalid, the image is set to
     * null.</li>
     * </ul>
     * 
     * @param book The book whose details need to be displayed. Must not be null.
     */
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

    /**
     * Exports the client's borrow transaction data to an Excel file.
     * 
     * <p>
     * This method checks if the directory for storing the Excel file exists. If
     * not, it attempts to
     * create it. It also verifies if the directory has write permissions. If
     * everything is valid,
     * it calls the model to export the transaction data to an Excel file.
     * </p>
     * 
     * <p>
     * In case of any errors during the export process, an exception is caught and
     * printed to
     * the console.
     * </p>
     * 
     * @see Model#getInstance().exportClientBorrowTransactionsToExcel(String)
     */
    @FXML
    private void exportExcel() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chọn Thư Mục Lưu Tệp");

        // Mở cửa sổ chọn thư mục và lấy thư mục người dùng chọn
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            try {
                // Tạo đường dẫn tệp (tên tệp có thể cố định hoặc lấy từ dữ liệu)
                String filePath = selectedDirectory.getAbsolutePath() + "/borrow_transactions.xlsx";

                // Gọi hàm export dữ liệu vào file đã chọn
                Model.getInstance().exportClientBorrowTransactionsToExcel(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error exporting to Excel: " + e.getMessage());
            }
        } else {
            System.out.println("No directory selected.");
        }
    }

    /**
     * Opens the detailed view of the selected book transaction.
     * 
     * <p>
     * This method checks whether a transaction is selected. If no transaction is
     * selected,
     * it displays a warning alert asking the user to select a transaction. If a
     * transaction is
     * selected, the method sets the selected book in the model for further
     * processing (e.g.,
     * opening a detailed view).
     * </p>
     * 
     * @see Model#getInstance().setSelectedBook(Book)
     */
    @FXML
    private void openDetailWindow() {
        // Check if a transaction has been selected
        if (selectedTransaction == null) {
            // Display a warning alert if no transaction is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Transaction Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a transaction to view details.");
            alert.showAndWait();
            return;
        }

        // Set the selected book for further processing
        Model.getInstance().setSelectedBook(selectedBook);
    }
}
