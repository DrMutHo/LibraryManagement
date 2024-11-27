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
    private Button changePasswordButton;
    @FXML
    private Button changeEmailButton;
    @FXML
    private Button deleteAccountButton;
    @FXML
    private BorderPane borderPane;
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
private void changePassword() {
    // Lấy changePasswordPane từ ViewFactory
    AnchorPane changePasswordPane = Model.getInstance().getViewFactory().getChangePasswordView();

    // Kiểm tra nếu changePasswordPane không null
    if (changePasswordPane != null) {
        // Đặt kích thước tối đa bằng với kích thước ưu tiên
        changePasswordPane.setMaxWidth(changePasswordPane.getPrefWidth());  // Chiều rộng tối đa bằng chiều rộng ưu tiên
        changePasswordPane.setMaxHeight(changePasswordPane.getPrefHeight()); // Chiều cao tối đa bằng chiều cao ưu tiên

        // Đặt changePasswordPane vào Center của BorderPane
        borderPane.setCenter(changePasswordPane);

        // Hiển thị changePasswordPane và ẩn vBox
        changePasswordPane.setVisible(true);
        changePasswordPane.setManaged(true); // Đảm bảo changePasswordPane tham gia layout
        vBox.setVisible(false);
        vBox.setManaged(false); // Ẩn vBox khỏi layout

    } else {
        System.out.println("changePasswordPane is not initialized.");
    }
}

}