package main.Controllers.Client;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javax.swing.plaf.ButtonUI;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Models.Model;
import main.Views.ClientMenuOptions;
import main.Models.Notification;

import java.net.URL;

public class ClientMenuController implements Initializable {

    public Button dashboard_btn;
    public Button home_btn;
    public Button profile_btn;
    public Button browsing_btn;
    public Button noti_btn;
    public Button transaction_btn;
    public ImageView noti_img;

    private final Image defaultNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_off.png"));
    private final Image activeNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_on.png"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        checkAndUpdateNotificationButton();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        home_btn.setOnAction(event -> onHome());
        profile_btn.setOnAction(event -> onProfile());
        browsing_btn.setOnAction(event -> onBrowsing());
        noti_btn.setOnAction(event -> onNotification());
        transaction_btn.setOnAction(event -> onTransaction());

        Model.getInstance().getAllNotifications()
                .addListener((javafx.collections.ListChangeListener.Change<? extends Notification> change) -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            for (Notification newNoti : change.getAddedSubList()) {
                                newNoti.isReadProperty().addListener((obs, wasRead, isNowRead) -> {
                                    Platform.runLater(this::checkAndUpdateNotificationButton);
                                });
                            }
                        }
                        if (change.wasRemoved()) {
                        }
                        if (change.wasUpdated()) {
                            Platform.runLater(this::checkAndUpdateNotificationButton);
                        }
                    }
                });

        for (Notification notification : Model.getInstance().getAllNotifications()) {
            notification.isReadProperty().addListener((obs, wasRead, isNowRead) -> {
                Platform.runLater(this::checkAndUpdateNotificationButton);
            });
        }
    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    private void onHome() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.HOME);
    }

    private void onProfile() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
    }

    private void onBrowsing() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.BROWSING);
    }

    private void onNotification() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.NOTIFICATION);

    }

    private void checkAndUpdateNotificationButton() {
        int unreadCount = Model.getInstance().getDatabaseDriver()
                .countUnreadNotifications(Model.getInstance().getClient().getClientId());

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

    private void onTransaction() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.BORROWTRANSACTION);
    }
}
