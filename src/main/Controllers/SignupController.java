package main.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SignupController implements Initializable {
    @FXML
    private PasswordField signup_passwordField;
    @FXML
    private TextField signup_usernameField;
    @FXML
    private ImageView signup_imageIcon;
    @FXML
    private TextField signup_textField;
    @FXML
    private Image signup_eyeClosed;
    @FXML
    private Image signup_eyeOpen;
    @FXML
    private Button signup_toggleButton;
    @FXML
    private HBox signup_hbox1;
    @FXML
    private HBox signup_hbox0;

    @Override 
     public void initialize(URL url, ResourceBundle resourceBundle) {
        username_password_promptext_init();
        passwordField_init();
    }
    public void passwordField_init() {
        signup_passwordField.setVisible(true);
        signup_passwordField.setManaged(true);
        signup_textField.setVisible(false);
        signup_textField.setManaged(false);
        signup_textField.textProperty().bindBidirectional(signup_passwordField.textProperty());
        signup_eyeClosed = new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm());
        signup_eyeOpen = new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm());
        signup_imageIcon.setImage(signup_eyeClosed);
        signup_toggleButton.setOnAction(even -> togglePasswordVisibility());
    }

    public void username_password_promptext_init() {
        signup_usernameField.setPromptText("Enter your username");
        signup_passwordField.setPromptText("Enter your password");
        signup_textField.setPromptText("Enter your password");
        signup_passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox1.getStyleClass().add("hbox_set-focused");
            } else {
                signup_hbox1.getStyleClass().remove("hbox_set-focused");
            }
        });
        signup_usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox0.getStyleClass().add("hbox_set-focused");
            } else {
                signup_hbox0.getStyleClass().remove("hbox_set-focused");
            }
        });
        signup_textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox1.getStyleClass().add("hbox_set-focused");
            } else {
                signup_hbox0.getStyleClass().remove("hbox_set-focused");
            }
        });
    }
    @FXML
    private void togglePasswordVisibility() {
        if (signup_passwordField.isVisible()) {
            // Ẩn passwordField và hiển thị textField (mật khẩu dưới dạng văn bản)
            signup_passwordField.setVisible(false);
            signup_passwordField.setManaged(false);
            signup_textField.setVisible(true);
            signup_textField.setManaged(true);
            signup_imageIcon.setImage(signup_eyeOpen);
        } else {
            // Hiển thị passwordField (mật khẩu dưới dạng dấu chấm) và ẩn textField
            signup_textField.setVisible(false);
            signup_textField.setManaged(false);
            signup_passwordField.setVisible(true);
            signup_passwordField.setManaged(true);
            signup_imageIcon.setImage(signup_eyeClosed);
        }
    }
}
