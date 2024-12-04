package main.Controllers.Admin;

import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import javax.swing.plaf.ButtonUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;
import main.Views.AdminMenuOptions;
import main.Views.ClientMenuOptions;
import main.Models.Notification;

import java.net.URL;

public class AdminMenuController implements Initializable {

    public Button dashboard_btn;
    // public Button profile_btn;
    public Button browsing_book_btn;
    public Button browsing_client_btn;
    public Button noti_btn;
    public Button transaction_btn;
    public ImageView noti_img;
    public Button logout_btn;



    private final Image defaultNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_off.png"));
    private final Image activeNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_on.png"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        checkAndUpdateNotificationButton();
        // Model.getInstance().getDatabaseDriver().toCSV();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        browsing_book_btn.setOnAction(event -> onBookBrowsing());
        // profile_btn.setOnAction(event -> onProfile());
        browsing_client_btn.setOnAction(event -> onCLientBrowsing());
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

    private void onDashboard() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DASHBOARD);
    }

    private void onBookBrowsing() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.BOOKBROWSING);
    }

    // private void onProfile() {
    //     Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.PROFILE);
    // }

    private void onCLientBrowsing() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTSBROWSING);
    }

    private void onNotification() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.NOTIFICATION);
    }

    private void onTransaction() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.BOOKTRANSACTION);
    }

    private void checkAndUpdateNotificationButton() {
        int unreadCount = (Model.getInstance().getViewFactory().getLoginAccountType().equals(AccountType.CLIENT))
                ? Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Client")
                : Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Admin");

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

    @FXML
    private void onLogout() {
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().setClientLoginSuccessFlag(false);
    }

}
