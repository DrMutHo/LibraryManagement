package main.Controllers.Admin;

import main.Models.DatabaseDriver;
import main.Models.Model;
import main.Models.Notification;
import main.Views.AccountType;
import main.Views.AdminNotificationCellFactory;
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

public class AdminNotificationsController implements Initializable {
    @FXML
    private ListView<Notification> notifications_listview;

    @FXML
    private Label unread_count_lbl;

    @FXML
    private Button markAllAsReadBtn;

    private int recipientId;

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
            }
            Platform.runLater(() -> {
                updateUnreadCount(recipientId);
            });
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

    private void initAllNotificationsList() {
        Model.getInstance().setAllNotifications();
    }

    private void updateUnreadCount(int recipientId) {
        int unreadCount = (Model.getInstance().getViewFactory().getLoginAccountType().equals(AccountType.CLIENT))
                ? Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Client")
                : Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getAdmin().getadmin_id(), "Admin");
        unread_count_lbl.setText("Unread Notifications: " + unreadCount);
    }

    private void handleMarkAllAsRead() {
        Model.getInstance().markAllNotificationsAsRead(recipientId);
        updateUnreadCount(recipientId);
    }
}
