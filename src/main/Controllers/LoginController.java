package main.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane outer_pane;
    @FXML
    private AnchorPane inner_pane;
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
    private Stage stage;

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

    private void setAcc_selector() {
        Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue());
        if (acc_selector.getValue() == AccountType.ADMIN) {
            inner_pane.getChildren().remove(forgotaccountButton);
            inner_pane.getChildren().remove(createnewaccountButton);
        } else {
            inner_pane.getChildren().add(forgotaccountButton);
            inner_pane.getChildren().add(createnewaccountButton);
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

    private void onLogin() {
        stage = (Stage) loginButton.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            Model.getInstance().getViewFactory().showClientWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        }
    }

    private void onsignUp() throws IOException {
        stage = (Stage) loginButton.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            FXMLLoader load = Model.getInstance().getViewFactory().getSignUpView();
            Scene scene= new Scene(load.load());
            Model.getInstance().getViewFactory().closeStage(stage);
        }
    }
}
