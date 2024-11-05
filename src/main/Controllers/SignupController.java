package main.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;

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
    @FXML
    private Button signup_exitButton;
    @FXML
    private HBox signup_hbox3;
    @FXML
    private TextField signup_emailField;
    @FXML
    private TextField signup_phoneNumField;
    @FXML
    private TextField signup_addressField;
    @FXML
    private HBox signup_hbox4;
    @FXML
    private HBox signup_hbox5;
    @FXML 
    private Button signup_creatNewAccountButton;

    @Override 
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username_password_promptext_init();
        try { 
            passwordField_init();
        } catch (Exception e) {
            e.getStackTrace();
        }
        signup_creatNewAccountButton.setOnAction(event -> onCreatNewAccount());
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
        signup_passwordField1.setPromptText("Confirmed password");
        signup_textField1.setPromptText("Confirmed password");
        signup_emailField.setPromptText("Enter your email"); 
        signup_addressField.setPromptText("Enter your home address");
        signup_phoneNumField.setPromptText("Enter your phone number");   

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
        if (signup_passwordField1.isVisible()) {
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

    @FXML
    private void onExit() {
         Stage stage = (Stage) signup_exitButton.getScene().getWindow();
            Model.getInstance().getViewFactory().showLoginWindow();
    }

    @FXML
    private void onCreatNewAccount() {
        
    }

    private boolean isValidSignUp(String username, String password) {
        String query = "SELECT * FROM Client WHERE username = ?";
        try (Connection connection = databaseDriver.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return false;
            }
    
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                String storedPasswordHash = resultSet.getString("password_hash");
                return verifyPassword(password, storedPasswordHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
