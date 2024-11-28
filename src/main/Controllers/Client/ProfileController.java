package main.Controllers.Client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import main.Models.Model;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class ProfileController implements Initializable {
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label labelLibraryCardNumber, labelFullName, labelUsername, labelEmail, labelPhoneNumber,
            labelAddress,
            labelFee, labelRegistrationDate;
    @FXML
    private Button deleteAccountButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void changeProfileImageView(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn Ảnh Đại Diện");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tệp Hình Ảnh", ".png", ".jpg", ".jpeg", ".gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            profileImageView.setImage(image);
        }
    }
}