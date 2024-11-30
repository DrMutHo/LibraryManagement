package main.Controllers.Admin;


import main.Models.Model;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
public class AdminController implements Initializable {

    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Model.getInstance().getViewFactory().getAdminSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case PROFILE ->
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminProfileView());
                        case BOOKBROWSING ->
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminBookBrowsingView());
                        case CLIENTSBROWSING ->
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminClientsBrowsingView());
                        case NOTIFICATION ->
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminNotiView());
                        case BOOKTRANSACTION ->
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminBookTransactionView());
                        default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
                    }
                });
    }
}

