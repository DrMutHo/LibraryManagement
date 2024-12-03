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

import org.apache.poi.ss.formula.functions.Mode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.Models.Model;

/**
 * The {@code EditProfileController} class handles the user interactions for editing
 * the client's profile, including updating address, phone number, and email.
 * <p>
 * It validates user input, interacts with the database to update profile information,
 * and provides feedback to the user through alerts.
 * </p>
 */
public class EditProfileController {
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

    /**
     * Initializes the prompt text for the username and password fields.
     */
    public void username_password_promptext_init() {
        // Set prompt text for the username and password fields
        setPromptText();
    }

    /**
     * Clears the text fields for address, phone number, and email.
     */
    public void resetText() {
        addressField.clear();
        phoneField.clear();
        emailField.clear();
    }

    /**
     * Sets the prompt text for the address, phone number, and email fields.
     */
    private void setPromptText() {
        addressField.setPromptText("Enter your new address");
        phoneField.setPromptText("Enter your new phone number");
        emailField.setPromptText("Enter your new email (abc@gmail.com)");
    }

    /**
     * Validates the email using an external API.
     *
     * @param email The email address to validate.
     * @return {@code true} if the email is valid and free; {@code false} otherwise.
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
     * Checks if the email is unique in the database.
     *
     * @param email The email address to check.
     * @return {@code true} if the email is not present in the database; {@code false} otherwise.
     */
    private boolean isValidEmailDatabase(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        int count = Model.getInstance().getDatabaseDriver().getEmailCountFromDatabase(email);
        return count == 0;
    }

    /**
     * Combines API and database validations to determine if the email is valid.
     *
     * @param email The email address to validate.
     * @return {@code true} if the email passes both API and database validations; {@code false} otherwise.
     */
    private boolean isValidEmail(String email) {
        return isValidEmailApi(email) && isValidEmailDatabase(email);
    }

