package main.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo dữ liệu cho combobox acc_selector
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));

        // Đặt giá trị mặc định cho acc_selector từ viewFactory
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());

        // Chữ mờ mờ ở username khi chưa nhập gì.
        usernameField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");
    }
}