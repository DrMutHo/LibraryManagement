package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReportController implements Initializable {
    @FXML
    private TextField briefDesciption;

    @FXML
    private TextArea detailDesciption;

    @FXML
    private ImageView image;

    @FXML
    private ComboBox<String> severity;

    @FXML
    private Button submit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image imagetmp = new Image(getClass().getResource("/Images/bug.png").toExternalForm());
        image.setImage(imagetmp);
    }
}
