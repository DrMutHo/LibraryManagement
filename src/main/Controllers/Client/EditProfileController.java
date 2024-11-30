package main.Controllers.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.Models.Model;

public class EditProfileController {
    @FXML
    private HBox hbox_0;
    @FXML
    private HBox hbox_01;
    @FXML
    private HBox hbox_02;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private Button changeAdress;
    @FXML
    private Button changePhoneNum;
    @FXML
    private Button changeEmail;
    @FXML
    private ImageView warning0;
    @FXML
    private ImageView warning01;
    @FXML
    private ImageView warning02;

    public void username_password_promptext_init() {
        // Set prompt text for the username and password fields
        setPromptText();

        // Add focus listeners for each field (addressField, phoneField, emailField)
        addTextFieldFocusListener(addressField, hbox_0);
        addTextFieldFocusListener(emailField, hbox_02);
        addTextFieldFocusListener(phoneField, hbox_01);
    }

    // Helper method to set prompt text
    private void setPromptText() {
        addressField.setPromptText("Enter your new address");
        phoneField.setPromptText("Enter your new phone number");
        emailField.setPromptText("Enter your new email(abc@gmail.com)");
    }

    // Focus listener for textField
    private void addTextFieldFocusListener(TextField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // When textField is focused, add the focus style to the corresponding HBox
                hbox.getStyleClass().add("hbox_set-focused");
            } else {
                // When focus is lost, remove the focus style from the HBox
                hbox.getStyleClass().remove("hbox_set-focused");
            }
        });
    }

    // Check if email is valid using API
    private boolean isValidEmailApi(String email) {
        boolean isValidEmailApi = false;
        if (email == null || email.trim().isEmpty()) {
            return isValidEmailApi;
        } else {
            try {
                @SuppressWarnings("deprecation")
                URL url = new URL(
                        "https://emailvalidation.abstractapi.com/v1/?api_key=fe97d39becd94b14a4a19b97ebcc29a1&email="
                                + email);
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
                    Type type = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> map = gson.fromJson(response.toString(), type);
                    Map<String, Object> isFreeEmail = (Map<String, Object>) map.get("is_free_email");

                    boolean value = (boolean) isFreeEmail.get("value");
                    isValidEmailApi = value;
                } else {
                    System.out.println("GET request not worked");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isValidEmailApi;
    }

    // Check if email is valid in database
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

    // Combine both methods to validate email
    private boolean isValidEmail(String email) {
        return isValidEmailApi(email) && isValidEmailDatabase(email);
    }

    // Check if phone number is valid (must be 10 digits)
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() == 10 && phoneNumber.matches("[0-9]+");
    }

    // Check if address is not empty
    private boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    // Handle the button click event to update the profile
    @FXML
    private void handleChangeAddressButtonClick() {
        String newAddress = addressField.getText();
        if (isValidAddress(newAddress)) {
            if (updateAddress(newAddress, Model.getInstance().getClient().getUsername())) {
                showAlert(AlertType.INFORMATION, "Success", "Address updated successfully!");
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to update address.");
            }
        } else {
            showAlert(AlertType.WARNING, "Warning", "Please enter a valid address.");
        }
    }

    @FXML
    private void handleChangePhoneButtonClick() {
        String newPhoneNumber = phoneField.getText();
        if (isValidPhoneNumber(newPhoneNumber)) {
            if (updatePhoneNumber(newPhoneNumber, Model.getInstance().getClient().getUsername())) {
                showAlert(AlertType.INFORMATION, "Success", "Phone number updated successfully!");
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to update phone number.");
            }
        } else {
            showAlert(AlertType.WARNING, "Warning", "Please enter a valid phone number.");
        }
    }

    @FXML
    private void handleChangeEmailButtonClick() {
        String newEmail = emailField.getText();
        if (isValidEmail(newEmail)) {
            if (updateEmail(newEmail, Model.getInstance().getClient().getUsername())) {
                showAlert(AlertType.INFORMATION, "Success", "Email updated successfully!");
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to update email.");
            }
        } else {
            showAlert(AlertType.WARNING, "Invalid Email",
                    "Invalid email address or already in use. Please create a valid email.");
        }
    }

    private boolean updateAddress(String newAddress, String username) {
        // Câu truy vấn để cập nhật địa chỉ
        String updateQuery = "UPDATE client SET address = ? WHERE username = ?";

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return false;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, newAddress); // Đặt địa chỉ mới
            preparedStatement.setString(2, username); // Đặt tên người dùng

            // Thực thi câu lệnh update
            int rowsUpdated = preparedStatement.executeUpdate();

            // Nếu có ít nhất một dòng bị ảnh hưởng, tức là địa chỉ đã được cập nhật
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu không thành công
    }

    private boolean updatePhoneNumber(String newPhoneNumber, String username) {
        // Câu truy vấn để cập nhật số điện thoại
        String updateQuery = "UPDATE client SET phone_number = ? WHERE username = ?";

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return false;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, newPhoneNumber); // Đặt số điện thoại mới
            preparedStatement.setString(2, username); // Đặt tên người dùng

            // Thực thi câu lệnh update
            int rowsUpdated = preparedStatement.executeUpdate();

            // Nếu có ít nhất một dòng bị ảnh hưởng, tức là số điện thoại đã được cập nhật
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu không thành công
    }

    private boolean updateEmail(String newEmail, String username) {
        // Câu truy vấn để cập nhật email
        String updateQuery = "UPDATE client SET email = ? WHERE username = ?";

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return false;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, newEmail); // Đặt email mới
            preparedStatement.setString(2, username); // Đặt tên người dùng

            // Thực thi câu lệnh update
            int rowsUpdated = preparedStatement.executeUpdate();

            // Nếu có ít nhất một dòng bị ảnh hưởng, tức là email đã được cập nhật
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nếu không thành công
    }

    // Show an alert with a specific type, title, and message
    private void showAlert(AlertType alertType, String title, String message) {
        // Tạo Alert với kiểu bạn cần
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Không sử dụng header text
        alert.setContentText(message); // Đặt nội dung thông báo

        // Chọn hình ảnh cảnh báo phù hợp với AlertType
        ImageView warningIcon = new ImageView();

        // Dựa trên loại cảnh báo, thay đổi hình ảnh
        switch (alertType) {
            case INFORMATION:
                warningIcon
                        .setImage(new Image(getClass().getResource("/resources/Images/success.png").toExternalForm()));
                break;
            case WARNING:
                warningIcon.setImage(
                        new Image(getClass().getResource("/resources/Images/warning-icon.png").toExternalForm()));
                break;
            default:
                warningIcon.setImage(
                        new Image(getClass().getResource("/resources/Images/warning-icon.png").toExternalForm()));
                break;
        }

        // Đặt kích thước cho hình ảnh biểu tượng
        warningIcon.setFitHeight(30); // Đặt chiều cao hình ảnh
        warningIcon.setFitWidth(30); // Đặt chiều rộng hình ảnh
        alert.setGraphic(warningIcon); // Thêm hình ảnh vào Alert

        // Đổi nền của Alert thành màu trắng
        alert.getDialogPane().setStyle("-fx-background-color: white;"); // Nền trắng

        // Hiển thị Alert
        alert.showAndWait();
    }
}