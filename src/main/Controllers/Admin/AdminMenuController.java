package main.Controllers.Admin;

import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;
import main.Views.AdminMenuOptions;
import main.Models.Notification;

import java.net.URL;

/**
 * Controller class for the admin menu.
 * Manages navigation between different admin views and updates notification icons.
 */
public class AdminMenuController implements Initializable {

    /** Button to navigate to the dashboard */
    public Button dashboard_btn;
    // public Button profile_btn;
    /** Button to browse books */
    public Button browsing_book_btn;
    /** Button to browse clients */
    public Button browsing_client_btn;
    /** Button to view notifications */
    public Button noti_btn;
    /** Button to view transactions */
    public Button transaction_btn;
    /** ImageView for the notification icon */
    public ImageView noti_img;
    /** Button to log out of the application */
    public Button logout_btn;

    /** Image for the default notification icon (no new notifications) */
    private final Image defaultNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_off.png"));
    /** Image for the active notification icon (new notifications present) */
    private final Image activeNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_on.png"));

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up listeners and checks for notifications.
     *
     * @param url            The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        checkAndUpdateNotificationButton();
        // Model.getInstance().getDatabaseDriver().toCSV();
    }

    /**
     * Adds event listeners to buttons and notification properties.
     * Sets up actions for menu navigation and updates notification icon when new notifications arrive.
     */
    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        browsing_book_btn.setOnAction(event -> onBookBrowsing());
        // profile_btn.setOnAction(event -> onProfile());
        browsing_client_btn.setOnAction(event -> onClientBrowsing());
        noti_btn.setOnAction(event -> onNotification());
        transaction_btn.setOnAction(event -> onTransaction());

        ObservableList<Notification> notifications = Model.getInstance().getAllNotifications();

        notifications.addListener((javafx.collections.ListChangeListener.Change<? extends Notification> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Notification notification : c.getAddedSubList()) {
                        notification.isReadProperty().addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(this::checkAndUpdateNotificationButton);
                        });
                    }
                }
                Platform.runLater(this::checkAndUpdateNotificationButton);
            }
        });

        for (Notification notification : notifications) {
            notification.isReadProperty().addListener((obs, wasRead, isNowRead) -> {
                Platform.runLater(this::checkAndUpdateNotificationButton);
            });
        }
    }

    /**
     * Handles the action when the dashboard button is clicked.
     * Navigates to the dashboard view.
     */
    private void onDashboard() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DASHBOARD);
    }

    /**
     * Handles the action when the book browsing button is clicked.
     * Navigates to the book browsing view.
     */
    private void onBookBrowsing() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.BOOKBROWSING);
    }

    // /**
    //  * Handles the action when the profile button is clicked.
    //  * Navigates to the profile view.
    //  */
    // private void onProfile() {
    //     Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.PROFILE);
    // }

    /**
     * Handles the action when the client browsing button is clicked.
     * Navigates to the client browsing view.
     */
    private void onClientBrowsing() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTSBROWSING);
    }

    /**
     * Handles the action when the notification button is clicked.
     * Navigates to the notification view.
     */
    private void onNotification() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.NOTIFICATION);
    }

    /**
     * Handles the action when the transaction button is clicked.
     * Navigates to the transaction view.
     */
    private void onTransaction() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.BOOKTRANSACTION);
    }

    /**
     * Checks for unread notifications and updates the notification button icon and style accordingly.
     * Changes the icon and button style if there are unread notifications.
     */
    private void checkAndUpdateNotificationButton() {
        int unreadCount;
        if (Model.getInstance().getViewFactory().getLoginAccountType().equals(AccountType.CLIENT)) {
            unreadCount = Model.getInstance().getDatabaseDriver()
                    .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Client");
        } else {
            unreadCount = Model.getInstance().getDatabaseDriver()
                    .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Admin");
        }

        if (unreadCount > 0) {
            noti_img.setImage(activeNotiIcon);
            noti_btn.setStyle("-fx-background-color: #FFCCCC;");
            noti_btn.setText("Notification");
        } else {
            noti_img.setImage(defaultNotiIcon);
            noti_btn.setStyle("");
            noti_btn.setText("Notification");
        }
    }

    /**
     * Handles the action when the logout button is clicked.
     * Closes the current stage and opens the login window.
     */
    @FXML
    private void onLogout() {
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().setClientLoginSuccessFlag(false);
    }

}
