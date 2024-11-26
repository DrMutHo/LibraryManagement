package main.Controllers.Client;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private Button changePasswordButton;
    @FXML
    private Button changeEmailButton;
    @FXML
    private Button deleteAccountButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(Model.getInstance().getClient().getAvatarImagePath());
        profileImageView.setImage(image);
        labelLibraryCardNumber.setText(Model.getInstance().getClient().getLibraryCardNumber());
        labelFullName.setText(Model.getInstance().getClient().getName());
        labelUsername.setText(Model.getInstance().getClient().getUsername());
        labelEmail.setText(Model.getInstance().getClient().getEmail());
        labelPhoneNumber.setText(Model.getInstance().getClient().getPhoneNumber());
        labelAddress.setText(Model.getInstance().getClient().getAddress());
        labelFee.setText(String.valueOf(Model.getInstance().getClient().getOutstandingFees()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(Model.getInstance().getClient().getRegistrationDate());
        labelRegistrationDate.setText(formattedDate);
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