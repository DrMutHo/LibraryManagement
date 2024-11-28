package main.Views;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.concurrent.Task;
import main.Controllers.Client.ChangePasswordController;
import main.Controllers.Client.ClientController;
import main.Controllers.Client.ProfileController;
import javafx.scene.layout.AnchorPane;

public class ViewFactory {
    private AccountType loginAccountType;
    // Client Views
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private BorderPane dashboardView;
    private BorderPane homeView;
    private BorderPane profileView;
    private BorderPane browsingView;
    private BorderPane notiView;
    private BorderPane booktransactionView;
    private AnchorPane changePasswordView;
    private AnchorPane editProfileView;
    private AnchorPane deleteAccountView;
    

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

    public AnchorPane getChangePasswordView() {
        if (changePasswordView == null) {
            try {
                // Tải FXML và lưu vào changePasswordView chỉ một lần
                changePasswordView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/ChangePassword.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                // Xử lý khi không thể tải tệp FXML
                return null;  // Hoặc có thể ném ngoại lệ, tùy theo yêu cầu
            }
        }
        return changePasswordView;
    }
    

    public BorderPane getBookTransactionView() {
        if (booktransactionView == null) {
            try {
                booktransactionView = new FXMLLoader(
                        getClass().getResource("/resources/Fxml/Client/BookTransaction.fxml"))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return booktransactionView;
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

    public AnchorPane getEditProfileView() {
        if (editProfileView == null) {
            try {
                editProfileView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/EditProfile.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return editProfileView;
    }

    public AnchorPane getDeleteAccountView() {
        if (deleteAccountView == null) {
            try {
                deleteAccountView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/DeleteAccount.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deleteAccountView;
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

    public void ShowResetPasswordWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Forgotpassword.fxml"));
        createStage(loader);
    }

    public void showLoading(Runnable task, Pane pane) {
        // Tạo lớp phủ với loading
        StackPane loadingOverlay = new StackPane();
        loadingOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        loadingOverlay.setPrefSize(pane.getWidth(), pane.getHeight());
    
        ProgressIndicator progressIndicator = new ProgressIndicator();
        loadingOverlay.getChildren().add(progressIndicator);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);
    
        // Đảm bảo lớp phủ được thêm vào giao diện
        Platform.runLater(() -> {
            if (!pane.getChildren().contains(loadingOverlay)) {
                pane.getChildren().add(loadingOverlay);
            }
        });
    
        // Thực hiện công việc chính trong một Thread riêng biệt
        new Thread(() -> {
            long startTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu
    
            // Tính thời gian chuẩn bị tài nguyên (chạy trước khi thực hiện task)
            long preparationTime = System.currentTimeMillis() - startTime;
    
            task.run(); // Thực hiện công việc chính
    
            long elapsedTime = System.currentTimeMillis() - startTime; // Tính thời gian đã chạy
    
            // Đảm bảo thời gian hiển thị lớp phủ tối thiểu bằng thời gian chuẩn bị tài nguyên
            long minimumDisplayTime = preparationTime; // Thời gian chuẩn bị tài nguyên
            long remainingTime = minimumDisplayTime - elapsedTime;
    
            if (remainingTime > 0) {
                try {
                    Thread.sleep(remainingTime); // Chờ thêm nếu cần
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
                }
            }
    
            // Gỡ bỏ lớp phủ loading
            Platform.runLater(() -> pane.getChildren().remove(loadingOverlay));
        }).start();
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
