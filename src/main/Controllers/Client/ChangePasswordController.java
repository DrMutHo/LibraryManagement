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

/**
 * Controller class for the Change Password view.
 * Handles functionality for changing the user's password, including toggling password visibility
 * and validating new password inputs.
 */
public class ChangePasswordController implements Initializable {

    /**
     * Current password field.
     */
    @FXML
    private PasswordField passwordField0;

    /**
     * New password field.
     */
    @FXML
    private PasswordField passwordField1;

    /**
     * Retype new password field.
     */
    @FXML
    private PasswordField passwordField2;

    /**
     * TextField for showing current password when visibility is toggled.
     */
    @FXML
    private TextField textField0;

    /**
     * TextField for showing new password when visibility is toggled.
     */
    @FXML
    private TextField textField1;

    /**
     * TextField for showing retyped new password when visibility is toggled.
     */
    @FXML
    private TextField textField2;

    /**
     * Button to toggle visibility for current password field.
     */
    @FXML
    private Button toggleButton0;

    /**
     * Button to toggle visibility for new password field.
     */
    @FXML
    private Button toggleButton1;

    /**
     * Button to toggle visibility for retyped new password field.
     */
    @FXML
    private Button toggleButton2;

    /**
     * Button to save the password changes.
     */
    @FXML
    private Button saveChangesButton;

    /**
     * ImageView for the visibility icon in the current password field.
     */
    @FXML
    private ImageView imageView0;

    /**
     * ImageView for the visibility icon in the new password field.
     */
    @FXML
    private ImageView imageView1;

    /**
     * ImageView for the visibility icon in the retyped new password field.
     */
    @FXML
    private ImageView imageView2;

    /**
     * Icon for hiding password (eye closed).
     */
    @FXML
    private Image eyeOpen;

    /**
     * Icon for showing password (eye open).
     */
    @FXML
    private Image eyeClosed;

    /**
     * HBox container for current password field and toggle button.
     */
    @FXML
    private HBox hBox0;

    /**
     * HBox container for new password field and toggle button.
     */
    @FXML
    private HBox hBox1;

    /**
     * HBox container for retyped new password field and toggle button.
     */
    @FXML
    private HBox hBox2;

    /**
     * Warning icon for current password field validation.
     */
    @FXML
    private ImageView warning0;

    /**
     * Warning icon for new password field validation.
     */
    @FXML
    private ImageView warning1;

    /**
     * Warning icon for retyped new password field validation.
     */
    @FXML
    private ImageView warning2;

    /**
     * Flag to track visibility of the current password field.
     */
    private boolean passwordVisible0 = false;

    /**
     * Flag to track visibility of the new password field.
     */
    private boolean passwordVisible1 = false;

    /**
     * Flag to track visibility of the retyped new password field.
     */
    private boolean passwordVisible2 = false;

    /**
     * Initializes the controller class.
     * Sets up the password fields and toggle buttons.
     * 
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField_init();
    }

    /**
     * Initializes password fields and their corresponding text fields, toggle buttons, and icons.
     */
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

    /**
     * Sets the prompt text for all password fields.
     */
    /**
     * Helper method to set prompt text for all password fields.
     * Sets appropriate placeholder messages for the password fields and corresponding text fields.
     */
    private void setPromptText() {
        passwordField0.setPromptText("Enter your current password");
        passwordField1.setPromptText("Your new password must be over 6 letters");
        passwordField2.setPromptText("Retype your new password");
        textField0.setPromptText("Enter your current password");
        textField1.setPromptText("Your new password must be over 6 letters");
        textField2.setPromptText("Retype your new password");
    }

    /**
     * Helper method to initialize password fields and text fields.
     * Configures visibility, binding, and focus listeners for the fields.
     * 
     * @param passwordField The PasswordField to initialize.
     * @param textField The TextField synchronized with the PasswordField.
     * @param hBox The HBox containing the password field and toggle button.
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
     * Adds a focus listener to a {@link PasswordField} to update the style of the surrounding {@link HBox}.
     * When the {@link PasswordField} gains focus, a specified style class is added to the {@link HBox}.
     * When the {@link PasswordField} loses focus, the style class is removed.
     *
     * @param passwordField the {@link PasswordField} to which the focus listener is added
     * @param hbox          the {@link HBox} whose style will be updated based on the focus state of the {@link PasswordField}
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
     * Adds a focus listener to a {@link TextField} to update the style of the surrounding {@link HBox}.
     * When the {@link TextField} gains focus, a specified style class is added to the {@link HBox}.
     * When the {@link TextField} loses focus, the style class is removed.
     *
     * @param textField the {@link TextField} to which the focus listener is added
     * @param hbox      the {@link HBox} whose style will be updated based on the focus state of the {@link TextField}
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
     * Toggles the visibility of the {@link PasswordField} and the {@link TextField} to switch between
     * displaying and hiding the password. Updates the icon of the provided {@link ImageView} to indicate
     * the current visibility state.
     *
     * When the {@link PasswordField} is visible:
     * - It is hidden and the {@link TextField} is shown, displaying the password as plain text.
     * - The {@link ImageView} is updated to show the "eye-open" icon.
     *
     * When the {@link TextField} is visible:
     * - It is hidden and the {@link PasswordField} is shown, masking the password.
     * - The {@link ImageView} is updated to show the "eye-closed" icon.
     *
     * @param passwordField the {@link PasswordField} used to hide the password
     * @param textField     the {@link TextField} used to show the password in plain text
     * @param imageView     the {@link ImageView} used to display the visibility toggle icon
     */
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


