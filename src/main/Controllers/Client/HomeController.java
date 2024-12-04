package main.Controllers.Client;

import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Models.Book;
import main.Models.BookRecommendation;
import main.Models.Model;

public class HomeController implements Initializable {
    @FXML
    private HBox highestRatedBooks;
    @FXML
    private HBox Recommended;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private Label bookTitle;
    @FXML
    private TextArea description;
    @FXML
    private HBox readingBook1;
    @FXML
    private Rectangle rec;

    @Override
    /**
     * Initializes the HomeController by setting up the UI with the details of the
     * currently reading book,
     * populating the genre combo box, and updating the list of highest-rated books.
     * This method is called when the FXML view is loaded.
     *
     * @param url            the location used to resolve relative paths for
     *                       resources.
     * @param resourceBundle the resources used for localization.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateRecommendedBook();
        Task<Book> task = new Task<>() {
            @Override
            protected Book call() throws Exception {
                // Thực hiện công việc trong nền (background)
                return Model.getInstance().getReadingBook(); // Lấy thông tin sách đang đọc
            }
        };
        
        // Lắng nghe khi task hoàn thành để cập nhật UI
        task.setOnSucceeded(event -> {
            Book readingBook = task.getValue(); // Lấy kết quả trả về từ Task
        
            // Cập nhật tiêu đề và mô tả sách
            bookTitle.setText(readingBook.getTitle());
            description.setText(readingBook.getDescription());
        
            // Đặt hình ảnh của sách
            rec.setArcWidth(20);
            rec.setArcHeight(20);
            ImagePattern pattern = new ImagePattern(new Image(readingBook.getImagePath()));
            rec.setFill(pattern);
            rec.setStroke(Color.TRANSPARENT);
        
            // Cập nhật danh sách thể loại vào ComboBox
            ObservableList<String> genres = FXCollections.observableArrayList(
                    "All", "Fiction", "Dystopian", "Romance", "Adventure", "Historical", "Fantasy", "Philosophical", "Epic",
                    "Modernist", "Gothic", "Magic Realism", "Existential", "Literature", "War", "Science Fiction");
            genreComboBox.setItems(genres);
        
            // Đặt giá trị mặc định cho ComboBox
            genreComboBox.setValue("All");
            // Cập nhật danh sách sách được đánh giá cao nhất dựa trên thể loại mặc định "All"
            updateHighestRatedBooks("All");
        
            // Thêm listener để cập nhật khi người dùng thay đổi thể loại
            genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateHighestRatedBooks(newValue);
            });
        });
        
        // Lắng nghe lỗi nếu Task gặp vấn đề
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            System.err.println("Đã xảy ra lỗi khi tải dữ liệu sách: " + exception.getMessage());
        });
        
        // Chạy Task trên một Thread riêng
        Thread thread = new Thread(task);
        thread.setDaemon(true); // Đặt thread là daemon để tự động dừng khi ứng dụng đóng
        thread.start();
    }

    /**
     * Adds a book to the highest-rated books display by creating a new card for the
     * book
     * and adding it to the UI. It also sets up event listeners for showing book
     * details when
     * the card or the reading book is clicked.
     *
     * @param book the book to be added to the highest-rated books display.
     */
    private void addBookToHighestRatedBooks(Book book) {
        try {
            // Load the card view for the book from the FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/FXML/Client/Card.fxml"));
            VBox cardBox = loader.load();

            // Get the controller for the card view and set its data
            CardController cardController = loader.getController();
            cardController.setData(book);

            // Apply an effect to open the book card and make the reading book grow on hover
            addOpenBookEffect(cardBox, cardController);
            addReadingBookEffect(readingBook1);

            // Set event listener to show the current reading book's details when clicked
            readingBook1.setOnMouseClicked(event -> showBookDetails(Model.getInstance().getReadingBook()));

            // Set event listener to show the clicked book's details when its card is
            // clicked
            cardBox.setOnMouseClicked(event -> showBookDetails(book));

            // Add the newly created card to the highest rated books display
            highestRatedBooks.getChildren().add(cardBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBookToRecommended(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/FXML/Client/Card.fxml"));
            VBox cardBox = loader.load();

            CardController cardController = loader.getController();
            cardController.setData(book);
            addOpenBookEffect(cardBox, cardController);
            addReadingBookEffect(readingBook1);
            readingBook1.setOnMouseClicked(event -> showBookDetails(Model.getInstance().getReadingBook()));
            cardBox.setOnMouseClicked(event -> showBookDetails(book));

            Recommended.getChildren().add(cardBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a hover effect to the given reading book element. When the mouse enters
     * the book's area,
     * the book will scale up slightly to give a visual cue. When the mouse exits,
     * the book will scale back to its original size.
     * Additionally, the cursor changes to a hand when hovering over the book and
     * resets when the mouse leaves.
     *
     * @param readingBook the HBox containing the reading book element to apply the
     *                    hover effect.
     */
    private void addReadingBookEffect(HBox readingBook) {
        // Create a scale transition to enlarge the reading book on mouse hover
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), readingBook);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);

        // Create a scale transition to revert the reading book to its original size
        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), readingBook);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Set mouse enter event to trigger the scale-up effect and change the cursor to
        // a hand
        readingBook.setOnMouseEntered(event -> {
            scaleUp.playFromStart();
            readingBook.setStyle("-fx-cursor: hand;");
        });

        // Set mouse exit event to trigger the scale-down effect and reset the cursor to
        // default
        readingBook.setOnMouseExited(event -> {
            scaleDown.playFromStart();
            readingBook.setStyle("-fx-cursor: default;");
        });
    }

