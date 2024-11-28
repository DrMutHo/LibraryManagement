package main.Controllers.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import main.Models.Model;

public class ProfileController implements Initializable {

    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case PROFILEDETAIL ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getProfileDetailView());
                        case EDITPROFILE ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getEditProfileView());
                        case CHANGEPASSWORD ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getChangePasswordView());
                        case DELETEACCOUNT ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getDeleteAccountView());
                        default -> client_parent.setCenter(Model.getInstance().getViewFactory().getProfileDetailView());
                    }
                });
    }

}