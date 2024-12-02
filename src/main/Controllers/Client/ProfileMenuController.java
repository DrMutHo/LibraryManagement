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

/**
 * Controller for the profile menu in the client interface.
 * This controller manages actions such as displaying profile details, editing profile,
 * changing password, deleting account, and updating profile image.
 */
public class ProfileMenuController implements Initializable {

    @FXML
    private HBox changeProfileImage;  // The HBox to handle profile image change action

    @FXML
    private HBox changePassword;  // The HBox to handle password change action

    @FXML
    private HBox deleteAccount;  // The HBox to handle account deletion action

    @FXML
    private HBox editProfile;  // The HBox to handle profile editing action

    @FXML
    private Label labelFullName;  // Label to display the full name of the user

    @FXML
    private Circle profileImage;  // Circle to display the user's profile image

    @FXML
    private Button profileDetail;  // Button to view the profile details

    /**
     * Initializes the ProfileMenuController by setting up the user's full name and 
     * profile image, and adding event listeners for various actions.
     * 
     * @param location the location used to resolve relative paths for resources.
     * @param resources the resources used for localization.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set user's full name and profile image from the Model
        labelFullName.setText(Model.getInstance().getClient().getName());
        ImagePattern pattern = new ImagePattern(
                new Image(Model.getInstance().getClient().getAvatarImagePath()));
        profileImage.setFill(pattern);
        profileImage.setStroke(Color.TRANSPARENT);

        // Add listeners for user interactions
        addListeners();
    }

    /**
     * Adds listeners to the UI components to handle user interactions, such as changing profile image,
     * changing password, editing profile, and deleting the account.
     */
    private void addListeners() {
        profileDetail.setOnAction(event -> onProfileDetail());  // View profile details
        changeProfileImage.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn Ảnh Đại Diện");

            // Set valid image file extensions for profile image selection
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Tệp Hình Ảnh", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

            // Open file chooser dialog to select a new profile image
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                String fileURI = selectedFile.toURI().toString();
                Model.getInstance().setClientAvatar(fileURI);  // Set new avatar path in the model
                Image image = new Image(fileURI);  // Load new image
                ImagePattern pattern = new ImagePattern(image);  // Create new image pattern for the profile image circle
                profileImage.setFill(pattern);  // Apply new image pattern to the profile image
            }
        });
        changePassword.setOnMouseClicked(event -> onChangePassword());  // Change password action
        deleteAccount.setOnMouseClicked(event -> onDeleteAccount());  // Delete account action
        editProfile.setOnMouseClicked(event -> onEditProfile());  // Edit profile action
    }

    /**
     * Navigates to the profile detail view.
     */
    private void onProfileDetail() {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem().set(ProfileMenuOptions.PROFILEDETAIL);
    }

    /**
     * Navigates to the edit profile view.
     */
    private void onEditProfile() {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem().set(ProfileMenuOptions.EDITPROFILE);
    }

    /**
     * Navigates to the change password view.
     */
    private void onChangePassword() {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem().set(ProfileMenuOptions.CHANGEPASSWORD);
    }

    /**
     * Navigates to the delete account view.
     */
    private void onDeleteAccount() {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem().set(ProfileMenuOptions.DELETEACCOUNT);
    }
}