    /**
     * Adds a hover effect to the book card, where the card scales up when the mouse
     * enters
     * and scales back down when the mouse exits. Additionally, the rating bar
     * becomes visible
     * when hovering over the card and is hidden when the mouse leaves the card.
     *
     * @param cardBox        the VBox containing the book card to apply the hover
     *                       effect.
     * @param cardController the controller for the book card, used to manage the
     *                       rating bar visibility.
     */
    private void addOpenBookEffect(VBox cardBox, CardController cardController) {
        // Create a scale transition to enlarge the book card on mouse hover
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), cardBox);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);

        // Create a scale transition to revert the book card to its original size
        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), cardBox);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Set mouse enter event to trigger the scale-up effect and show the rating bar
        cardBox.setOnMouseEntered(event -> {
            scaleUp.playFromStart();
            cardController.ratingBar.setVisible(true);
        });

        // Set mouse exit event to trigger the scale-down effect and hide the rating bar
        cardBox.setOnMouseExited(event -> {
            scaleDown.playFromStart();
            cardController.ratingBar.setVisible(false);
        });
    }

    /**
     * Sets the given book as the selected book in the model. This method is called
     * when a user
     * clicks on a book card to view its details.
     *
     * @param book the book to be set as the selected book in the model.
     */
    private void showBookDetails(Book book) {
        Model.getInstance().setSelectedBook(book);
    }

    /**
     * Updates the list of highest-rated books based on the selected genre. This
     * method clears
     * the current list of books and populates it with the books that match the
     * selected genre.
     *
     * @param genre the genre to filter the highest-rated books by.
     */
    private void updateHighestRatedBooks(String genre) {
        // Clear the current list of highest-rated books
        highestRatedBooks.getChildren().clear();

        // Set the highest-rated books in the model based on the selected genre
        Task<Void> task = new Task<Void>() {
            protected Void call() throws Exception {
                Model.getInstance().setHighestRatedBooks(genre);
                return null;
            }
        };
        task.setOnSucceeded(event -> { 
            for (Book book : Model.getInstance().getHighestRatedBook()) {
                addBookToHighestRatedBooks(book);
            }
        });
        new Thread(task).start();

    }
    private void updateRecommendedBook() {
        Recommended.getChildren().clear();
        BookRecommendation bookRecommendation = new BookRecommendation();
        Task<List<Book>> recommendationTask = new Task<>() {
            @Override
            protected List<Book> call() throws Exception {
                // Thực hiện công việc nặng ở đây
                return bookRecommendation.Recommendation();
            }
        };
        
        // Xử lý khi Task thành công
        recommendationTask.setOnSucceeded(event -> {
            List<Book> dak = recommendationTask.getValue();
            // Cập nhật UI hoặc xử lý dữ liệu tại đây
            // Ví dụ: cập nhật TableView, ListView, v.v.
            for (Book book : dak) {
            addBookToRecommended(book);
        }
        });
        new Thread(recommendationTask).start();
    }
}
