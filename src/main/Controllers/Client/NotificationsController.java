package main.Controllers.Client;

import main.Models.DatabaseDriver;
import main.Models.Model;
import main.Models.Notification;
import main.Views.AccountType;
import main.Views.NotificationCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for handling notifications in the client interface.
 * This controller is responsible for displaying notifications, 
 * updating the unread notification count, and marking all notifications as read.
 */
public class NotificationsController implements Initializable {
    
    @FXML
    private ListView<Notification> notifications_listview;  // The list view to display notifications

    @FXML
    private Label unread_count_lbl;  // Label displaying the count of unread notifications

    @FXML
    private Button markAllAsReadBtn;  // Button to mark all notifications as read

    private int recipientId;  // The client ID for the recipient of notifications

    /**
     * Initializes the NotificationsController by setting up the list of notifications,
     * updating the unread count, and setting up event listeners for notifications and the 'Mark All As Read' button.
     * 
     * @param location the location used to resolve relative paths for resources.
     * @param resources the resources used for localization.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recipientId = Model.getInstance().getClient().getClientId();
        initAllNotificationsList();  // Initializes the list of all notifications
        updateUnreadCount(recipientId);  // Updates the count of unread notifications

        ObservableList<Notification> notifications = Model.getInstance().getAllNotifications();

        // Bind notifications list to ListView and set up custom cell factory
        notifications_listview.setItems(notifications);
        notifications_listview.setCellFactory(e -> new NotificationCellFactory());

        // Add listener to update unread count when a notification's read status changes
        notifications.addListener((javafx.collections.ListChangeListener.Change<? extends Notification> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Notification notification : c.getAddedSubList()) {
                        notification.isReadProperty().addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                updateUnreadCount(recipientId);
                            });
                        });
                    }
                }
            }
            Platform.runLater(() -> {
                updateUnreadCount(recipientId);
            });
        });

        // Add listener for each notification to track read/unread status
        for (Notification notification : notifications) {
            notification.isReadProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(() -> {
                    updateUnreadCount(recipientId);
                });
            });
        }

        // Set action for "Mark All As Read" button
        markAllAsReadBtn.setOnAction(event -> {
            handleMarkAllAsRead();
        });
    }

    /**
     * Initializes the list of all notifications for the client.
     * This method fetches the notifications and sets them in the model.
     */
    private void initAllNotificationsList() {
        Model.getInstance().setAllNotifications();
    }

    /**
     * Updates the unread notification count displayed on the UI.
     * It calculates the number of unread notifications based on the account type (Client or Admin).
     * 
     * @param recipientId the ID of the recipient whose unread notifications are being counted.
     */
    private void updateUnreadCount(int recipientId) {
        int unreadCount = (Model.getInstance().getViewFactory().getLoginAccountType().equals(AccountType.CLIENT))
                ? Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Client")
                : Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Admin");
        unread_count_lbl.setText("Unread Notifications: " + unreadCount);
    }

    /**
     * Marks all notifications for the current recipient as read.
     * It updates the unread notification count after marking all notifications.
     */
    private void handleMarkAllAsRead() {
        Model.getInstance().markAllNotificationsAsRead(recipientId);
        updateUnreadCount(recipientId);
    }
}
