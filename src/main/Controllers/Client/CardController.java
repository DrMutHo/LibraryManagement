package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import main.Models.Book;

public class BookController implements Initializable {
    @FXML
    private Image image;

    @FXML
    private Label title;

    @FXML
    private Label author;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Book book) {
        Image bookCoverImage = new Image(book.getImage_url(), true);
        image = bookCoverImage;
        title.setText("Title: " + book.getTitle());
        author.setText("Author " + book.getAuthor());
    }
}