    /**
     * Validates the phone number to ensure it consists of exactly 10 digits.
     *
     * @param phoneNumber The phone number to validate.
     * @return {@code true} if the phone number is valid; {@code false} otherwise.
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() == 10 && phoneNumber.matches("[0-9]+");
    }

    /**
     * Validates that the address is not empty or null.
     *
     * @param address The address to validate.
     * @return {@code true} if the address is valid; {@code false} otherwise.
     */
    private boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    /**
     * Handles the event when the "Change Address" button is clicked.
     * <p>
     * This method validates the new address and updates it in the database if valid.
     * The validation and update operations are performed on background threads to
     * prevent blocking the UI.
     * </p>
     */
    @FXML
    private void handleChangeAddressButtonClick() {
        String newAddress = addressField.getText().trim();
        String username = Model.getInstance().getClient().getUsername(); // Get current username

        // Create a Task to validate the address on a background thread
        Task<Boolean> validateAddressTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return isValidAddress(newAddress);
            }
        };

        // Handle the result of the address validation Task
        validateAddressTask.setOnSucceeded(event -> {
            boolean isValid = validateAddressTask.getValue();
            if (isValid) {
                // Create a Task to update the address on a background thread
                Task<Boolean> updateAddressTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return updateAddress(newAddress, username);
                    }
                };

                // Handle the result of the address update Task
                updateAddressTask.setOnSucceeded(updateEvent -> {
                    boolean isUpdated = updateAddressTask.getValue();
                    if (isUpdated) {
                        showAlert(AlertType.INFORMATION, "Success", "Address updated successfully!");
                    } else {
                        showAlert(AlertType.ERROR, "Error", "Unable to update address.");
                    }
                    resetText();
                });

                // Handle failures in the address update Task
                updateAddressTask.setOnFailed(updateEvent -> {
                    Throwable exception = updateAddressTask.getException();
                    showAlert(AlertType.ERROR, "Error", "An error occurred while updating address: " + 
                        (exception != null ? exception.getMessage() : "Unknown error."));
                    resetText();
                });

                // Execute the address update Task on a background thread
                new Thread(updateAddressTask).start();

            } else {
                // Address is invalid, show a warning alert
                showAlert(AlertType.WARNING, "Warning", "Please enter a valid address.");
                resetText();
            }
        });

        // Handle failures in the address validation Task
        validateAddressTask.setOnFailed(event -> {
            Throwable exception = validateAddressTask.getException();
            showAlert(AlertType.ERROR, "Error", "An error occurred while validating address: " + 
                (exception != null ? exception.getMessage() : "Unknown error."));
            resetText();
        });

        // Execute the address validation Task on a background thread
        new Thread(validateAddressTask).start();
    }

    /**
     * Handles the event when the "Change Phone Number" button is clicked.
     * <p>
     * This method validates the new phone number and updates it in the database if valid.
     * The validation and update operations are performed on background threads to
     * prevent blocking the UI.
     * </p>
     */
    @FXML
    private void handleChangePhoneButtonClick() {
        String newPhoneNumber = phoneField.getText().trim();
        String username = Model.getInstance().getClient().getUsername(); // Get current username

        // Create a Task to validate the phone number on a background thread
        Task<Boolean> validatePhoneTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return isValidPhoneNumber(newPhoneNumber);
            }
        };

        // Handle the result of the phone number validation Task
        validatePhoneTask.setOnSucceeded(event -> {
            boolean isValid = validatePhoneTask.getValue();
            if (isValid) {
                // Create a Task to update the phone number on a background thread
                Task<Boolean> updatePhoneTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return updatePhoneNumber(newPhoneNumber, username);
                    }
                };

                // Handle the result of the phone number update Task
                updatePhoneTask.setOnSucceeded(updateEvent -> {
                    boolean isUpdated = updatePhoneTask.getValue();
                    if (isUpdated) {
                        showAlert(AlertType.INFORMATION, "Success", "Phone number updated successfully!");
                    } else {
                        showAlert(AlertType.ERROR, "Error", "Unable to update phone number.");
                    }
                    resetText();
                });

                // Handle failures in the phone number update Task
                updatePhoneTask.setOnFailed(updateEvent -> {
                    Throwable exception = updatePhoneTask.getException();
                    showAlert(AlertType.ERROR, "Error", "An error occurred while updating phone number: " 
                        + (exception != null ? exception.getMessage() : "Unknown error."));
                    resetText();
                });

                // Execute the phone number update Task on a background thread
                new Thread(updatePhoneTask).start();

            } else {
                // Phone number is invalid, show a warning alert
                showAlert(AlertType.WARNING, "Warning", "Please enter a valid phone number.");
                resetText();
            }
        });

        // Handle failures in the phone number validation Task
        validatePhoneTask.setOnFailed(event -> {
            Throwable exception = validatePhoneTask.getException();
            showAlert(AlertType.ERROR, "Error", "An error occurred while validating phone number: " 
                + (exception != null ? exception.getMessage() : "Unknown error."));
            resetText();
        });

        // Execute the phone number validation Task on a background thread
        new Thread(validatePhoneTask).start();
    }

    /**
     * Handles the event when the "Change Email" button is clicked.
     * <p>
     * This method validates the new email and updates it in the database if valid.
     * The validation and update operations are performed on background threads to
     * prevent blocking the UI.
     * </p>
     */
    @FXML
    private void handleChangeEmailButtonClick() {
        String newEmail = emailField.getText().trim();
        String username = Model.getInstance().getClient().getUsername(); // Get current username

        // Create a Task to validate the email on a background thread
        Task<Boolean> validateEmailTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return isValidEmail(newEmail);
            }
        };

        // Handle the result of the email validation Task
        validateEmailTask.setOnSucceeded(event -> {
            boolean isValid = validateEmailTask.getValue();
            if (isValid) {
                // Create a Task to update the email on a background thread
                Task<Boolean> updateEmailTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return updateEmail(newEmail, username);
                    }
                };

                // Handle the result of the email update Task
                updateEmailTask.setOnSucceeded(updateEvent -> {
                    boolean isUpdated = updateEmailTask.getValue();
                    if (isUpdated) {
                        showAlert(AlertType.INFORMATION, "Success", "Email updated successfully!");
                    } else {
                        showAlert(AlertType.ERROR, "Error", "Unable to update email.");
                    }
                    resetText();
                });

                // Handle failures in the email update Task
                updateEmailTask.setOnFailed(updateEvent -> {
                    Throwable exception = updateEmailTask.getException();
                    showAlert(AlertType.ERROR, "Error", "An error occurred while updating email: " 
                        + (exception != null ? exception.getMessage() : "Unknown error."));
                    resetText();
                });

                // Execute the email update Task on a background thread
                new Thread(updateEmailTask).start();

            } else {
                // Email is invalid or already in use, show a warning alert
                showAlert(AlertType.WARNING, "Warning", "Invalid or already used email. Please enter a valid email.");
                resetText();
            }
        });

        // Handle failures in the email validation Task
        validateEmailTask.setOnFailed(event -> {
            Throwable exception = validateEmailTask.getException();
            showAlert(AlertType.ERROR, "Error", "An error occurred while validating email: " 
                + (exception != null ? exception.getMessage() : "Unknown error."));
            resetText();
        });

        // Execute the email validation Task on a background thread
        new Thread(validateEmailTask).start();
    }

    /**
     * Updates the address of the user in the database.
     *
     * @param newAddress The new address to set.
     * @param username   The username of the user whose address is being updated.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     */
    private boolean updateAddress(String newAddress, String username) {
        int isUpdate = Model.getInstance().getDatabaseDriver().updateAddress(newAddress, username);
        return isUpdate > 0;
    }

    /**
     * Updates the phone number of the user in the database.
     *
     * @param newPhoneNumber The new phone number to set.
     * @param username       The username of the user whose phone number is being updated.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     */
    private boolean updatePhoneNumber(String newPhoneNumber, String username) {
        int isUpdate = Model.getInstance().getDatabaseDriver().updatePhoneNumber(newPhoneNumber, username);
        return isUpdate > 0;
    }

    /**
     * Updates the email of the user in the database.
     *
     * @param newEmail The new email to set.
     * @param username The username of the user whose email is being updated.
     * @return {@code true} if the update was successful; {@code false} otherwise.
     */
    private boolean updateEmail(String newEmail, String username) {
        int isUpdate = Model.getInstance().getDatabaseDriver().updateEmail(newEmail, username);
        return isUpdate > 0;
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of alert to display (e.g., INFORMATION, WARNING, ERROR).
     * @param title     The title of the alert dialog.
     * @param message   The message content of the alert.
     */
    private void showAlert(AlertType alertType, String title, String message) {
        // Create an Alert with the specified type
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message); // Set the alert message

        // Choose an appropriate icon based on the AlertType
        ImageView warningIcon = new ImageView();

        // Change the image based on the type of alert
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

        // Set the size for the icon
        warningIcon.setFitHeight(30); // Set image height
        warningIcon.setFitWidth(30); // Set image width
        alert.setGraphic(warningIcon); // Add the image to the Alert

        // Change the background of the Alert to white
        alert.getDialogPane().setStyle("-fx-background-color: white;"); // White background

        // Display the Alert
        alert.showAndWait();
    }
}
