package main.Controllers.Admin;

import main.Models.Model;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 * Controller class for the admin panel.
 * Manages the navigation between different admin views based on user selections.
 */
public class AdminController implements Initializable {

    /** The main layout container for the admin panel */
    public BorderPane admin_parent;

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * @param url            The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
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
                        default ->
                            admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
                    }
                });
    }
}
