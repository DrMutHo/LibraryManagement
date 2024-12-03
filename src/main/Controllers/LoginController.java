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
import javafx.stage.Stage;
import main.Models.DatabaseDriver;
import main.Models.Model;
import main.Views.AccountType;

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
    @FXML ImageView lib_image;
    private Stage stage;
    @FXML
    private AnchorPane inner_pane;

    
    
        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector_init();
        username_password_promptext_init();
        initializePasswordField();
        setButtonActions();
    }

    private void setButtonActions() {
        loginButton.setOnAction(event -> onLogin());
        createnewaccountButton.setOnAction(event -> onsignUp());
        forgotaccountButton.setOnAction(event -> onResetPassword());
        toggleButton.setOnAction(event -> togglePasswordVisibility());
    }

    @FXML
    private void setAcc_selector() {
        Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue());
        boolean isAdmin = acc_selector.getValue() == AccountType.ADMIN;
        forgotaccountButton.setVisible(!isAdmin);
        createnewaccountButton.setVisible(!isAdmin);
    }

    public void acc_selector_init() {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> setAcc_selector());
    }

    public void username_password_promptext_init() {
        setPromptText();
        addFocusListeners();
    }

    // Helper method to set prompt text
    private void setPromptText() {
        usernameField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");
        textField.setPromptText("Enter your password");
    }

    // Set up focus listeners for username and password fields
    private void addFocusListeners() {
        addTextFieldFocusListener(usernameField, hbox_0);
        addPasswordFieldFocusListener(passwordField, hbox_1);
        addTextFieldFocusListener(textField, hbox_1); // textField shares the same HBox as passwordField
    }

    // Focus listener for PasswordField
    private void addPasswordFieldFocusListener(PasswordField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> toggleFocusStyle(newVal, hbox));
    }

    // Focus listener for TextField
    private void addTextFieldFocusListener(TextField field, HBox hbox) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> toggleFocusStyle(newVal, hbox));
    }

    // Helper method to toggle focus style
    private void toggleFocusStyle(boolean isFocused, HBox hbox) {
        if (isFocused) {
            hbox.getStyleClass().add("hbox_set-focused");
        } else {
            hbox.getStyleClass().remove("hbox_set-focused");
        }
    }

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

    @FXML
    private void togglePasswordVisibility() {
        boolean isPasswordVisible = passwordField.isVisible();
        passwordField.setVisible(!isPasswordVisible);
        passwordField.setManaged(!isPasswordVisible);
        textField.setVisible(isPasswordVisible);
        textField.setManaged(isPasswordVisible);
        imageIcon.setImage(isPasswordVisible ? eyeOpen : eyeClosed);
    }

    @FXML 
    private void onResetPassword() {
        stage = (Stage) forgotaccountButton.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            Model.getInstance().getViewFactory().showLoading(() -> {
                // Giả lập thời gian chuẩn bị tài nguyên (độ trễ nhân tạo)
                try {
                    Thread.sleep(500); // Thời gian chuẩn bị tài nguyên giả lập 500ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // Công việc chính: Mở cửa sổ Sign Up và đóng cửa sổ hiện tại
                Platform.runLater(() -> {
                    Model.getInstance().getViewFactory().ShowResetPasswordWindow();;
                    Model.getInstance().getViewFactory().closeStage(stage);
                });
            }, outer_pane);
        }
    }

    @FXML
    private void onLogin() {
        stage = (Stage) loginButton.getScene().getWindow();
        String username = usernameField.getText();
        String password = passwordField.getText();
        Task<Boolean> task0 = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Thực hiện công việc trong nền (background)
                return isValidClientCredentials(username, password);
            }
        };
        
        Task<Boolean> task1 = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Thực hiện công việc trong nền (background)
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
                            Thread.sleep(1000);
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
        }  else {
            new Thread(task1).start();
            task1.setOnSucceeded(event -> {
                if (task1.getValue()) {
                    Model.getInstance().evaluateAdminCred(username);
                    Model.getInstance().getViewFactory().showLoading(() -> {
                        try {
                            Thread.sleep(1000);
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

        }
    }

    @FXML
    public void handleOkButtonAction() {
        notificationPane.setVisible(false);
        lib_image.setVisible(true);
        enableAllComponents(inner_pane);

    }
    private void disableAllComponents(AnchorPane root) {
        for (javafx.scene.Node node : root.getChildren()) {
            // Kiểm tra nếu node không phải là notificationPane và không phải con của notificationPane
            if (!(node instanceof AnchorPane && ((AnchorPane) node).getId() != null && ((AnchorPane) node).getId().equals("notificationPane"))) {
                node.setDisable(true);
            } else if (node instanceof AnchorPane && ((AnchorPane) node).getId().equals("notificationPane")) {
                // Nếu node là notificationPane, duyệt qua các con của notificationPane
                AnchorPane notificationPane = (AnchorPane) node;
                for (javafx.scene.Node notificationChild : notificationPane.getChildren()) {
                    notificationChild.setDisable(false);  // Đảm bảo các thành phần trong notificationPane không bị disable
                }
            }
        }
    }

    private void enableAllComponents(AnchorPane root) {
        for (javafx.scene.Node node : root.getChildren()) {
            // Kiểm tra nếu node không phải là notificationPane và không phải con của notificationPane
            if (!(node instanceof AnchorPane && ((AnchorPane) node).getId() != null && ((AnchorPane) node).getId().equals("notificationPane"))) {
                node.setDisable(false);
            } else if (node instanceof AnchorPane && ((AnchorPane) node).getId().equals("notificationPane")) {
                // Nếu node là notificationPane, duyệt qua các con của notificationPane
                AnchorPane notificationPane = (AnchorPane) node;
                for (javafx.scene.Node notificationChild : notificationPane.getChildren()) {
                    notificationChild.setDisable(false);  // Đảm bảo các thành phần trong notificationPane không bị disable
                }
            }
        }
    }


    private void onsignUp() {
        stage = (Stage) createnewaccountButton.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            Model.getInstance().getViewFactory().showLoading(() -> {
                // Giả lập thời gian chuẩn bị tài nguyên (độ trễ nhân tạo)
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // Công việc chính: Mở cửa sổ Sign Up và đóng cửa sổ hiện tại
                Platform.runLater(() -> {
                    Model.getInstance().getViewFactory().showSignUpWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                });
            }, outer_pane);
        }
    }

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
    
    private boolean verifyPassword(String password, String storedPasswordHash) {
        return BCrypt.checkpw(password, storedPasswordHash);
    }
}
