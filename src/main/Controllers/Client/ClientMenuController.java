package main.Controllers.Client;

import java.util.ResourceBundle;

import javax.swing.plaf.ButtonUI;

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
    public Button transaction_btn;

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
        transaction_btn.setOnAction(event -> onTransaction());
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

    private void onTransaction() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.BOOKTRANSACTION);
    }
}
