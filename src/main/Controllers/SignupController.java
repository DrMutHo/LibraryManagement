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

    private void setButtonActions() {
        signup_createNewAccountButton.setOnAction(event -> onCreateNewAccount());
        signup_exitButton.setOnAction(event -> onExit());
    }

    public void passwordField_init() {
        initializePasswordFields(signup_passwordField, signup_textField);
        initializePasswordFields(signup_passwordField1, signup_textField1);

        // Set initial eye icons for password visibility
        initializeEyeIcons();

        // Set actions for toggle buttons
        setPasswordToggleActions();
    }

    private void initializePasswordFields(PasswordField passwordField, TextField textField) {
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        textField.setVisible(false);
        textField.setManaged(false);
        textField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    private void initializeEyeIcons() {
        signup_eyeClosed = new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm());
        signup_eyeOpen = new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm());
        signup_imageIcon.setImage(signup_eyeClosed);
        signup_imageIcon1.setImage(signup_eyeClosed);
    }

    private void setPasswordToggleActions() {
        signup_toggleButton.setOnAction(event -> togglePasswordVisibility(signup_passwordField, signup_textField, signup_imageIcon, signup_eyeOpen, signup_eyeClosed));
        signup_toggleButton1.setOnAction(event -> togglePasswordVisibility(signup_passwordField1, signup_textField1, signup_imageIcon1, signup_eyeOpen, signup_eyeClosed));
    }

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

    public void signup_prompt_init() {
        setPromptText();
        addFocusListeners();
    }

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

    private void addFocusListener(TextField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox.getStyleClass().add("signup_hbox_set-focused");
            } else {
                hbox.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
    }

    private void addFocusListener(PasswordField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox.getStyleClass().add("signup_hbox_set-focused");
            } else {
                hbox.getStyleClass().remove("signup_hbox_set-focused");
            }
        });
    }

    @FXML
    private void onExit() {
        stage = (Stage) signup_exitButton.getScene().getWindow();
        Model.getInstance().getViewFactory().showLoading(() -> {
            // Giả lập thời gian chuẩn bị tài nguyên (độ trễ nhân tạo)
            try {
                Thread.sleep(500); // Thời gian chuẩn bị tài nguyên giả lập 500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Công việc chính: Mở cửa sổ Sign Up và đóng cửa sổ hiện tại
            Platform.runLater(() -> {
                Model.getInstance().getViewFactory().showLoginWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            });
        }, signup_anchorpane);
    }

    @FXML
    private void onReturnToLogin() {
        stage = (Stage) returnToLoginButton.getScene().getWindow();
        Model.getInstance().getViewFactory().showLoading(() -> {
            // Giả lập thời gian chuẩn bị tài nguyên (độ trễ nhân tạo)
            try {
                Thread.sleep(500); // Thời gian chuẩn bị tài nguyên giả lập 500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Công việc chính: Mở cửa sổ Sign Up và đóng cửa sổ hiện tại
            Platform.runLater(() -> {
                Model.getInstance().getViewFactory().showLoginWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            });
        }, signup_anchorpane);
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
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Thực hiện công việc trong nền (background)
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
            // Nếu có lỗi, hiển thị tất cả các lỗi trong một hộp thoại cảnh báo
            showAlert(AlertType.WARNING, "Lỗi Đăng Ký", errorMessages.toString());
            isValid = false;
        }
        return isValid;
    }
    
    private void showAlert(AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            // Tạo một Alert với loại được chỉ định
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null); // Không có tiêu đề phụ
            alert.setContentText(message); // Thiết lập thông báo
    
            // Chọn biểu tượng phù hợp dựa trên AlertType
            ImageView icon = new ImageView();
    
            // Thay đổi hình ảnh dựa trên loại cảnh báo
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
    
            // Thiết lập kích thước cho biểu tượng
            icon.setFitHeight(30); // Chiều cao hình ảnh
            icon.setFitWidth(30); // Chiều rộng hình ảnh
            alert.setGraphic(icon); // Thêm hình ảnh vào Alert
    
            // Thay đổi nền của Alert thành màu trắng
            alert.getDialogPane().setStyle("-fx-background-color: white;"); // Nền trắng
    
            // Hiển thị Alert
            alert.showAndWait();
        });
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Xác minh địa chỉ email thông qua API.
     *
     * @param email Địa chỉ email cần xác minh.
     * @return true nếu email hợp lệ, ngược lại trả về false.
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
     * Kiểm tra tính hợp lệ của email trong cơ sở dữ liệu.
     *
     * @param email Địa chỉ email cần kiểm tra.
     * @return true nếu email không tồn tại trong cơ sở dữ liệu, ngược lại trả về false.
     */
    private boolean isValidEmailDatabase(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        int count = Model.getInstance().getDatabaseDriver().getEmailCountFromDatabase(email);
        return count == 0;
    }


    private boolean isValidEmail(String email) {
        return isValidEmailApi(email) && isValidEmailDatabase(email);
    }

    private boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

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
