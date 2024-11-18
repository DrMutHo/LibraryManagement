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
            Model.getInstance().getViewFactory().showLoginWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        }, outerAnchorPane);
    }

    @FXML
    private void onExit() {
        stage = (Stage) exitButton.getScene().getWindow();
        Model.getInstance().getViewFactory().showLoading(() -> {
            Model.getInstance().getViewFactory().showLoginWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
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
            // Kiểm tra nếu node là StackPane, tiếp tục kiểm tra failedNotification trong đó
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                for (javafx.scene.Node stackNode : stackPane.getChildren()) {
                    // Nếu node là failedNotification, bỏ qua nó và các con của nó
                    if (stackNode instanceof AnchorPane && ((AnchorPane) stackNode).getId() != null &&
                            ((AnchorPane) stackNode).getId().equals("failedNotification")) {
                        continue;  // Không disable các thành phần bên trong failedNotification
                    }
                }
            }
            // Nếu không phải StackPane chứa failedNotification, tiến hành enable node
            else {
                node.setDisable(false);
            }
        }
    }

    private void disableAllComponents(AnchorPane root) {
        for (javafx.scene.Node node : root.getChildren()) {
            // Kiểm tra nếu node là StackPane, tiếp tục kiểm tra failedNotification trong đó
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                for (javafx.scene.Node stackNode : stackPane.getChildren()) {
                    // Nếu node là failedNotification, bỏ qua nó và các con của nó
                    if (stackNode instanceof AnchorPane && ((AnchorPane) stackNode).getId() != null &&
                            ((AnchorPane) stackNode).getId().equals("failedNotification")) {
                        continue;  // Không disable các thành phần bên trong failedNotification
                    }
                }
            }
            // Nếu không phải StackPane chứa failedNotification, tiến hành disable node
            else {
                node.setDisable(true);
            }
        }
    }

    @FXML
    private void onSendingEmailAndUpdate() throws MessagingException {
        String username = usernameField.getText();
        String newPassword = generateRandomString(6);
        if (getEmailByUsername(username) == null) {
            libImage.setVisible(false);
            failedNotification.setVisible(true);
            disableAllComponents(innerAnchorPane);
        } else {
            try {
                sendNewPassword(getEmailByUsername(username), newPassword);
                successNotification.setVisible(true);
                updatePassword(username, newPassword);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePassword(String username, String newPassword) {
        // Mã hóa mật khẩu mới sử dụng jBCrypt
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Câu lệnh SQL để cập nhật mật khẩu mới cho người dùng
        String query = "UPDATE client SET password_hash = ? WHERE username = ?";

        // Sử dụng try-with-resources để tự động đóng tài nguyên
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Đặt giá trị cho PreparedStatement
            preparedStatement.setString(1, hashedPassword);  // Mật khẩu đã mã hóa
            preparedStatement.setString(2, username);        // Tên đăng nhập của người dùng

            // Thực thi câu lệnh SQL
            int rowsUpdated = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getEmailByUsername(String username) {
        // Câu lệnh SQL truy vấn email theo username
        String query = "SELECT email FROM client WHERE username = ?";
    
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            // Thiết lập giá trị cho tham số truy vấn
            preparedStatement.setString(1, username);
    
            // Thực thi truy vấn
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Lấy giá trị email từ kết quả truy vấn
                    return resultSet.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null; // Trả về null nếu username không tồn tại hoặc có lỗi xảy ra
    }

    public void sendNewPassword(String recipientEmail, String newPassword) throws MessagingException {
        // Cấu hình SMTP server của Gmail
        String smtpHost = "smtp.gmail.com"; // SMTP của Gmail
        String smtpPort = "587"; // 587 cho TLS, 465 cho SSL
        String senderEmail = "thuha25121976@gmail.com"; // Thay bằng email của bạn
        String senderPassword = "bbjh xcbp oxtj qozz"; // Mật khẩu ứng dụng (do bạn tạo ở Google)
    
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Kích hoạt TLS
    
        // Tạo session cho email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
    
        // Tạo email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Your New Password");
        message.setText("Dear customer,\n\nYour new password is: " + newPassword + "\n\nPlease log in and change your password as soon as possible.\n\nThank you.");
    
        // Gửi email
        Transport.send(message);
        System.out.println("Email sent successfully to " + recipientEmail);
    }
}

