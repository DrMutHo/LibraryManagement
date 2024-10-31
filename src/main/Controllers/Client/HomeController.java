package main.Controllers.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Models.Book;
import main.Models.Model;

public class HomeController implements Initializable {
    @FXML
    private HBox recentlyAddBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setAllBook();

        for (Book book : Model.getInstance().getAllBook()) {
            addBookToRecentlyAdded(book);
        }
    }

    private void addBookToRecentlyAdded(Book book) {
        try {
            // Tải FXML của CardController
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/FXML/Client/Card.fxml"));
            VBox cardBox = loader.load();

            // Lấy controller của Card và thiết lập dữ liệu
            CardController cardController = loader.getController();
            cardController.setData(book);

            // Thêm thẻ sách vào HBox
            recentlyAddBook.getChildren().add(cardBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
