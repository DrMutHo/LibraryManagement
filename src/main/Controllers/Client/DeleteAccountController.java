package main.Controllers.Client;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import javafx.application.Platform;
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

public class DeleteAccountController implements Initializable {
    @FXML
    private PasswordField passwordField0;
    @FXML
    private Button deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField_init();
    }

    public void passwordField_init() {
        setPromptText();

        deleteButton.setOnAction(event -> handleDeleteButton());
    }

    // Helper method to set prompt text for all password fields
    private void setPromptText() {
        passwordField0.setPromptText("Enter your current password");
    }

    // Method to check current password
    private boolean checkCurrentPassword(String username, String password) {
        // Query database to get user's hashed password
        String query = "SELECT * FROM client WHERE username = ?";
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Check connection validity
            if (connection == null || connection.isClosed()) {
                System.err.println("Invalid database connection!");
                return false;
            }

            // Set parameters for the query
            preparedStatement.setString(1, username);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If user is found, compare the password
            if (resultSet.next()) {
                String storedPasswordHash = resultSet.getString("password_hash");

                // Verify password with hash
                return verifyPassword(password, storedPasswordHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Verify password with stored hash
    private boolean verifyPassword(String password, String storedPasswordHash) {
        return BCrypt.checkpw(password, storedPasswordHash);
    }

    // Action when the delete button is pressed
    @FXML
    private void handleDeleteButton() {
        String username = Model.getInstance().getClient().getUsername(); // Replace with actual current username logic
        String password = passwordField0.getText();

        // Verify password
        if (checkCurrentPassword(username, password)) {
            // Show confirmation dialog with Yes, No, and Cancel options
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete your account?");
            alert.setContentText("This action cannot be undone.");

            // Add Yes, No, and Cancel buttons
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

            // Wait for the user's response
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // Delete account from database
                    deleteAccount(username);
                    Model.getInstance().getViewFactory().resetAllPanes();
                    // Navigate to login screen
                    Stage stage = (Stage) deleteButton.getScene().getWindow();
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
                    }, Model.getInstance().getViewFactory().getDeleteAccountView());
                } else if (response == ButtonType.CANCEL) {
                    // Do nothing, simply close the alert
                    alert.close();
                }
            });
        } else {
            // Show error if password is incorrect
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect password");
            alert.setContentText("The entered password is incorrect.");
            alert.showAndWait();
        }
    }

    private void deleteAccount(String username) {
        String deleteTransactionsQuery = "DELETE FROM borrowtransaction WHERE client_id = (SELECT client_id FROM client WHERE username = ?)";
        String deleteNotificationRequestsQuery = "DELETE FROM notificationrequest WHERE client_id = (SELECT client_id FROM client WHERE username = ?)";
        String deleteAccountQuery = "DELETE FROM client WHERE username = ?";

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement deleteTransactionsStatement = connection.prepareStatement(deleteTransactionsQuery);
                PreparedStatement deleteNotificationRequestsStatement = connection
                        .prepareStatement(deleteNotificationRequestsQuery);
                PreparedStatement deleteAccountStatement = connection.prepareStatement(deleteAccountQuery)) {

            // Check connection validity
            if (connection == null || connection.isClosed()) {
                System.err.println("Invalid database connection!");
                return;
            }

            // Step 1: Delete related borrow transactions
            deleteTransactionsStatement.setString(1, username);
            int rowsAffectedInTransactions = deleteTransactionsStatement.executeUpdate();
            if (rowsAffectedInTransactions > 0) {
                System.out.println("Related borrow transactions deleted successfully.");
            }

            // Step 2: Delete related notification requests
            deleteNotificationRequestsStatement.setString(1, username);
            int rowsAffectedInNotificationRequests = deleteNotificationRequestsStatement.executeUpdate();
            if (rowsAffectedInNotificationRequests > 0) {
                System.out.println("Related notification requests deleted successfully.");
            }

            // Step 3: Delete the account from the client table
            deleteAccountStatement.setString(1, username);
            int rowsAffectedInAccount = deleteAccountStatement.executeUpdate();
            if (rowsAffectedInAccount > 0) {
                System.out.println("Account deleted successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}