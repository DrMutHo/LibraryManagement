package main.Controllers.Admin;

import main.Models.DatabaseDriver;
import main.Models.Model;
import main.Models.Notification;
import main.Views.AccountType;
import main.Views.AdminNotificationCellFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for managing admin notifications.
 * Handles displaying, updating, and marking notifications as read in the admin panel.
 */
public class AdminNotificationsController implements Initializable {
    /** ListView to display notifications */
    @FXML
    private ListView<Notification> notifications_listview;

    /** Label to display the count of unread notifications */
    @FXML
    private Label unread_count_lbl;

    /** Button to mark all notifications as read */
    @FXML
    private Button markAllAsReadBtn;

    /** The ID of the recipient (admin) */
    private int recipientId;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the notifications list, adds listeners, and initializes unread count.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recipientId = Model.getInstance().getAdmin().getadmin_id();
        initAllNotificationsList();
        updateUnreadCount(recipientId);

        ObservableList<Notification> notifications = Model.getInstance().getAllNotifications();

        notifications_listview.setItems(notifications);
        notifications_listview.setCellFactory(e -> new AdminNotificationCellFactory());

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
                Platform.runLater(() -> {
                    updateUnreadCount(recipientId);
                });
            }
        });

        for (Notification notification : notifications) {
            notification.isReadProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(() -> {
                    updateUnreadCount(recipientId);
                });
            });
        }

        markAllAsReadBtn.setOnAction(event -> {
            handleMarkAllAsRead();
        });
    }

    /**
     * Initializes the notifications list by fetching all notifications from the model.
     */
    private void initAllNotificationsList() {
        Model.getInstance().setAllNotifications();
    }

    /**
     * Updates the unread notifications count and displays it in the label.
     *
     * @param recipientId The ID of the recipient (admin) whose unread notifications are to be counted.
     */
    private void updateUnreadCount(int recipientId) {
        int unreadCount = (Model.getInstance().getViewFactory().getLoginAccountType().equals(AccountType.CLIENT))
                ? Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Client")
                : Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getAdmin().getadmin_id(), "Admin");
        unread_count_lbl.setText("Unread Notifications: " + unreadCount);
    }

    /**
     * Handles the action when the "Mark All as Read" button is clicked.
     * Marks all notifications as read and updates the unread count.
     */
    private void handleMarkAllAsRead() {
        Model.getInstance().markAllNotificationsAsRead(recipientId);
        updateUnreadCount(recipientId);
    }
}
