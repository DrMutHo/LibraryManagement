package main.Controllers.Client;

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
 * Controller for the NotificationCell, responsible for initializing and displaying
 * notification details within a list of notifications.
 * It sets up the notification data, binds UI elements to model properties, and provides functionality
 * to delete or mark notifications as read.
 */
public class NotificationCellController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label client_name_lbl;
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

    /**
     * Constructor for the NotificationCellController class. Initializes the controller with the provided notification.
     * This constructor is used to set the notification data for this controller when creating an instance of it.
     *
     * @param notification the notification to be associated with this controller.
     */
    public NotificationCellController(Notification notification) {
        this.notification = notification;
    }


    @Override
    /**
     * Initializes the NotificationCellController by setting the relevant data for the notification display.
     * This includes displaying the recipient's name, notification type, creation date, message, and setting up
     * event handlers for deleting and updating the notification.
     * 
     * The method also binds the background color of the notification cell to the read/unread status of the notification:
     * - Gray for read notifications.
     * - White for unread notifications.
     *
     * @param location the URL location used to resolve relative paths for resources.
     * @param resources the resources used for localization.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Get the client name based on the recipient ID from the model
        String clientName = Model.getInstance().getDatabaseDriver().getClientNameById(notification.getRecipientId());
        client_name_lbl.setText(clientName != null ? clientName : "Unknown");
    
        // Set the notification type label to the notification type's string representation
        notification_type_lbl.setText(notification.getNotificationType().toString());
    
        // Format and display the notification creation date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        date_lbl.setText(notification.getCreatedAt().format(formatter));
    
        // Set the message label to the notification message
        message_lbl.setText(notification.getMessage());
    
        // Set up the delete button action to delete the notification when clicked
        delete_btn.setOnAction(event -> {
            Model.getInstance().deleteNotification(notification);
        });
    
        // Bind the background color of the root pane to the read/unread status of the notification
        rootPane.styleProperty().bind(Bindings.when(notification.isReadProperty())
                .then("-fx-background-color: #f0f0f0;") // Gray for read notifications
                .otherwise("-fx-background-color: #ffffff;")); // White for unread notifications
    
        // Set up an event handler for clicking the notification cell to mark it as read
        rootPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!notification.isRead()) {
                Model.getInstance().updateNotification(notification);
            }
        });
    }
}

