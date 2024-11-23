package main.Controllers.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import main.Models.Book;
import main.Models.Model;

public class HomeController implements Initializable {
    @FXML
    private HBox highestRatedBooks;
    @FXML
    private Label welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setHighestRatedBook();
        welcome.setText("Hello, " + Model.getInstance().getClient().getUsername());

        for (Book book : Model.getInstance().getHighestRatedBook()) {
            addBookToHighestRatedBooks(book);
        }
    }

    private void addBookToHighestRatedBooks(Book book) {
        try {
            // Tải FXML của CardController
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/FXML/Client/Card.fxml"));
            VBox cardBox = loader.load();

            // Lấy controller của Card và thiết lập dữ liệu
            CardController cardController = loader.getController();
            cardController.setData(book);
            addOpenBookEffect(cardBox);
            // Thêm thẻ sách vào HBox
            highestRatedBooks.getChildren().add(cardBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addOpenBookEffect(VBox cardBox) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), cardBox);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), cardBox);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        cardBox.setOnMouseEntered(event -> scaleUp.playFromStart());

        cardBox.setOnMouseExited(event -> scaleDown.playFromStart());
    }
}
