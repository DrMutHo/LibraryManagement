package main.Controllers.Client;

import main.Models.Model;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class ClientController implements Initializable {

    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Model.getInstance().getViewFactory().getClientSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case HOME ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getHomeView());
                        case PROFILE ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                        case BROWSING ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getBrowsingView());
                        case NOTIFICATION ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getNotiView());
                        case BORROWTRANSACTION ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getBorrowTransactionView());
                        default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    }
                });
    }
}
