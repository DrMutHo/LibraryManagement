package main.Controllers.Client;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.plaf.basic.BasicButtonUI;

import org.mindrot.jbcrypt.BCrypt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.ViewFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private Button saveChangesButton;
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
    @FXML
    private ImageView warning0;
    @FXML
    private ImageView warning1;
    @FXML
    private ImageView warning2;



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
        passwordField1.setPromptText("Your new password must be over 6 letters");
        passwordField2.setPromptText("Retype your new password");
        textField0.setPromptText("Enter your current password");
        textField1.setPromptText("Your new password must be over 6 letters");
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

    @FXML
    private void changePassword() {
        // Lấy giá trị từ các trường nhập liệu
        String currentPassword = passwordField0.getText();
        String newPassword = passwordField1.getText();
        String confirmPassword = passwordField2.getText();
        boolean canUpdate = true;
    
        // Kiểm tra các điều kiện đầu vào và highlight các trường hợp lỗi
        if (!isValidCurrentPassword(currentPassword)) {
            highlightField(hBox0, warning0);
            canUpdate = false;
        } else {
            resetField(hBox0, warning0);
        }
    
        if (!isValidNewPassword(newPassword)) {
            highlightField(hBox1, warning1);
            canUpdate = false;
        } else {
            resetField(hBox1, warning1);
        }
    
        if (!isValidConfirmPassword(confirmPassword)) {
            highlightField(hBox2, warning2);
            canUpdate = false;
        } else {
            resetField(hBox2, warning2);
        }
    
        // Kiểm tra mật khẩu hiện tại từ cơ sở dữ liệu
        if (!checkCurrentPassword(Model.getInstance().getClient().getUsername(), currentPassword)) {
            highlightField(hBox0, warning0);
            canUpdate = false;
        }
    
        // Kiểm tra nếu tất cả các điều kiện đều hợp lệ
        if (canUpdate) {
            // Cập nhật mật khẩu mới vào cơ sở dữ liệu
            if (updatePassword(Model.getInstance().getClient().getUsername(), newPassword)) {
                showAlert("Password changed successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error updating password.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Please correct the highlighted errors.", Alert.AlertType.ERROR);
        }
    }
    
    // Phương thức kiểm tra mật khẩu hiện tại
    private boolean isValidCurrentPassword(String currentPassword) {
        return !currentPassword.isEmpty();
    }
    
    // Phương thức kiểm tra mật khẩu mới
    private boolean isValidNewPassword(String newPassword) {
        return !newPassword.isEmpty() && newPassword.length() >= 6;
    }
    
    // Phương thức kiểm tra mật khẩu xác nhận
    private boolean isValidConfirmPassword(String confirmPassword) {
        return !confirmPassword.isEmpty() && confirmPassword.length() >= 6;
    }
    
    // Phương thức highlight các trường có lỗi
    private void highlightField(HBox hbox, ImageView icon) {
        hbox.getStyleClass().add("hbox_set-error");
        icon.setImage(new Image(getClass().getResource("/resources/Images/warning-icon.png").toExternalForm()));
    }
    
    // Phương thức reset lại các trường hợp không có lỗi
    private void resetField(HBox hbox, ImageView icon) {
        hbox.getStyleClass().remove("hbox_set-error");
        icon.setImage(null);
    }
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);  // Có thể bỏ trống hoặc thiết lập header nếu cần
        alert.setContentText(message);
        alert.showAndWait();  // Chờ người dùng đóng thông báo trước khi tiếp tục
    }

    private boolean checkCurrentPassword(String username, String password) {
        // Truy vấn cơ sở dữ liệu để lấy mật khẩu hash của người dùng
        String query = "SELECT * FROM client WHERE username = ?";
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return false;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, username);

            // Thực thi truy vấn
            ResultSet resultSet = preparedStatement.executeQuery();

            // Nếu tìm thấy người dùng, so sánh mật khẩu
            if (resultSet.next()) {
                String storedPasswordHash = resultSet.getString("password_hash");  // Mật khẩu đã được hash trong DB

                // Kiểm tra mật khẩu đầu vào với mật khẩu đã lưu (bằng cách xác minh hash)
                return verifyPassword(password, storedPasswordHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Nếu không tìm thấy người dùng hoặc có lỗi, trả về false
    }

    // Hàm kiểm tra mật khẩu bằng cách so sánh với hash
    private boolean verifyPassword(String password, String storedPasswordHash) {
        // Giả sử bạn dùng thư viện bcrypt hoặc PBKDF2 để so sánh hash
        // Ví dụ sử dụng BCrypt để xác thực mật khẩu
        return BCrypt.checkpw(password, storedPasswordHash);
    }

    private boolean updatePassword(String username, String newPassword) {
        // Câu truy vấn để cập nhật mật khẩu
        String updateQuery = "UPDATE client SET password_hash = ? WHERE username = ?";

        // Mã hóa mật khẩu mới (nếu bạn sử dụng BCrypt, ví dụ)
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());  // Mã hóa mật khẩu mới

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return false;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, hashedPassword);  // Đặt mật khẩu đã mã hóa
            preparedStatement.setString(2, username);  // Đặt tên người dùng

            // Thực thi câu lệnh update
            int rowsUpdated = preparedStatement.executeUpdate();

            // Nếu có ít nhất một dòng bị ảnh hưởng, tức là mật khẩu đã được cập nhật
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Trả về false nếu có lỗi hoặc không có dòng nào bị cập nhật
    }

    // Giả sử hàm này sẽ trả về ID người dùng hiện tại
    private int getUserId() {
        // Bạn có thể lấy ID người dùng từ session hoặc thông qua cơ chế xác thực của ứng dụng
        return 1;  // Giả sử ID của người dùng là 1
    }
}