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
import javafx.scene.control.Label;
import javafx.scene.Parent;

public class ChangePasswordController implements Initializable {
    @FXML
    private PasswordField passwordField0;
    @FXML
    private PasswordField passwordField1;
    @FXML
    private PasswordField passwordField2;
    @FXML
    private Button saveChangesButton;
    @FXML
    private ImageView eyeIcon0;
    @FXML
    private ImageView eyeIcon1;
    @FXML
    private ImageView eyeIcon2;

    @FXML
    private Label currentPasswordLabel;
    @FXML
    private Label newPasswordLabel;
    @FXML
    private Label confirmPasswordLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField_init();
        eyeIcon0.setOnMouseClicked(event -> togglePasswordVisibility(passwordField0, eyeIcon0));
        eyeIcon1.setOnMouseClicked(event -> togglePasswordVisibility(passwordField1, eyeIcon1));
        eyeIcon2.setOnMouseClicked(event -> togglePasswordVisibility(passwordField2, eyeIcon2));
    }

    public void passwordField_init() {
        setPromptText();
    }

    private void togglePasswordVisibility(PasswordField passwordField, ImageView eyeIcon) {
        // First, get the parent of the passwordField and cast it to HBox (or any
        // appropriate layout type)
        Parent parent = passwordField.getParent();

        // Ensure that the parent is a container (HBox, VBox, etc.)
        if (parent instanceof HBox) {
            HBox parentHBox = (HBox) parent; // Cast to HBox

            // If the password field is currently visible, hide it and add a TextField
            if (passwordField.isVisible()) {
                passwordField.setVisible(false); // Hide the PasswordField

                // Create a new TextField to show the password
                TextField textField = new TextField(passwordField.getText());
                textField.setVisible(true); // Make TextField visible

                // Add the new TextField to the HBox
                parentHBox.getChildren().add(textField);
            } else {
                passwordField.setVisible(true); // Show the PasswordField

                // Find the TextField inside the HBox and remove it
                for (int i = 0; i < parentHBox.getChildren().size(); i++) {
                    if (parentHBox.getChildren().get(i) instanceof TextField) {
                        TextField textField = (TextField) parentHBox.getChildren().get(i);
                        parentHBox.getChildren().remove(textField); // Remove the TextField
                        break;
                    }
                }
            }

            // Change the eye icon based on visibility
            if (eyeIcon.getImage().getUrl().contains("hide-password.png")) {
                eyeIcon.setImage(
                        new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm()));
            } else {
                eyeIcon.setImage(
                        new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm()));
            }
        } else {
            System.err.println("Parent is not an HBox! Unable to toggle visibility.");
        }
    }

    // Helper method to set prompt text for all password fields
    private void setPromptText() {
        passwordField0.setPromptText("Enter your current password");
        passwordField1.setPromptText("Your new password must be over 6 letters");
        passwordField2.setPromptText("Retype your new password");
    }

    private void resetText() {
        passwordField0.clear();
        passwordField1.clear();
        passwordField2.clear();
    }

    @FXML
    private void changePassword() {
        // Lấy giá trị từ các trường nhập liệu
        String currentPassword = passwordField0.getText();
        String newPassword = passwordField1.getText();
        String confirmPassword = passwordField2.getText();
        boolean canUpdate = true;
        String errorMessage = "";

        // Kiểm tra các điều kiện đầu vào và highlight các trường hợp lỗi
        if (!isValidCurrentPassword(currentPassword)) {
            errorMessage += "Current password is required.\n";
            canUpdate = false;
        }

        if (!isValidNewPassword(newPassword)) {
            errorMessage += "New password must be at least 6 characters.\n";
            canUpdate = false;
        }

        if (!isValidConfirmPassword(confirmPassword)) {
            errorMessage += "Confirmation password is required and should match new password.\n";
            canUpdate = false;
        }

        // Kiểm tra mật khẩu hiện tại từ cơ sở dữ liệu
        if (!checkCurrentPassword(Model.getInstance().getClient().getUsername(), currentPassword)) {
            errorMessage += "Current password is incorrect.\n";
            canUpdate = false;
        }

        // Kiểm tra nếu tất cả các điều kiện đều hợp lệ
        if (canUpdate) {
            // Kiểm tra nếu mật khẩu xác nhận trùng khớp
            if (!newPassword.equals(confirmPassword)) {
                errorMessage += "New password and confirmation do not match.\n";
                canUpdate = false;
            }

            // Cập nhật mật khẩu mới vào cơ sở dữ liệu
            if (canUpdate) {
                if (updatePassword(Model.getInstance().getClient().getUsername(), newPassword)) {
                    showAlert("Password changed successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error updating password. Please try again.", Alert.AlertType.ERROR);
                }
            }
        }

        // Nếu có lỗi, hiển thị thông báo lỗi
        if (!canUpdate) {
            showAlert("Please correct the following errors:\n" + errorMessage, Alert.AlertType.ERROR);
        }

        resetText();
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
        alert.setHeaderText(null); // Có thể bỏ trống hoặc thiết lập header nếu cần
        alert.setContentText(message);
        alert.showAndWait(); // Chờ người dùng đóng thông báo trước khi tiếp tục
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
                String storedPasswordHash = resultSet.getString("password_hash"); // Mật khẩu đã được hash trong DB

                // Kiểm tra mật khẩu đầu vào với mật khẩu đã lưu (bằng cách xác minh hash)
                return verifyPassword(password, storedPasswordHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu không tìm thấy người dùng hoặc có lỗi, trả về false
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
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt()); // Mã hóa mật khẩu mới

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return false;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, hashedPassword); // Đặt mật khẩu đã mã hóa
            preparedStatement.setString(2, username); // Đặt tên người dùng

            // Thực thi câu lệnh update
            int rowsUpdated = preparedStatement.executeUpdate();

            // Nếu có ít nhất một dòng bị ảnh hưởng, tức là mật khẩu đã được cập nhật
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi hoặc không có dòng nào bị cập nhật
    }
}