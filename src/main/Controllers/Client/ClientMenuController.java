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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        home_btn.setOnAction(event -> onHome());
        profile_btn.setOnAction(event -> onProfile());
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
}
