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
import javafx.scene.layout.AnchorPane;
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
    private Button changePasswordButton;
    @FXML
    private Button changeEmailButton;
    @FXML
    private Button deleteAccountButton;
    @FXML
    private AnchorPane changePasswordPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Image image = new Image(Model.getInstance().getClient().getAvatarImagePath());
        //profileImageView.setImage(image);
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
        changePasswordPane = null;
        changePasswordButton.setOnAction(event -> changePassword());
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

    @FXML
<<<<<<< Updated upstream
    private void changePassword() { 
        // Cập nhật kích thước của StackPane sao cho phù hợp với ChangePasswordView
        changePasswordPane = Model.getInstance().getViewFactory().getChangePasswordView();
        if (changePasswordPane != null) {
            changePasswordPane.setVisible(true);
            stackPane.setPrefWidth(changePasswordPane.getWidth());
            stackPane.setPrefHeight(changePasswordPane.getHeight());  // Hiển thị ChangePasswordPane
        } else {
            System.out.println("changePasswordPane is not initialized.");
        }
    
        vBox.setVisible(false);  // Ẩn VBox
        
=======
    private void changePassword() {
        // Lấy reference đến ChangePasswordView
        changePasswordPane = Model.getInstance().getViewFactory().getChangePasswordView();
    
        // Đảm bảo ChangePasswordView đã được hiển thị và tính toán kích thước
        changePasswordPane.setVisible(true);
    
        // Cập nhật kích thước của StackPane sao cho phù hợp với ChangePasswordView
        stackPane.setPrefWidth(changePasswordPane.getWidth());
        stackPane.setPrefHeight(changePasswordPane.getHeight());
    
        // Ẩn VBox và hiển thị ChangePasswordPane
        vBox.setVisible(false);
>>>>>>> Stashed changes
    }
    
}