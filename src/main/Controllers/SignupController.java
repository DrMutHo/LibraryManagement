package main.Controllers;
import org.mindrot.jbcrypt.BCrypt;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
    private Button signup_createNewAccountButton;
    @FXML 
    private ImageView signup_imageErrorIcon;
    @FXML 
    private ImageView signup_imageErrorIcon1;
    @FXML 
    private ImageView signup_imageErrorIcon2;
    @FXML 
    private ImageView signup_imageErrorIcon3;
    @FXML 
    private ImageView signup_imageErrorIcon4;
    @FXML 
    private ImageView signup_imageErrorIcon5;
    @FXML
    private TextField signup_name;
    @FXML
    private HBox signup_hbox6;
    @FXML
    private ImageView signup_imageErrorIcon6;

    @Override 
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username_password_promptext_init();
        try { 
            passwordField_init();
        } catch (Exception e) {
            e.getStackTrace();
        }
        signup_createNewAccountButton.setOnAction(event -> onCreateNewAccount());
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
    private void onCreateNewAccount() {
        String email = signup_emailField.getText();
        String phoneNum = signup_phoneNumField.getText();
        String username = signup_usernameField.getText();
        String password = signup_passwordField.getText();
        String password1 = signup_passwordField1.getText();
        String address = signup_addressField.getText();
        String name = signup_name.getText();
        String hashedPassword = BCrypt.hashpw(password1, BCrypt.gensalt());
        if (isValidSignUp(email, phoneNum, username, password, password1, address, name)) {
            Model.getInstance().getDatabaseDriver().createClient(email, phoneNum, address, username, hashedPassword, name);
        }
    }

    private boolean isValidSignUp(String email, String phoneNum,
    String username, String password, String password1, String address, String name) {
        boolean isValid = true;

        if (!isValidName(name)) {
            highlightField(signup_hbox6, signup_imageErrorIcon6);
        } else {
            resetField(signup_hbox6, signup_imageErrorIcon6);
        }
        if (!isValidUserName(username)) {
            highlightField(signup_hbox0, signup_imageErrorIcon);
            isValid = false;
        } else {
            resetField(signup_hbox0, signup_imageErrorIcon);
        }

        if(!isValidAddress(address)) {
            highlightField(signup_hbox5, signup_imageErrorIcon1);
            isValid = false;
        } else {
            resetField(signup_hbox5, signup_imageErrorIcon1);
        }
        
        if (!isValidEmail(email)) {
            highlightField(signup_hbox3, signup_imageErrorIcon3);
            isValid = false;
        } else {
            resetField(signup_hbox3, signup_imageErrorIcon3);
        }
        
        if (!isValidPassword(password, password1)) {
            highlightField(signup_hbox1, signup_imageErrorIcon4);
            highlightField(signup_hbox2, signup_imageErrorIcon5);
            isValid = false;
        } else {
            resetField(signup_hbox1, signup_imageErrorIcon4);
            resetField(signup_hbox2, signup_imageErrorIcon5);
        }
        
        if (!isValidPhoneNum(phoneNum)) {
            highlightField(signup_hbox4, signup_imageErrorIcon2);
            isValid = false;
        } else {
            resetField(signup_hbox4, signup_imageErrorIcon2);
        }
        return isValid;
    }
    
    private void highlightField(HBox hbox, ImageView icon) {
        hbox.getStyleClass().add("signup_hbox_set-error"); 
        icon.setImage(new Image(getClass().getResource("/resources/Images/warning-icon.png").toExternalForm()));
    }
        
    private void resetField(HBox hbox, ImageView icon) {
        hbox.getStyleClass().remove("signup_hbox_set-error"); 
        icon.setImage(null); 
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false; // Kiểm tra null và chuỗi rỗng
        }
        return true;
    }

    private boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    private boolean isValidUserName(String username) {
    if (username == null || username.trim().isEmpty()) {
        return false; // Username không được để trống
    }

    // Kết nối đến cơ sở dữ liệu
    try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection()) {
        String query = "SELECT COUNT(*) FROM Client WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0; // Trả về true nếu username không tồn tại
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi xảy ra
    }

    return false; // Mặc định trả về false nếu có lỗi xảy ra
}

    private boolean isValidPhoneNum(String phoneNum) {
        if (phoneNum == null) {
            return false;
        }
        return phoneNum.length() == 10; 
    }

    private boolean isValidPassword(String password, String password1) {
        if (password == null || password1 == null) {
            return false;
        }
        if (password.length() < 6) {
            return false;
        }
        
        if (!password.equals(password1)) {
            return false;
        }
        return true;
    }
}
