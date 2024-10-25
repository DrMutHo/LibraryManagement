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
    @FXML
    private HBox signup_hbox2;
    @FXML
    private PasswordField signup_passwordField1;
    @FXML
    private TextField signup_textField1;
    @FXML
    private Button signup_toggleButton1;
    @FXML 
    private ImageView signup_imageIcon1;

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
        signup_passwordField1.setVisible(true);
        signup_passwordField1.setManaged(true);
        signup_textField1.setVisible(false);
        signup_textField1.setManaged(false);
        signup_textField.textProperty().bindBidirectional(signup_passwordField.textProperty());
        signup_textField1.textProperty().bindBidirectional(signup_passwordField1.textProperty());
        signup_eyeClosed = new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm());
        signup_eyeOpen = new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm());
        signup_imageIcon.setImage(signup_eyeClosed);
        signup_imageIcon1.setImage(signup_eyeClosed);
        signup_toggleButton1.setOnAction(event -> togglePasswordVisibility1());
        signup_toggleButton.setOnAction(even -> togglePasswordVisibility());
        
    }

    public void username_password_promptext_init() {
        signup_usernameField.setPromptText("Enter your username");
        signup_passwordField.setPromptText("Enter your password");
        signup_textField.setPromptText("Enter your password");
        signup_passwordField1.setPromptText("Re-enter your password");
        signup_textField1.setPromptText("Re-enter your password");

        signup_passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox1.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox1.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
        signup_usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox0.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox0.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
        signup_textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox1.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox1.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
        signup_passwordField1.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox2.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox2.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
        signup_textField1.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox2.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox2.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
    }
    @FXML
    private void togglePasswordVisibility() {
        if (signup_passwordField.isVisible()) {
            signup_passwordField.setVisible(false);
            signup_passwordField.setManaged(false);
            signup_textField.setVisible(true);
            signup_textField.setManaged(true);
            signup_imageIcon.setImage(signup_eyeOpen);
        } else {
            signup_textField.setVisible(false);
            signup_textField.setManaged(false);
            signup_passwordField.setVisible(true);
            signup_passwordField.setManaged(true);
            signup_imageIcon.setImage(signup_eyeClosed);
        }
    }

    @FXML
    private void togglePasswordVisibility1() {
        if (signup_passwordField.isVisible()) {
            signup_passwordField1.setVisible(false);
            signup_passwordField1.setManaged(false);
            signup_textField1.setVisible(true);
            signup_textField1.setManaged(true);
            signup_imageIcon1.setImage(signup_eyeOpen);
        } else {
            signup_textField1.setVisible(false);
            signup_textField1.setManaged(false);
            signup_passwordField1.setVisible(true);
            signup_passwordField1.setManaged(true);
            signup_imageIcon1.setImage(signup_eyeClosed);
        }
    }
}
