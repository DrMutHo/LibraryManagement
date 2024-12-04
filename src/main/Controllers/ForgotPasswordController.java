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

    public void acc_selector_init() {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
    }

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

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Tất cả các ký tự có thể chọn
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length()); // Lấy chỉ số ngẫu nhiên
            result.append(characters.charAt(index)); // Thêm ký tự vào chuỗi kết quả
        }
        
        return result.toString();
    }

    @FXML
    private void onReturnToLogin() {
        stage = (Stage) returnToLoginButton.getScene().getWindow();
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
        }, outerAnchorPane);
    }

    @FXML
    private void onExit() {
        stage = (Stage) exitButton.getScene().getWindow();
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
        }, outerAnchorPane);
    }

    @FXML
    public void handleOkButtonAction() {
        failedNotification.setVisible(false);
        libImage.setVisible(true);
        enableAllComponents(innerAnchorPane);
    }

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
            }
            else {
                node.setDisable(false);
            }
        }
    }

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
            }
            else {
                node.setDisable(true);
            }
        }
    }

    @FXML
    private void onSendingEmailAndUpdate() throws MessagingException {
        String username = usernameField.getText();
        String newPassword = generateRandomString(6);
        Task<String> task1 = new Task<>() {
            @Override
            protected String call() throws Exception {
                // Thực hiện công việc trong nền (background)
                return Model.getInstance().getDatabaseDriver().getEmailByUsername(username);
            }
        };
        new Thread(task1).start();
        task1.setOnSucceeded(event -> {
            if (task1.getValue() == null) {
                libImage.setVisible(false);
                failedNotification.setVisible(true);
                disableAllComponents(innerAnchorPane);
            } else {
                Model.getInstance().getViewFactory().showLoading(() -> {
                try {
                    Model.getInstance().getDatabaseDriver().sendNewPassword(
                    Model.getInstance().getDatabaseDriver().getEmailByUsername(username), newPassword);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    successNotification.setVisible(true);
                    Model.getInstance().getDatabaseDriver().updatePassword(username, newPassword);
                });
            }, outerAnchorPane);
        }
        });
    }
}

