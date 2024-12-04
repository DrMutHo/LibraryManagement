package main.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.Random;

/**
 * Controller class for handling the Forgot Password functionality.
 * Implements the Initializable interface to initialize the controller after its root element has been completely processed.
 */
public class ForgotPasswordController implements Initializable {

    @FXML
    private AnchorPane outerAnchorPane;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView libImage;

    @FXML
    private Button resetPasswordButton;

    @FXML
    private ChoiceBox<AccountType> acc_selector;

    @FXML
    private HBox hBox0;

    @FXML
    private HBox hBox1;

    @FXML
    private TextField usernameField;

    @FXML
    private AnchorPane failedNotification;

    @FXML
    private AnchorPane successNotification;

    @FXML
    private Button returnToLoginButton;

    @FXML
    private Stage stage;

    @FXML
    private Button exitButton;

    @FXML
    private AnchorPane innerAnchorPane;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector_init();
        username_password_promptext_init();
        resetPasswordButton.setOnAction(event -> {
            try {
                onSendingEmailAndUpdate();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        returnToLoginButton.setOnAction(event -> onReturnToLogin());
    }

    /**
     * Initializes the account type selector with available account types.
     */
    public void acc_selector_init() {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
    }

    /**
     * Initializes the prompt text for the username field and adds focus listeners.
     */
    public void username_password_promptext_init() {
        usernameField.setPromptText("Enter your username");
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hBox0.getStyleClass().add("hbox_set-focused");
            } else {
                hBox0.getStyleClass().remove("hbox_set-focused");
            }
        });
    }

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length The length of the random string to generate.
     * @return A randomly generated string.
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // All possible characters to choose from
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length()); // Get a random index
            result.append(characters.charAt(index)); // Append the character to the result string
        }

        return result.toString();
    }

    /**
     * Handles the action of returning to the login window.
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
        }, outerAnchorPane);
    }

    /**
     * Handles the action of exiting the application.
     * Shows a loading screen during the transition.
     */
    @FXML
    private void onExit() {
        stage = (Stage) exitButton.getScene().getWindow();
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
        }, outerAnchorPane);
    }

    /**
     * Handles the action of clicking the OK button in the notification.
     * Hides the failed notification and re-enables UI components.
     */
    @FXML
    public void handleOkButtonAction() {
        failedNotification.setVisible(false);
        libImage.setVisible(true);
        enableAllComponents(innerAnchorPane);
    }

    /**
     * Enables all UI components within the specified AnchorPane.
     *
     * @param root The root AnchorPane whose child components are to be enabled.
     */
    private void enableAllComponents(AnchorPane root) {
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                for (javafx.scene.Node stackNode : stackPane.getChildren()) {
                    if (stackNode instanceof AnchorPane && ((AnchorPane) stackNode).getId() != null &&
                            ((AnchorPane) stackNode).getId().equals("failedNotification")) {
                        continue;
                    }
                }
            } else {
                node.setDisable(false);
            }
        }
    }

    /**
     * Disables all UI components within the specified AnchorPane.
     *
     * @param root The root AnchorPane whose child components are to be disabled.
     */
    private void disableAllComponents(AnchorPane root) {
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                for (javafx.scene.Node stackNode : stackPane.getChildren()) {
                    if (stackNode instanceof AnchorPane && ((AnchorPane) stackNode).getId() != null &&
                            ((AnchorPane) stackNode).getId().equals("failedNotification")) {
                        continue;
                    }
                }
            } else {
                node.setDisable(true);
            }
        }
    }

    /**
     * Handles the action of sending an email with a new password and updating the user's password in the database.
     *
     * @throws MessagingException If there is an error in sending the email.
     */
    @FXML
    private void onSendingEmailAndUpdate() throws MessagingException {
        String username = usernameField.getText();
        String newPassword = generateRandomString(6);
        String email = getEmailByUsername(username);
        if (email == null) {
            libImage.setVisible(false);
            failedNotification.setVisible(true);
            disableAllComponents(innerAnchorPane);
        } else {
            Model.getInstance().getViewFactory().showLoading(() -> {
                try {
                    sendNewPassword(email, newPassword);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    successNotification.setVisible(true);
                    updatePassword(username, newPassword);
                });
            }, outerAnchorPane);
        }
    }

    /**
     * Updates the user's password in the database with a new hashed password.
     *
     * @param username    The username of the user whose password is to be updated.
     * @param newPassword The new plaintext password to be hashed and stored.
     */
    public void updatePassword(String username, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String query = "UPDATE client SET password_hash = ? WHERE username = ?";
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, username);
            int rowsUpdated = preparedStatement.executeUpdate();
            // Optionally, you can handle the rowsUpdated if needed
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the email address associated with the given username from the database.
     *
     * @param username The username whose email address is to be retrieved.
     * @return The email address if found, otherwise null.
     */
    private String getEmailByUsername(String username) {
        String query = "SELECT email FROM client WHERE username = ?";

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends an email containing the new password to the specified recipient email address.
     *
     * @param recipientEmail The email address of the recipient.
     * @param newPassword    The new password to be sent.
     * @throws MessagingException If there is an error in sending the email.
     */
    public void sendNewPassword(String recipientEmail, String newPassword) throws MessagingException {
        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        String senderEmail = "thuha25121976@gmail.com";
        String senderPassword = "bbjh xcbp oxtj qozz";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            /**
             * Provides authentication for the mail session.
             *
             * @return A PasswordAuthentication object containing the sender's email and password.
             */
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Your New Password");
        message.setText("Dear customer,\n\nYour new password is: " + newPassword
                + "\n\nPlease log in and change your password as soon as possible.\n\nThank you.");
        Transport.send(message);
        System.out.println("Email sent successfully to " + recipientEmail);
    }
}
