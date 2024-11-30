package main.Controllers.Client;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import main.Models.Model;
import main.Views.ClientMenuOptions;
import main.Views.ProfileMenuOptions;

public class ProfileMenuController implements Initializable {

    @FXML
    private HBox changeProfileImage;

    @FXML
    private HBox changePassword;

    @FXML
    private HBox deleteAccount;

    @FXML
    private HBox editProfile;

    @FXML
    private Label labelFullName;

    @FXML
    private Circle profileImage;

    @FXML
    private Button profileDetail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        labelFullName.setText(Model.getInstance().getClient().getName());
        ImagePattern pattern = new ImagePattern(
                new Image(Model.getInstance().getClient().getAvatarImagePath()));
        profileImage.setFill(pattern);
        profileImage.setStroke(Color.TRANSPARENT);

        addListeners();
    }

    private void addListeners() {
        profileDetail.setOnAction(event -> onProfileDetail());
        changeProfileImage.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn Ảnh Đại Diện");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Tệp Hình Ảnh", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                String fileURI = selectedFile.toURI().toString();
                Model.getInstance().setClientAvatar(fileURI);
                Image image = new Image(fileURI);
                ImagePattern pattern = new ImagePattern(
                        image);
                profileImage.setFill(pattern);
            }
        });
        changePassword.setOnMouseClicked(event -> onChangePassword());
        deleteAccount.setOnMouseClicked(event -> onDeleteAccount());
        editProfile.setOnMouseClicked(event -> onEditProfile());
    }

    private void onProfileDetail() {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem().set(ProfileMenuOptions.PROFILEDETAIL);
    }

    private void onEditProfile() {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem().set(ProfileMenuOptions.EDITPROFILE);
    }

    private void onChangePassword() {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem().set(ProfileMenuOptions.CHANGEPASSWORD);
    }

    private void onDeleteAccount() {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem().set(ProfileMenuOptions.DELETEACCOUNT);
    }
}
