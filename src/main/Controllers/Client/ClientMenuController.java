package main.Controllers.Client;

import java.util.ResourceBundle;

import javafx.application.Platform;

import javafx.collections.ObservableList;

import javafx.application.Platform;

import javax.swing.plaf.ButtonUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;
import main.Views.ClientMenuOptions;
import main.Models.Notification;
import main.Controllers.Client.ReportController;

import java.io.IOException;
import java.net.URL;

public class ClientMenuController implements Initializable {

    public Button dashboard_btn;
    public Button home_btn;
    public Button profile_btn;
    public Button browsing_btn;
    public Button noti_btn;
    public Button transaction_btn;
    public Button logout_btn;
    public Button report_btn;
    public ImageView noti_img;

    private final Image defaultNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_off.png"));
    private final Image activeNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_on.png"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        checkAndUpdateNotificationButton();
        checkAndUpdateNotificationButton();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        home_btn.setOnAction(event -> onHome());
        profile_btn.setOnAction(event -> onProfile());
        browsing_btn.setOnAction(event -> onBrowsing());
        noti_btn.setOnAction(event -> onNotification());
        transaction_btn.setOnAction(event -> onTransaction());
        logout_btn.setOnAction(event -> onLogout());

        ObservableList<Notification> notifications = Model.getInstance().getAllNotifications();
        report_btn.setOnAction(event -> onReport());

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

    @FXML
    private void onLogout() {
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().resetAllPanes();
        Model.getInstance().reset();
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().setClientLoginSuccessFlag(false);
    }

    @FXML
    private void onReport() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/FXML/Client/Report.fxml"));
            ReportController reportController = new ReportController();
            loader.setController(reportController);
            VBox bugReportRoot = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Bug Report");
            stage.setScene(new Scene(bugReportRoot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void onTransaction() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.BORROWTRANSACTION);
    }
}
