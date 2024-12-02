package main.Controllers.Admin;

import main.Models.Notification;
import main.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdminNotificationCellController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label admin_name_lbl;
    @FXML
    private Label recipient_type_lbl;
    @FXML
    private Label notification_type_lbl;
    @FXML
    private Label date_lbl;
    @FXML
    private Label message_lbl;
    @FXML
    private Button delete_btn;

    private final Notification notification;

    public AdminNotificationCellController(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        admin_name_lbl.setText("admin");

        notification_type_lbl.setText(notification.getNotificationType().toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        date_lbl.setText(notification.getCreatedAt().format(formatter));

        message_lbl.setText(notification.getMessage());

        delete_btn.setOnAction(event -> {
            Model.getInstance().deleteNotification(notification);
        });

        // Chỉnh màu sắc giống giao diện gmail
        rootPane.styleProperty().bind(Bindings.when(notification.isReadProperty())
                .then("-fx-background-color: #f0f0f0;") // Đã đọc: màu xám nhạt
                .otherwise("-fx-background-color: #ffffff;")); // Chưa đọc: màu trắng

        rootPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!notification.isRead()) {
                Model.getInstance().updateNotification(notification);
            }
        });
    }
}