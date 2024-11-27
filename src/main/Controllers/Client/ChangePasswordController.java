package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private TextField textField0;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private Button toggleButton0; // Button to toggle visibility for passwordField0
    @FXML
    private Button toggleButton1; // Button to toggle visibility for passwordField1
    @FXML
    private Button toggleButton2; // Button to toggle visibility for passwordField2
    @FXML
    private ImageView imageView0;
    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML 
    private Image eyeOpen;
    @FXML
    private Image eyeClosed;
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
    }
    public void passwordField_init() {
        // Set prompt text for the password fields
        setPromptText();
    
        // Initialize password fields and text fields
        initializePasswordAndTextFields(passwordField0, textField0, hBox0);
        initializePasswordAndTextFields(passwordField1, textField1, hBox1);
        initializePasswordAndTextFields(passwordField2, textField2, hBox2);
    
        // Load eye icon images for showing/hiding passwords
        eyeClosed = new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm());
        eyeOpen = new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm());
    
        // Set initial icon state
        imageView0.setImage(eyeClosed);
        imageView1.setImage(eyeClosed);
        imageView2.setImage(eyeClosed);
    
        // Set toggle button actions
        toggleButton0.setOnAction(event -> togglePasswordVisibility(passwordField0, textField0, imageView0));
        toggleButton1.setOnAction(event -> togglePasswordVisibility(passwordField1, textField1, imageView1));
        toggleButton2.setOnAction(event -> togglePasswordVisibility(passwordField2, textField2, imageView2));
    }
    
    // Helper method to set prompt text for all password fields
    private void setPromptText() {
        passwordField0.setPromptText("Enter your current password");
        passwordField1.setPromptText("Enter your new password");
        passwordField2.setPromptText("Retype your new password");
        textField0.setPromptText("Enter your current password");
        textField1.setPromptText("Enter your new password");
        textField2.setPromptText("Retype your new password");
    }
    
    // Helper method to initialize password fields and text fields
    private void initializePasswordAndTextFields(PasswordField passwordField, TextField textField, HBox hBox) {
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        textField.setVisible(false);
        textField.setManaged(false);
        textField.textProperty().bindBidirectional(passwordField.textProperty());
    
        // Add focus listeners
        addPasswordFieldFocusListener(passwordField, hBox);
        addTextFieldListener(textField, hBox);
    }
    
    // Focus listener for password fields
    private void addPasswordFieldFocusListener(PasswordField passwordField, HBox hbox) {
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox.getStyleClass().add("hbox_set-focused");
            } else {
                hbox.getStyleClass().remove("hbox_set-focused");
            }
        });
    }
    
    // Focus listener for text fields
    private void addTextFieldListener(TextField textField, HBox hbox) {
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox.getStyleClass().add("hbox_set-focused");
            } else {
                hbox.getStyleClass().remove("hbox_set-focused");
            }
        });
    }
    
    // Toggle password visibility for each field
    private void togglePasswordVisibility(PasswordField passwordField, TextField textField, ImageView imageView) {
        if (passwordField.isVisible()) {
            // Hide PasswordField, show TextField
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            textField.setVisible(true);
            textField.setManaged(true);
            imageView.setImage(eyeOpen); // Set the eye-open icon
        } else {
            // Hide TextField, show PasswordField
            textField.setVisible(false);
            textField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            imageView.setImage(eyeClosed); // Set the eye-closed icon
        }
    }
    
}
