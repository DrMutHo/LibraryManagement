package main.Controllers.Client;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Models.Model;

/**
 * Controller for managing the account deletion process.
 * This class handles the UI components and user actions for deleting an account, 
 * including the password input field behavior and visibility toggle.
 */
public class DeleteAccountController implements Initializable {

    @FXML
    private HBox hBox0;

    @FXML
    private PasswordField passwordField0;

    @FXML
    private TextField textField0;

    @FXML
    private Button deleteButton;

    @FXML
    private Button toggleButton0;

    @FXML
    private Image eyeOpen;

    @FXML
    private Image eyeClosed;

    @FXML
    private ImageView imageView0;

    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller after the root element has been processed.
     * This method sets up the initial states for password fields, button actions,
     * and image icons for the toggle password visibility feature.
     * 
     * @param url The location used to resolve relative paths for the root object,
     *            or {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object,
     *                       or {@code null} if the resources are not required.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField_init();
    }

    /**
     * Initializes the password field and sets up the prompt text, 
     * visibility toggle for password fields, and button actions.
     */
    public void passwordField_init() {
        // Set prompt text for the password fields
        setPromptText();

        // Initialize password fields and text fields
        initializePasswordAndTextFields(passwordField0, textField0, hBox0);

        // Load eye icon images for showing/hiding passwords
        eyeClosed = new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm());
        eyeOpen = new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm());

        // Set initial icon state
        imageView0.setImage(eyeClosed);

        // Set toggle button actions
        toggleButton0.setOnAction(event -> togglePasswordVisibility());
        deleteButton.setOnAction(event -> handleDeleteButton());
    }

    /**
     * Helper method to set prompt text for the password fields.
     */
    private void setPromptText() {
        passwordField0.setPromptText("Enter your current password");
        textField0.setPromptText("Enter your current password");
    }

    /**
     * Initializes the password field and text field visibility and management.
     * Binds the text properties of the password field and text field.
     * 
     * @param passwordField The password field to be initialized.
     * @param textField The text field to be initialized.
     * @param hBox The HBox containing the password and text fields.
     */
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

    /**
     * Focus listener for the password field. Adds a style class when the password field gains focus
     * and removes it when the field loses focus.
     * 
     * @param passwordField The password field whose focus changes are being tracked.
     * @param hbox The HBox containing the password field, used to apply/remove the focus style.
     */
    private void addPasswordFieldFocusListener(PasswordField passwordField, HBox hbox) {
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox.getStyleClass().add("hbox_set-focused");
            } else {
                hbox.getStyleClass().remove("hbox_set-focused");
            }
        });
    }

    /**
     * Focus listener for the text field. Adds a style class when the text field gains focus
     * and removes it when the field loses focus.
     * 
     * @param textField The text field whose focus changes are being tracked.
     * @param hbox The HBox containing the text field, used to apply/remove the focus style.
     */
    private void addTextFieldListener(TextField textField, HBox hbox) {
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox.getStyleClass().add("hbox_set-focused");
            } else {
                hbox.getStyleClass().remove("hbox_set-focused");
            }
        });
    }

    /**
     * Checks if the provided password matches the stored password for the given username.
     * 
     * @param username The username whose password is being verified.
     * @param password The password entered by the user.
     * @return {@code true} if the password matches the stored hash, {@code false} otherwise.
     */
    private boolean checkCurrentPassword(String username, String password) {
        try (ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientData(username)) {
            if (resultSet != null && resultSet.next()) {
                String storedPasswordHash = resultSet.getString("password_hash");
                return verifyPassword(password, storedPasswordHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifies the provided password against a stored password hash.
     * 
     * @param password The plain text password entered by the user.
     * @param storedPasswordHash The stored password hash to compare against.
     * @return {@code true} if the password matches the stored hash, {@code false} otherwise.
     */
    private boolean verifyPassword(String password, String storedPasswordHash) {
        return BCrypt.checkpw(password, storedPasswordHash);
    }

    /**
     * Toggles the visibility of the password in the password field.
     * If the password is currently visible, it hides the password, and vice versa.
     * Updates the icon in the image view accordingly.
     */
    @FXML
    private void togglePasswordVisibility() {
        boolean isPasswordVisible = passwordField0.isVisible();
        passwordField0.setVisible(!isPasswordVisible);
        passwordField0.setManaged(!isPasswordVisible);
        textField0.setVisible(isPasswordVisible);
        textField0.setManaged(isPasswordVisible);
        imageView0.setImage(isPasswordVisible ? eyeOpen : eyeClosed);
    }


    /**
     * Handles the action when the delete button is pressed. This method verifies the user's current password,
     * prompts the user for confirmation to delete the account, and performs the deletion if confirmed.
     * 
     * The method first checks if the entered password matches the stored password. If the password is correct,
     * it shows a confirmation dialog asking the user to confirm the deletion. If the user confirms, the account
     * is deleted from the database, and the application navigates to the login screen. If the user cancels or 
     * the password is incorrect, appropriate messages are displayed.
     */
    @FXML
    private void handleDeleteButton() {
        String username = Model.getInstance().getClient().getUsername(); // Replace with actual current username logic
        String password = passwordField0.getText();

        // Tạo Task để kiểm tra mật khẩu
        Task<Boolean> verifyPasswordTask = new Task<>() {
            @Override
            protected Boolean call() {
                return checkCurrentPassword(username, password); // Hàm xác minh mật khẩu
            }
        };

        // Xử lý kết quả kiểm tra mật khẩu
        verifyPasswordTask.setOnSucceeded(event -> {
            boolean isPasswordCorrect = verifyPasswordTask.getValue();
            if (isPasswordCorrect) {
                // Hiển thị hộp thoại xác nhận
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("Are you sure you want to delete your account?");
                alert.setContentText("This action cannot be undone.");
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

                // Chờ người dùng phản hồi
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        // Tạo Task để xóa tài khoản
                        Task<Void> deleteAccountTask = new Task<>() {
                            @Override
                            protected Void call() {
                                Model.getInstance().getDatabaseDriver().deleteAccount(username);
                                return null;
                            }
                        };

                        // Xử lý sau khi xóa tài khoản thành công
                        deleteAccountTask.setOnSucceeded(deleteEvent -> {
                            // Đặt lại các giao diện
                            Model.getInstance().getViewFactory().resetAllPanes();
                            Stage stage = (Stage) deleteButton.getScene().getWindow();

                            // Hiển thị loading khi chuyển sang màn hình khác
                            Model.getInstance().getViewFactory().showLoading(() -> {
                                try {
                                    Thread.sleep(500); // Mô phỏng chờ tài nguyên
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                                Platform.runLater(() -> {
                                    Model.getInstance().getViewFactory().showLoginWindow();
                                    Model.getInstance().getViewFactory().closeStage(stage);
                                });
                            }, Model.getInstance().getViewFactory().getDeleteAccountView());
                        });

                        // Chạy Task xóa tài khoản trên luồng riêng
                        new Thread(deleteAccountTask).start();
                    } else if (response == ButtonType.CANCEL) {
                        alert.close();
                    }
                });
            } else {
                // Mật khẩu không đúng, hiển thị thông báo lỗi
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Incorrect password");
                errorAlert.setContentText("The entered password is incorrect.");
                errorAlert.showAndWait();
            }
        });
    }
}