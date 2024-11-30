package main.Controllers.Client;

import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.Models.Model;
import main.Views.ClientMenuOptions;

import java.net.URL;

public class ClientMenuController implements Initializable {

    public Button dashboard_btn;
    public Button home_btn;
    public Button profile_btn;
    public Button browsing_btn;
    public Button noti_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        home_btn.setOnAction(event -> onHome());
        profile_btn.setOnAction(event -> onProfile());
        browsing_btn.setOnAction(event -> onBrowsing());
        noti_btn.setOnAction(event -> onNotification());
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
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.BOOKTRANSACTION);
    }
}
