package main.Controllers;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;

/**
 * Controller class for handling the Sign-Up functionality.
 * Implements the Initializable interface to initialize the controller after its root element has been completely processed.
 */
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
    private TextField signup_name;

    @FXML
    private HBox signup_hbox6;

    @FXML
    private AnchorPane signup_anchorpane;

    @FXML
    private Stage stage;

    @FXML
    private AnchorPane successNotification;

    @FXML
    private Button returnToLoginButton;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signup_prompt_init();
        try {
            passwordField_init();
        } catch (Exception e) {
            e.printStackTrace(); // Better to log or handle the exception
        }
        setButtonActions();
    }

    /**
     * Sets up the actions for various buttons in the sign-up form.
     */
    private void setButtonActions() {
        signup_createNewAccountButton.setOnAction(event -> onCreateNewAccount());
        signup_exitButton.setOnAction(event -> onExit());
    }

    /**
     * Initializes the password fields by setting up visibility toggles and bindings.
     */
    public void passwordField_init() {
        initializePasswordFields(signup_passwordField, signup_textField);
        initializePasswordFields(signup_passwordField1, signup_textField1);

        // Set initial eye icons for password visibility
        initializeEyeIcons();

        // Set actions for toggle buttons
        setPasswordToggleActions();
    }

    /**
     * Initializes the visibility and bindings between PasswordField and TextField for password input.
     *
     * @param passwordField The PasswordField to initialize.
     * @param textField     The corresponding TextField for toggling visibility.
     */
    private void initializePasswordFields(PasswordField passwordField, TextField textField) {
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        textField.setVisible(false);
        textField.setManaged(false);
        textField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    /**
     * Initializes the eye icons used for toggling password visibility.
     */
    private void initializeEyeIcons() {
        signup_eyeClosed = new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm());
        signup_eyeOpen = new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm());
        signup_imageIcon.setImage(signup_eyeClosed);
        signup_imageIcon1.setImage(signup_eyeClosed);
    }

    /**
     * Sets the actions for the password visibility toggle buttons.
     */
    private void setPasswordToggleActions() {
        signup_toggleButton.setOnAction(event -> togglePasswordVisibility(signup_passwordField, signup_textField, signup_imageIcon, signup_eyeOpen, signup_eyeClosed));
        signup_toggleButton1.setOnAction(event -> togglePasswordVisibility(signup_passwordField1, signup_textField1, signup_imageIcon1, signup_eyeOpen, signup_eyeClosed));
    }

    /**
     * Toggles the visibility of the password between hidden and visible.
     *
     * @param passwordField The PasswordField to toggle.
     * @param textField     The TextField corresponding to the PasswordField.
     * @param imageIcon     The ImageView displaying the eye icon.
     * @param eyeOpen       The image representing the open eye.
     * @param eyeClosed     The image representing the closed eye.
     */
    private void togglePasswordVisibility(PasswordField passwordField, TextField textField, ImageView imageIcon, Image eyeOpen, Image eyeClosed) {
        if (passwordField.isVisible()) {
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            textField.setVisible(true);
            textField.setManaged(true);
            imageIcon.setImage(eyeOpen);
        } else {
            textField.setVisible(false);
            textField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            imageIcon.setImage(eyeClosed);
        }
    }

    /**
     * Initializes the prompt texts and focus listeners for the sign-up form fields.
     */
    public void signup_prompt_init() {
        setPromptText();
        addFocusListeners();
    }

    /**
     * Sets the prompt texts for all input fields in the sign-up form.
     */
    private void setPromptText() {
        signup_usernameField.setPromptText("Enter your username");
        signup_passwordField.setPromptText("Password must be 6+ characters");
        signup_textField.setPromptText("Password must be 6+ characters");
        signup_passwordField1.setPromptText("Confirmed password");
        signup_textField1.setPromptText("Confirmed password");
        signup_emailField.setPromptText("Enter your email");
        signup_addressField.setPromptText("Enter your home address");
        signup_phoneNumField.setPromptText("Enter your phone number");
        signup_name.setPromptText("Enter your fullname");
    }

    /**
     * Adds focus listeners to input fields to handle UI styling based on focus state.
     */
    private void addFocusListeners() {
        addFocusListener(signup_passwordField, signup_hbox1);
        addFocusListener(signup_textField, signup_hbox1);
        addFocusListener(signup_passwordField1, signup_hbox2);
        addFocusListener(signup_textField1, signup_hbox2);
        addFocusListener(signup_usernameField, signup_hbox0);
        addFocusListener(signup_emailField, signup_hbox3);
        addFocusListener(signup_phoneNumField, signup_hbox4);
        addFocusListener(signup_name, signup_hbox6);
        addFocusListener(signup_addressField, signup_hbox5);
    }

    /**
     * Adds a focus listener to a TextField to update the corresponding HBox style based on focus.
     *
     * @param field The TextField to add the listener to.
     * @param hbox  The HBox whose style is to be updated.
     */
    private void addFocusListener(TextField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox.getStyleClass().add("signup_hbox_set-focused");
            } else {
                hbox.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
    }

    /**
     * Adds a focus listener to a PasswordField to update the corresponding HBox style based on focus.
     *
     * @param field The PasswordField to add the listener to.
     * @param hbox  The HBox whose style is to be updated.
     */
    private void addFocusListener(PasswordField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox.getStyleClass().add("signup_hbox_set-focused");
            } else {
                hbox.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
    }

    /**
     * Handles the action of exiting the sign-up form.
     * Shows a loading screen during the transition back to the login window.
     */
    @FXML
    private void onExit() {
        stage = (Stage) signup_exitButton.getScene().getWindow();
        Model.getInstance().getViewFactory().showLoading(() -> {
            // Simulate resource preparation time (artificial delay)
            try {
                Thread.sleep(500); // Simulated resource preparation time of 500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Main task: Open the Login window and close the current stage
            Platform.runLater(() -> {
                Model.getInstance().getViewFactory().showLoginWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            });
        }, signup_anchorpane);
    }

    /**
     * Handles the action of returning to the login window from the sign-up form.
     * Shows a loading screen during the transition.
     */
    @FXML
    private void onReturnToLogin() {
        stage = (Stage) returnToLoginButton.getScene().getWindow();
        Model.getInstance().getViewFactory().showLoading(() -> {
            // Simulate resource preparation time (artificial delay)
            try {
                Thread.sleep(500); // Simulated resource preparation time of 500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Main task: Open the Login window and close the current stage
            Platform.runLater(() -> {
                Model.getInstance().getViewFactory().showLoginWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            });
        }, signup_anchorpane);
    }

    /**
     * Handles the action of creating a new account.
     * Validates the input fields and creates a new user account if all validations pass.
     */
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
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Perform background work
                return isValidSignUp(email, phoneNum, username, password, password1, address, name);
            }
        };
        
        task.setOnSucceeded(event -> { 
            if (task.getValue()) {
                Model.getInstance().evaluateClientCred(username);
                Model.getInstance().getViewFactory().showLoading(() -> {
                    try {
                       Model.getInstance().getDatabaseDriver().createClient(email, phoneNum, address, username, hashedPassword, name);
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                    Platform.runLater(() -> {
                        successNotification.setVisible(true);
                    });
                }, signup_anchorpane);
            }
        });
        new Thread(task).start();
    }

    /**
     * Validates the sign-up form inputs.
     *
     * @param email     The email entered by the user.
     * @param phoneNum  The phone number entered by the user.
     * @param username  The username entered by the user.
     * @param password  The password entered by the user.
     * @param password1 The confirmed password entered by the user.
     * @param address   The address entered by the user.
     * @param name      The full name entered by the user.
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean isValidSignUp(String email, String phoneNum,
    String username, String password, String password1, String address, String name) {
        boolean isValid = true;
        StringBuilder errorMessages = new StringBuilder();

        if (!isValidName(name)) {
            errorMessages.append("- Tên hợp lệ phải không để trống và chỉ chứa chữ cái.\n");
            isValid = false;
        }

        if (!isValidUserName(username)) {
            errorMessages.append("- Tên người dùng phải chưa tồn tại.\n");
            isValid = false;
        }

        if (!isValidAddress(address)) {
            errorMessages.append("- Địa chỉ không được để trống.\n");
            isValid = false;
        }

        if (!isValidEmail(email)) {
            errorMessages.append("- Địa chỉ email không hợp lệ.\n");
            isValid = false;
        }

        if (!isValidPassword(password, password1)) {
            errorMessages.append("- Mật khẩu không khớp hoặc ít nhất phải có 8 ký tự.\n");
            isValid = false;
        }

        if (!isValidPhoneNum(phoneNum)) {
            errorMessages.append("- Số điện thoại phải gồm 10 chữ số.\n");
            isValid = false;
        }

        if (errorMessages.length() > 0) {
            // If there are errors, display all errors in a warning dialog
            showAlert(AlertType.WARNING, "Lỗi Đăng Ký", errorMessages.toString());
            isValid = false;
        }
        return isValid;
    }
    
    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of alert to display.
     * @param title     The title of the alert dialog.
     * @param message   The message content of the alert dialog.
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            // Create an Alert with the specified type
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null); // No header
            alert.setContentText(message); // Set the message
    
            // Choose the appropriate icon based on AlertType
            ImageView icon = new ImageView();
    
            // Change the image based on the alert type
            switch (alertType) {
                case INFORMATION:
                    icon.setImage(new Image(getClass().getResource("/resources/Images/success.png").toExternalForm()));
                    break;
                case WARNING:
                    icon.setImage(new Image(getClass().getResource("/resources/Images/warning-icon.png").toExternalForm()));
                    break;
                case ERROR:
                    icon.setImage(new Image(getClass().getResource("/resources/Images/error-icon.png").toExternalForm()));
                    break;
                default:
                    icon.setImage(new Image(getClass().getResource("/resources/Images/warning-icon.png").toExternalForm()));
                    break;
            }
    
            // Set the size for the icon
            icon.setFitHeight(30); // Image height
            icon.setFitWidth(30); // Image width
            alert.setGraphic(icon); // Add the image to the Alert
    
            // Change the background of the Alert to white
            alert.getDialogPane().setStyle("-fx-background-color: white;"); // White background
    
            // Display the Alert
            alert.showAndWait();
        });
    }

    /**
     * Validates the user's full name.
     *
     * @param name The full name entered by the user.
     * @return true if the name is valid, false otherwise.
     */
    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Verifies the email address using an external API.
     *
     * @param email The email address to verify.
     * @return true if the email is valid according to the API, false otherwise.
     */
    private boolean isValidEmailApi(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String apiResponse = Model.getInstance().getDatabaseDriver().getEmailValidationApiResponse(email);
        if (apiResponse != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> map = gson.fromJson(apiResponse, type);
            Map<String, Object> isFreeEmail = (Map<String, Object>) map.get("is_free_email");
            boolean value = (boolean) isFreeEmail.get("value");
            System.out.println("Value of is_free_email: " + value);
            return value;
        }
        return false;
    }

    /**
     * Checks the validity of the email address within the database.
     *
     * @param email The email address to check.
     * @return true if the email does not exist in the database, false otherwise.
     */
    private boolean isValidEmailDatabase(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        int count = Model.getInstance().getDatabaseDriver().getEmailCountFromDatabase(email);
        return count == 0;
    }

    /**
     * Validates the email by checking both the external API and the database.
     *
     * @param email The email address to validate.
     * @return true if the email is valid in both the API and the database, false otherwise.
     */
    private boolean isValidEmail(String email) {
        return isValidEmailApi(email) && isValidEmailDatabase(email);
    }

    /**
     * Validates the address entered by the user.
     *
     * @param address The address entered by the user.
     * @return true if the address is not null and not empty, false otherwise.
     */
    private boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    /**
     * Validates the username entered by the user.
     *
     * @param username The username entered by the user.
     * @return true if the username does not exist in the database and is not empty, false otherwise.
     */
    private boolean isValidUserName(String username) {
        int count = Model.getInstance().getDatabaseDriver().getUsernameCount(username);
        if (username == null || username.trim().isEmpty()) {
            return false; 
        }
        if (count == 0) {
            return true;
        } 
        return false;
    }

    /**
     * Validates the phone number entered by the user.
     *
     * @param phoneNum The phone number entered by the user.
     * @return true if the phone number consists of exactly 10 digits, false otherwise.
     */
    private boolean isValidPhoneNum(String phoneNum) {
        if (phoneNum == null) {
            return false;
        }
        return phoneNum.length() == 10; 
    }

    /**
     * Validates the password entered by the user.
     *
     * @param password  The password entered by the user.
     * @param password1 The confirmed password entered by the user.
     * @return true if the password meets the criteria and matches the confirmation, false otherwise.
     */
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
