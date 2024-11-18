package main.Controllers;
import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    @FXML
    private AnchorPane signup_anchorpane;
    @FXML
    private Stage stage;

    @Override 
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signup_prompt_init();
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

    public void signup_prompt_init() {
        signup_usernameField.setPromptText("Enter your username");
        signup_passwordField.setPromptText("Password must be 6+ characters");
        signup_textField.setPromptText("Password must be 6+ characters");
        signup_passwordField1.setPromptText("Confirmed password");
        signup_textField1.setPromptText("Confirmed password");
        signup_emailField.setPromptText("Enter your email"); 
        signup_addressField.setPromptText("Enter your home address");
        signup_phoneNumField.setPromptText("Enter your phone number");   
        signup_name.setPromptText("Enter your fullname");

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
        signup_emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox3.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox3.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
        signup_phoneNumField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox4.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox4.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
        signup_name.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox6.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox6.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
        signup_addressField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                signup_hbox5.getStyleClass().add("signup_hbox_set-focused");
            } else {
                signup_hbox5.getStyleClass().remove("signup_hbox_set-focused");
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
    public void showLoadingAndCloseSignUpWindow() {
        StackPane loadingOverlay = new StackPane();
        loadingOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); 
        loadingOverlay.setPrefSize(signup_anchorpane.getWidth(), signup_anchorpane.getHeight()); 
    
        ProgressIndicator progressIndicator = new ProgressIndicator();
        loadingOverlay.getChildren().add(progressIndicator);
    
        StackPane.setAlignment(progressIndicator, Pos.CENTER);
    
        signup_anchorpane.getChildren().add(loadingOverlay);
    
        Task<Void> loadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Giả lập một tác vụ dài
                for (int i = 0; i <= 1; i++) {
                    Thread.sleep(500); 
                }
                return null;
            }
        };
        loadingTask.setOnSucceeded(event -> {
            signup_anchorpane.getChildren().remove(loadingOverlay);
            Platform.runLater(() -> {
                Model.getInstance().getViewFactory().showLoginWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            });
        });
        new Thread(loadingTask).start();
    }
    @FXML
    private void onExit() {
        stage = (Stage) signup_exitButton.getScene().getWindow();
        showLoadingAndCloseSignUpWindow();
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
            signup_showAlert("Thông báo!", "Đăng ký thành công");
        } 
    }

    private void signup_showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
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

    private boolean isValidEmailApi(String email) {
        boolean isValidEmailApi = false;
        if (email == null || email.trim().isEmpty()) {
            return isValidEmailApi;
        } else {
            try { 
            @SuppressWarnings("deprecation")
            URL url = new URL("https://emailvalidation.abstractapi.com/v1/?api_key=fe97d39becd94b14a4a19b97ebcc29a1&email=" + email);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); 

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> map = gson.fromJson(response.toString(), type);
                
                System.out.println(map.get("is_free_email"));
                Map<String, Object> isFreeEmail = (Map<String, Object>) map.get("is_free_email");


                boolean value = (boolean) isFreeEmail.get("value");
                isValidEmailApi = value;
                System.out.println("Value of is_free_email: " + value);
            } else {
                System.out.println("GET request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return isValidEmailApi;
}   

    private boolean isValidEmailDatabase(String email) {
        boolean isValidEmailDatabase = false;
        if (email.trim().isEmpty() || email == null) {
            return isValidEmailDatabase;
        } else {
            try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection()) {
                String query = "SELECT COUNT(*) FROM Client WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count == 0;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isValidEmailDatabase;
    }
    private boolean isValidEmail(String email) {
        return isValidEmailApi(email) && isValidEmailDatabase(email);
    }

    private boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    private boolean isValidUserName(String username) {
    if (username == null || username.trim().isEmpty()) {
        return false; 
    }
    try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection()) {
        String query = "SELECT COUNT(*) FROM Client WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }

    return false;
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
