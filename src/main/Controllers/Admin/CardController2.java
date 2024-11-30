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
import javafx.scene.control.Label;
import main.Models.Book;
import java.net.URL;
import main.Models.Model;

public class CardController2 implements Initializable{

    @FXML public HBox card;  // This will be injected by FXML
    @FXML private Label titleLabel;
    @FXML private ImageView bookImage;
    @FXML private Label isbnLabel;
    @FXML private Button AddBook;
    @FXML private TextField Quantity;
    private Book book2;

     @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.book2 = new Book();
    }

    // This method will be used to set the book data into the UI components
    public void setBookData(Book book) {
        this.book2 = book;
        if (bookImage != null) {
            // Set image URL here (ensure book.getImageUrl() is a valid URL or path)
            bookImage.setImage(new Image(book.getImagePath()));
        }

        if (isbnLabel != null) {
            isbnLabel.setText("ISBN: " + book.getIsbn());
        }

        if (titleLabel != null) {
            titleLabel.setText(book.getTitle());
        }
    }

    public void AddBookCTL(){
       try {
            int dak = Model.getInstance().getDatabaseDriver().addBook2(this.book2);

            showError(book2.getTitle());

            String sss = String.valueOf(dak);

            showError(sss);
    
            int chim = Integer.parseInt(Quantity.getText());
        
            for(int i = 0; i < chim; ++i){
                Model.getInstance().getDatabaseDriver().addBookCopy(dak, true, "Good");
            }
       } catch (Exception e) {
            e.printStackTrace();
       }
    }

    private void showError(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
    }

    // Get the HBox (card) to be added to the UI
    public HBox getCard() {
        return card;
    }

    // Initialize method for any setup actions (optional)
    
}
