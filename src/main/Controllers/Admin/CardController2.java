package main.Controllers.Admin;

import com.sun.jdi.IntegerType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import main.Models.Book;
import java.net.URL;
import main.Models.Model;

public class CardController2 implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private ImageView bookImage;
    @FXML
    private Label isbnLabel;
    @FXML
    private Button AddBook;
    @FXML
    private TextField Quantity;
    @FXML
    private VBox vBoxIsbnAndTitle;

    @FXML
    private VBox vBoxQuantityAndButton;

    @FXML
    private HBox card;
    private Book currentBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox.setHgrow(vBoxIsbnAndTitle, Priority.ALWAYS);

        vBoxQuantityAndButton.setStyle("-fx-alignment: CENTER_RIGHT;");
        currentBook = new Book();
    }

    public void setBookData(Book book) {
        this.currentBook = book;
        String imagePath = book.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            bookImage.setImage(new Image(imagePath));
        } else {
            bookImage.setImage(new Image("/resources/Images/default.png"));
        }

        if (isbnLabel != null) {
            isbnLabel.setText("ISBN: " + book.getIsbn());
        }

        if (titleLabel != null) {
            titleLabel.setText(book.getTitle());
        }
    }

    public void AddBookCTL() {
        try {
            // Lấy số lượng từ trường Quantity
            int quantity = Integer.parseInt(Quantity.getText());

            if(quantity == 0) {
                showError("quantity must be greater than 0");
                return;
            }

            // Gọi phương thức trong Model để thêm sách và số lượng
            Model.getInstance().AddBookCTL(currentBook, quantity);

            // Gọi phương thức notify để thông báo thêm sách
            Model.getInstance().notifyAddBookEvent();

            // Hiển thị thông báo thành công
            showSuccess("Book added successfully with " + quantity + " copies.");
        } catch (NumberFormatException e) {
            // Xử lý nếu số lượng nhập vào không phải là số hợp lệ
            showError("Please enter a valid quantity.");
        } catch (Exception e) {
            // Xử lý các lỗi khác
            e.printStackTrace();
            showError("An error occurred while adding the book.");
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Success");
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public HBox getCard() {
        return card;
    }
}
