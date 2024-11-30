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
                        default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    }
                });
    }
}


// package main.Controllers.Admin;

// import main.Models.Model;
// import java.net.URL;
// import java.util.ResourceBundle;

// import javafx.fxml.Initializable;
// import javafx.scene.layout.BorderPane;

// public class AdminController implements Initializable {

//     public BorderPane admin_parent;

//     @Override
//     public void initialize(URL url, ResourceBundle resourceBundle) {

//         Model.getInstance().getViewFactory().getAdminSelectedMenuItem()
//                 .addListener((observableValue, oldVal, newVal) -> {
//                     switch (newVal) {
//                         case HOME ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getHomeView());
//                         case PROFILE ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
//                         case BROWSING ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getBrowsingView());
//                         case NOTIFICATION ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getNotiView());
//                         case BOOKTRANSACTION ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getBookTransactionView());
//                         default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
//                     }
//                 });
//     }
// }
