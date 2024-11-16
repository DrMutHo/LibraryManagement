package main.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.concurrent.Task;
import main.Controllers.Client.ClientController;

public class ViewFactory {
    private AccountType loginAccountType;
    // Client Views
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private BorderPane dashboardView;
    private BorderPane homeView;
    private BorderPane profileView;
    private BorderPane browsingView;
    private BorderPane notiView;

    public ViewFactory() {
        this.loginAccountType = AccountType.CLIENT;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType() {
        return this.loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    /*
     * Client Views Section.
     */
    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    public BorderPane getDashboardView() {
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public BorderPane getHomeView() {
        if (homeView == null) {
            try {
                homeView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/Home.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return homeView;
    }

    public BorderPane getProfileView() {
        if (profileView == null) {
            try {
                profileView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/Profile.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return profileView;
    }

    public BorderPane getBrowsingView() {
        if (browsingView == null) {
            try {
                browsingView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/Browsing.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return browsingView;
    }

    public BorderPane getNotiView() {
        if (notiView == null) {
            try {
                notiView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/Notification.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notiView;
    }

    public void showClientWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showSignUpWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Signup.fxml"));
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Library Management System");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