    /**
     * Handles the process of changing the password. This method retrieves input values, validates them,
     * highlights errors, and updates the password if all conditions are met.
     *
     * <p>Steps:</p>
     * <ul>
     *   <li>Retrieves the current password, new password, and confirmation password from the input fields.</li>
     *   <li>Validates the inputs using appropriate validation methods:
     *     <ul>
     *       <li>{@code isValidCurrentPassword(String)} for the current password.</li>
     *       <li>{@code isValidNewPassword(String)} for the new password.</li>
     *       <li>{@code isValidConfirmPassword(String)} for the confirmation password.</li>
     *     </ul>
     *   </li>
     *   <li>Highlights any fields with errors using {@code highlightField(HBox, Label)}.</li>
     *   <li>Checks the current password against the database using {@code checkCurrentPassword(String, String)}.</li>
     *   <li>If all conditions are valid, updates the password in the database using {@code updatePassword(String, String)}.</li>
     *   <li>Displays appropriate alerts based on the outcome of the process.</li>
     * </ul>
     *
     * @implNote This method assumes the existence of helper methods such as:
     *           {@code highlightField(HBox, Label)}, {@code resetField(HBox, Label)},
     *           {@code isValidCurrentPassword(String)}, {@code isValidNewPassword(String)},
     *           {@code isValidConfirmPassword(String)}, {@code checkCurrentPassword(String, String)},
     *           and {@code updatePassword(String, String)}.
     */
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

    /**
     * Validates the current password to ensure it is not empty.
     * This is a basic check to ensure that the user has entered a value for the current password.
     *
     * @param currentPassword the current password entered by the user
     * @return {@code true} if the current password is not empty, {@code false} otherwise
     */
    private boolean isValidCurrentPassword(String currentPassword) {
        return !currentPassword.isEmpty();
    }


    /**
     * Validates the new password to ensure it is not empty and has a minimum length of 6 characters.
     *
     * @param newPassword the new password entered by the user
     * @return {@code true} if the new password is valid (non-empty and at least 6 characters), {@code false} otherwise
     */
    private boolean isValidNewPassword(String newPassword) {
        return !newPassword.isEmpty() && newPassword.length() >= 6;
    }

    /**
     * Validates the confirm password to ensure it is not empty and has a minimum length of 6 characters.
     *
     * @param confirmPassword the confirm password entered by the user
     * @return {@code true} if the confirm password is valid (non-empty and at least 6 characters), {@code false} otherwise
     */
    private boolean isValidConfirmPassword(String confirmPassword) {
        return !confirmPassword.isEmpty() && confirmPassword.length() >= 6;
    }

    /**
     * Highlights the given {@link HBox} and updates the {@link ImageView} icon to indicate an error.
     * This is used when the input field has invalid data.
     *
     * @param hbox  the {@link HBox} containing the input field to highlight
     * @param icon  the {@link ImageView} to display the error icon
     */
    private void highlightField(HBox hbox, ImageView icon) {
        hbox.getStyleClass().add("hbox_set-error");
        icon.setImage(new Image(getClass().getResource("/resources/Images/warning-icon.png").toExternalForm()));
    }

    /**
     * Resets the given {@link HBox} and {@link ImageView} to their default state when the input is valid.
     * This removes the error highlight and icon.
     *
     * @param hbox  the {@link HBox} containing the input field to reset
     * @param icon  the {@link ImageView} to remove the error icon
     */
    private void resetField(HBox hbox, ImageView icon) {
        hbox.getStyleClass().remove("hbox_set-error");
        icon.setImage(null);
    }


    /**
     * Displays an alert with the given message and alert type.
     * The alert will have a title of "Notification" and the provided message as the content.
     * The header text is left null, but can be customized if needed.
     *
     * @param message the message to be displayed in the alert
     * @param alertType the type of alert (e.g., INFORMATION, ERROR)
     */
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null); // Có thể bỏ trống hoặc thiết lập header nếu cần
        alert.setContentText(message);
        alert.showAndWait(); // Chờ người dùng đóng thông báo trước khi tiếp tục
    }

    /**
     * Checks if the provided current password matches the stored password hash in the database.
     *
     * @param username the username of the client whose password is being checked
     * @param password the current password entered by the user
     * @return {@code true} if the password is valid, {@code false} otherwise
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
     * Verifies the password by comparing it with the stored password hash.
     * Assumes the use of a hashing library like BCrypt or PBKDF2.
     *
     * @param password the password entered by the user
     * @param storedPasswordHash the stored hash of the password in the database
     * @return {@code true} if the password matches the hash, {@code false} otherwise
     */
    private boolean verifyPassword(String password, String storedPasswordHash) {
        // Giả sử bạn dùng thư viện bcrypt hoặc PBKDF2 để so sánh hash
        // Ví dụ sử dụng BCrypt để xác thực mật khẩu
        return BCrypt.checkpw(password, storedPasswordHash);
    }

    /**
     * Updates the password of the given user in the database.
     *
     * @param username the username of the client whose password is being updated
     * @param newPassword the new password to set
     * @return {@code true} if the password update was successful, {@code false} otherwise
     */
    private boolean updatePassword(String username, String newPassword) {
        if (Model.getInstance().getDatabaseDriver().updatePassword(username, newPassword) > 0) {
            return true;
        } else {
            return false;
        }
    }
}