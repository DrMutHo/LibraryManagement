package main.Controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.classfile.components.ClassPrinter.Node;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Models.DatabaseDriver;
import main.Models.Model;
import main.Views.AccountType;

/**
 * Controller class for handling the Login functionality.
 * Implements the Initializable interface to initialize the controller after its
 * root element has been completely processed.
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane outer_pane;

    @FXML
    private ChoiceBox<AccountType> acc_selector;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label chooseaccountLabel;

    @FXML
    private Button forgotaccountButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button createnewaccountButton;

    @FXML
    private HBox hbox_1;

    @FXML
    private HBox hbox_0;

    @FXML
    private TextField textField;

    @FXML
    private Button toggleButton;

    @FXML
    private Image eyeOpen;

    @FXML
    private Image eyeClosed;

    @FXML
    private ImageView imageIcon;

    @FXML
    private Button alert_button;

    @FXML
    private AnchorPane notificationPane;

    @FXML
    ImageView lib_image;

    private Stage stage;

    @FXML
    private VBox inner_pane;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     *
     * @param url            The location used to resolve relative paths for the
     *                       root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null
     *                       if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector_init();
        username_password_promptext_init();
        initializePasswordField();
        setButtonActions();
    }

    public void reset() {
        acc_selector_init();
        username_password_promptext_init();
        initializePasswordField();
        setButtonActions();
    }

    /**
     * Sets up the actions for various buttons in the login form.
     */
    private void setButtonActions() {
        loginButton.setOnAction(event -> onLogin());
        createnewaccountButton.setOnAction(event -> onsignUp());
        forgotaccountButton.setOnAction(event -> onResetPassword());
        toggleButton.setOnAction(event -> togglePasswordVisibility());
    }

    /**
     * Sets the selected account type and updates the visibility of related buttons.
     */
    @FXML
    private void setAcc_selector() {
        Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue());
        boolean isAdmin = acc_selector.getValue() == AccountType.ADMIN;
        forgotaccountButton.setVisible(!isAdmin);
        createnewaccountButton.setVisible(!isAdmin);
    }

    /**
     * Initializes the account type selector with available account types and sets
     * up listeners.
     */
    public void acc_selector_init() {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> setAcc_selector());
    }

    /**
     * Initializes the prompt texts and focus listeners for the username and
     * password fields.
     */
    public void username_password_promptext_init() {
        setPromptText();
        addFocusListeners();
    }

    /**
     * Sets the prompt texts for the username and password fields.
     */
    private void setPromptText() {
        usernameField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");
        textField.setPromptText("Enter your password");
    }

    /**
     * Adds focus listeners to input fields to handle UI styling based on focus
     * state.
     */
    private void addFocusListeners() {
        addTextFieldFocusListener(usernameField, hbox_0);
        addPasswordFieldFocusListener(passwordField, hbox_1);
        addTextFieldFocusListener(textField, hbox_1); // textField shares the same HBox as passwordField
    }

    /**
     * Adds a focus listener to a PasswordField to update the corresponding HBox
     * style based on focus.
     *
     * @param field The PasswordField to add the listener to.
     * @param hbox  The HBox whose style is to be updated.
     */
    private void addPasswordFieldFocusListener(PasswordField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> toggleFocusStyle(newVal, hbox));
    }

    /**
     * Adds a focus listener to a TextField to update the corresponding HBox style
     * based on focus.
     *
     * @param field The TextField to add the listener to.
     * @param hbox  The HBox whose style is to be updated.
     */
    private void addTextFieldFocusListener(TextField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> toggleFocusStyle(newVal, hbox));
    }

    /**
     * Toggles the focus style of an HBox based on the focus state.
     *
     * @param isFocused Whether the field is focused.
     * @param hbox      The HBox to update.
     */
    private void toggleFocusStyle(boolean isFocused, HBox hbox) {
        if (isFocused) {
            hbox.getStyleClass().add("hbox_set-focused");
        } else {
            hbox.getStyleClass().remove("hbox_set-focused");
        }
    }

    /**
     * Initializes the password field by setting up visibility toggles and bindings.
     */
    private void initializePasswordField() {
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        textField.setVisible(false);
        textField.setManaged(false);
        textField.textProperty().bindBidirectional(passwordField.textProperty());

        eyeClosed = new Image(getClass().getResource("/resources/Images/hide-password.png").toExternalForm());
        eyeOpen = new Image(getClass().getResource("/resources/Images/show-passwords.png").toExternalForm());
        imageIcon.setImage(eyeClosed);
        toggleButton.setOnAction(event -> togglePasswordVisibility());
    }

    /**
     * Toggles the visibility of the password between hidden and visible.
     */
    @FXML
    private void togglePasswordVisibility() {
        boolean isPasswordVisible = passwordField.isVisible();
        passwordField.setVisible(!isPasswordVisible);
        passwordField.setManaged(!isPasswordVisible);
        textField.setVisible(isPasswordVisible);
        textField.setManaged(isPasswordVisible);
        imageIcon.setImage(isPasswordVisible ? eyeOpen : eyeClosed);
    }

    /**
     * Handles the action of resetting the password.
     * Opens the reset password window for client accounts.
     */
    @FXML
    private void onResetPassword() {
        stage = (Stage) forgotaccountButton.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            Model.getInstance().getViewFactory().showLoading(() -> {
                // Simulate resource preparation time (artificial delay)
                try {
                    Thread.sleep(500); // Simulated resource preparation time of 500ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Main task: Open the Reset Password window and close the current stage
                Platform.runLater(() -> {
                    Model.getInstance().getViewFactory().ShowResetPasswordWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                });
            }, outer_pane);
        }
    }

    /**
     * Handles the login action when the login button is pressed.
     * Validates the user credentials and navigates to the appropriate window based
     * on account type.
     */
    @FXML
    private void onLogin() {
        stage = (Stage) loginButton.getScene().getWindow();
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Task for validating client credentials
        Task<Boolean> task0 = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Perform background work
                return isValidClientCredentials(username, password);
            }
        };

        // Task for validating admin credentials
        Task<Boolean> task1 = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Perform background work
                return isValidAdminCredentials(username, password);
            }
        };

        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            new Thread(task0).start();
            task0.setOnSucceeded(event -> {
                if (task0.getValue()) {
                    Model.getInstance().evaluateClientCred(username);
                    Model.getInstance().getViewFactory().showLoading(() -> {
                        try {
                            Thread.sleep(1000); // Simulated delay
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        Platform.runLater(() -> {
                            Model.getInstance().getViewFactory().showClientWindow();
                            Model.getInstance().getViewFactory().closeStage(stage);
                        });
                    }, outer_pane);
                } else {
                    lib_image.setVisible(false);
                    notificationPane.setVisible(true);
                    disableAllComponents(inner_pane);
                    passwordField.clear();
                }
            });
        } else {
            new Thread(task1).start();
            task1.setOnSucceeded(event -> {
                if (task1.getValue()) {
                    Model.getInstance().evaluateAdminCred(username);
                    Model.getInstance().getViewFactory().showLoading(() -> {
                        try {
                            Thread.sleep(1000); // Simulated delay
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        Platform.runLater(() -> {
                            Model.getInstance().getViewFactory().showAdminWindow();
                            Model.getInstance().getViewFactory().closeStage(stage);
                        });
                    }, outer_pane);
                } else {
                    lib_image.setVisible(false);
                    notificationPane.setVisible(true);
                    disableAllComponents(inner_pane);
                    passwordField.clear();
                }
            });
        }
    }

    /**
     * Handles the action of creating a new account by navigating to the sign-up
     * window.
     */
    private void onsignUp() {
        stage = (Stage) createnewaccountButton.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            Model.getInstance().getViewFactory().showLoading(() -> {
                // Simulate resource preparation time (artificial delay)
                try {
                    Thread.sleep(500); // Simulated resource preparation time of 500ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Main task: Open the Sign Up window and close the current stage
                Platform.runLater(() -> {
                    Model.getInstance().getViewFactory().showSignUpWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                });
            }, outer_pane);
        }
    }

    /**
     * Handles the OK button action in the notification pane.
     * Hides the notification pane and re-enables the main UI components.
     */
    @FXML
    public void handleOkButtonAction() {
        notificationPane.setVisible(false);
        lib_image.setVisible(true);
        enableAllComponents(inner_pane);
    }

    /**
     * Disables all UI components within the specified VBox except for the
     * notification pane.
     *
     * @param root The root VBox whose child components are to be disabled.
     */
    private void disableAllComponents(VBox root) {
        for (javafx.scene.Node node : root.getChildren()) {
            // Check if node is not the notificationPane
            if (!(node instanceof AnchorPane && ((AnchorPane) node).getId() != null
                    && ((AnchorPane) node).getId().equals("notificationPane"))) {
                node.setDisable(true);
            } else if (node instanceof AnchorPane && ((AnchorPane) node).getId().equals("notificationPane")) {
                // If node is notificationPane, iterate through its children and ensure they are
                // not disabled
                AnchorPane notificationPane = (AnchorPane) node;
                for (javafx.scene.Node notificationChild : notificationPane.getChildren()) {
                    notificationChild.setDisable(false); // Ensure components inside notificationPane are not disabled
                }
            }
        }
    }

    /**
     * Enables all UI components within the specified VBox.
     *
     * @param root The root VBox whose child components are to be enabled.
     */
    private void enableAllComponents(VBox root) {
        for (javafx.scene.Node node : root.getChildren()) {
            // Check if node is not the notificationPane
            if (!(node instanceof AnchorPane && ((AnchorPane) node).getId() != null
                    && ((AnchorPane) node).getId().equals("notificationPane"))) {
                node.setDisable(false);
            } else if (node instanceof AnchorPane && ((AnchorPane) node).getId().equals("notificationPane")) {
                // If node is notificationPane, iterate through its children and ensure they are
                // enabled
                AnchorPane notificationPane = (AnchorPane) node;
                for (javafx.scene.Node notificationChild : notificationPane.getChildren()) {
                    notificationChild.setDisable(false); // Ensure components inside notificationPane are enabled
                }
            }
        }
    }

    /**
     * Validates the admin credentials against the database.
     *
     * @param username The username entered by the admin.
     * @param password The password entered by the admin.
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean isValidAdminCredentials(String username, String password) {
        try (ResultSet resultSet = Model.getInstance().getDatabaseDriver().getAdminData(username)) {
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
     * Validates the client credentials against the database.
     *
     * @param username The username entered by the client.
     * @param password The password entered by the client.
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean isValidClientCredentials(String username, String password) {
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
     * Verifies the provided password against the stored password hash using BCrypt.
     *
     * @param password           The plaintext password to verify.
     * @param storedPasswordHash The hashed password stored in the database.
     * @return true if the password matches the hash, false otherwise.
     */
    private boolean verifyPassword(String password, String storedPasswordHash) {
        return BCrypt.checkpw(password, storedPasswordHash);
    }
}
