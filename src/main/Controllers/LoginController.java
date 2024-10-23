package main.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;

public class LoginController implements Initializable {

    public ChoiceBox<AccountType> acc_selector;
    public TextField usernameField;
    public PasswordField passwordField;
    public Label usernameLabel;
    public Label passnameLabel;
    public Label chooseaccountLabel;
    public Button forgotaccountButton;
    public Button loginButton;
    public Button createnewaccountButton;
    public HBox hbox_1;
    public HBox hbox_0;
    public TextField textField;
    @FXML
    private Button togglePasswordButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo dữ liệu cho combobox acc_selector
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));

        // Đặt giá trị mặc định cho acc_selector từ viewFactory
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());

        // Chữ mờ mờ ở username khi chưa nhập gì.
        usernameField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // Khi PasswordField được focus, đổi màu HBox
                hbox_1.getStyleClass().add("hbox_set-focused");
            } else {
                // Khi PasswordField không còn focus, trở lại trạng thái ban đầu
                hbox_1.getStyleClass().remove("hbox_set-focused");
            }
        });
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // Khi PasswordField được focus, đổi màu HBox
                hbox_0.getStyleClass().add("hbox_set-focused");
            } else {
                // Khi PasswordField không còn focus, trở lại trạng thái ban đầu
                hbox_0.getStyleClass().remove("hbox_set-focused");
            }
        });
        togglePasswordVisibility();
    }
    
    @FXML
    private void togglePasswordVisibility() {
        if (passwordField.isVisible()) {
            // Nếu PasswordField đang hiển thị
            textField.setText(passwordField.getText()); // Lấy nội dung từ PasswordField
            passwordField.setVisible(false); // Ẩn PasswordField
            textField.setVisible(true); // Hiển thị TextField
        } else {
            // Nếu TextField đang hiển thị
            passwordField.setText(textField.getText()); // Lấy nội dung từ TextField
            textField.setVisible(false); // Ẩn TextField
            passwordField.setVisible(true); // Hiển thị PasswordField
        }
    }
}