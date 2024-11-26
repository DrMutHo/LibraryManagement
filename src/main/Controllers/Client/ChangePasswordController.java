package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

public class ChangePasswordController implements Initializable {
    @FXML
    private PasswordField passwordField0; // Current password
    @FXML
    private PasswordField passwordField1; // New password
    @FXML
    private PasswordField passwordField2; // Retype new password
    @FXML
    private Button toggleVisibilityButton0; // Button to toggle visibility for passwordField0
    @FXML
    private Button toggleVisibilityButton1; // Button to toggle visibility for passwordField1
    @FXML
    private Button toggleVisibilityButton2; // Button to toggle visibility for passwordField2
    @FXML
    private ImageView imageView0;
    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private HBox hBox0;
    @FXML
    private HBox hBox1;
    @FXML
    private HBox hBox2;
    

    // Flags to track visibility of each password field
    private boolean passwordVisible0 = false;
    private boolean passwordVisible1 = false;
    private boolean passwordVisible2 = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField_init();
        toggleVisibilityButton0.setOnAction(event -> togglePasswordVisibility0());
        toggleVisibilityButton1.setOnAction(event -> togglePasswordVisibility1());
        toggleVisibilityButton2.setOnAction(event -> togglePasswordVisibility2());
    }
    public void passwordField_init() {
        // Set prompt text to guide the user
        passwordField0.setPromptText("Enter your current password");
        passwordField1.setPromptText("Enter your new password");
        passwordField2.setPromptText("Retype your new password");
        
        // Load eye icon images for showing/hiding passwords
        Image eyeClosed = new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm());
        Image eyeOpen = new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm());
        
        imageView0.setImage(eyeClosed);
        imageView1.setImage(eyeClosed);
        imageView2.setImage(eyeClosed);

        // Add focus listeners to change border color when password fields are focused
        addFocusListener(passwordField0, hBox0);
        addFocusListener(passwordField1, hBox1);
        addFocusListener(passwordField2, hBox2);
    }

    // Method to toggle visibility for passwordField0
    @FXML
    private void togglePasswordVisibility0() {
        passwordVisible0 = !passwordVisible0; // Toggle visibility flag for passwordField0
        toggleVisibility(passwordField0, passwordVisible0);
    }

    // Method to toggle visibility for passwordField1
    @FXML
    private void togglePasswordVisibility1() {
        passwordVisible1 = !passwordVisible1; // Toggle visibility flag for passwordField1
        toggleVisibility(passwordField1, passwordVisible1);
    }

    // Method to toggle visibility for passwordField2
    @FXML
    private void togglePasswordVisibility2() {
        passwordVisible2 = !passwordVisible2; // Toggle visibility flag for passwordField2
        toggleVisibility(passwordField2, passwordVisible2);
    }

    // Helper method to toggle visibility of a password field
    private void toggleVisibility(PasswordField passwordField, boolean isVisible) {
        if (isVisible) {
            passwordField.setStyle("-fx-background-color: white;");
            passwordField.setText(passwordField.getText()); // Show text
        } else {
            passwordField.setStyle("-fx-background-color: #f0f0f0;");
            passwordField.setText(passwordField.getText()); // Hide text (default behavior of PasswordField)
        }
    }

    // Helper method to add focus listener and change HBox border color
    private void addFocusListener(PasswordField passwordField, HBox hbox) {
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // When focused, change the border color of the HBox
                hbox.getStyleClass().add("hbox_set-focused");
            } else {
                // When focus is lost, reset the border color of the HBox
                hbox.getStyleClass().add("hbox_set-focused");
            }
        });
    }
}
