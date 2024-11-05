package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Models.Book;

public class CardController implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private Label title;

    @FXML
    private Label author;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Book book) {
        String image_path = getClass().getResource(book.getImagePath()).toExternalForm();
        Image bookCoverImage = new Image(image_path, true);
        imageView.setImage(bookCoverImage);
        title.setText("Title: " + book.getTitle());
        author.setText("Author " + book.getAuthor());
    }
}
