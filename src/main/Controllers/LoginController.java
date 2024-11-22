package main.Controllers;

import java.io.IOException;
import java.lang.classfile.components.ClassPrinter.Node;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
        try {
            passwordField_init();
        } catch (Exception e) {
            System.err.println("Error initializing password field: " + e.getMessage());
        }
        loginButton.setOnAction(event -> onLogin());
        createnewaccountButton.setOnAction(event -> onsignUp());
        forgotaccountButton.setOnAction(event -> onResetPassword());
    }

    @FXML
    private void togglePasswordVisibility() {
        if (passwordField.isVisible()) {
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            textField.setVisible(true);
            textField.setManaged(true);
            imageIcon.setImage(eyeOpen);
        } else {
            textField.setVisible(false);
            textField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            imageIcon.setImage(eyeClosed);
        }
    }

    @FXML
    private void setAcc_selector() {
        Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue());
        if (acc_selector.getValue() == AccountType.ADMIN) {
            forgotaccountButton.setVisible(false);
            createnewaccountButton.setVisible(false);
        } else {
            forgotaccountButton.setVisible(true);
            createnewaccountButton.setVisible(true);
        }
    }


    public void acc_selector_init() {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> setAcc_selector());
    }

    public void username_password_promptext_init() {
        usernameField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");
        textField.setPromptText("Enter your password");

        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox_1.getStyleClass().add("hbox_set-focused");
            } else {
                hbox_1.getStyleClass().remove("hbox_set-focused");
            }
        });

        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox_0.getStyleClass().add("hbox_set-focused");
            } else {
                hbox_0.getStyleClass().remove("hbox_set-focused");
            }
        });

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                hbox_1.getStyleClass().add("hbox_set-focused");
            } else {
                hbox_1.getStyleClass().remove("hbox_set-focused");
            }
        });
    }

    public void passwordField_init() {
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
        
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            if (isValidCredentials(username, password)) {
                Model.getInstance().getDatabaseDriver().getClientData(username);
                Model.getInstance().evaluateClientCred(username);
                Model.getInstance().getViewFactory().showLoading(() -> {
                    try {
                        Thread.sleep(500);
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
                    Thread.sleep(1000); // Thời gian chuẩn bị tài nguyên giả lập 500ms
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

    private boolean isValidCredentials(String username, String password) {
        String query = "SELECT * FROM Client WHERE username = ?";
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return false;
            }
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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
