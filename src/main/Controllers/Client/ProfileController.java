package main.Controllers.Client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import main.Models.Model;

/**
 * Controller for handling the profile view and its navigation in the client interface.
 * This controller allows users to navigate between different profile sections such as 
 * profile details, edit profile, change password, and delete account.
 */
public class ProfileController implements Initializable {

    public BorderPane client_parent;  // The main layout container for the profile view sections.

    /**
     * Initializes the ProfileController by setting up listeners for profile menu selection.
     * This method listens for changes in the selected menu item and updates the displayed content accordingly.
     * 
     * @param url the location used to resolve relative paths for resources.
     * @param resourceBundle the resources used for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Listener for changes in the selected menu item for profile navigation
        Model.getInstance().getViewFactory().getProfileSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    // Based on the selected profile menu item, update the center content of the client_parent BorderPane
                    switch (newVal) {
                        case PROFILEDETAIL:  // Display the Profile Detail View
                            client_parent.setCenter(Model.getInstance().getViewFactory().getProfileDetailView());
                            break;
                        case EDITPROFILE:  // Display the Edit Profile View
                            client_parent.setCenter(Model.getInstance().getViewFactory().getEditProfileView());
                            break;
                        case CHANGEPASSWORD:  // Display the Change Password View
                            client_parent.setCenter(Model.getInstance().getViewFactory().getChangePasswordView());
                            break;
                        case DELETEACCOUNT:  // Display the Delete Account View
                            client_parent.setCenter(Model.getInstance().getViewFactory().getDeleteAccountView());
                            break;
                        default:  // Default to Profile Detail View
                            client_parent.setCenter(Model.getInstance().getViewFactory().getProfileDetailView());
                    }
                });
    }
}
