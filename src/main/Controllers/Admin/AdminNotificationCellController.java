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

/**
 * Controller class for the admin notification cell.
 * Handles the display and interaction of individual notifications in the admin panel.
 */
public class AdminNotificationCellController implements Initializable {
    /** Root pane of the notification cell */
    @FXML
    private AnchorPane rootPane;
    /** Label for admin name */
    @FXML
    private Label admin_name_lbl;
    /** Label for recipient type */
    @FXML
    private Label recipient_type_lbl;
    /** Label for notification type */
    @FXML
    private Label notification_type_lbl;
    /** Label for date */
    @FXML
    private Label date_lbl;
    /** Label for message content */
    @FXML
    private Label message_lbl;
    /** Button to delete the notification */
    @FXML
    private Button delete_btn;

    /** The notification object associated with this cell */
    private final Notification notification;

    /**
     * Constructor for AdminNotificationCellController.
     *
     * @param notification The notification to display in this cell.
     */
    public AdminNotificationCellController(Notification notification) {
        this.notification = notification;
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
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

        // Adjust color similar to Gmail interface
        rootPane.styleProperty().bind(Bindings.when(notification.isReadProperty())
                .then("-fx-background-color: #f0f0f0;") // Read: light gray
                .otherwise("-fx-background-color: #ffffff;")); // Unread: white

        rootPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!notification.isRead()) {
                Model.getInstance().updateNotification(notification);
            }
        });
    }
}
