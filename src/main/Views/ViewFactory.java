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
import main.Controllers.Client.BookDetailWithReviewController;
import main.Controllers.Client.ClientController;
import main.Models.Book;
import main.Controllers.Client.ChangePasswordController;
import main.Controllers.Client.ClientController;
import main.Controllers.Client.ProfileController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.ProgressIndicator;
import main.Controllers.Client.ClientController;
import main.Controllers.Admin.AdminController;

public class ViewFactory {

    private AccountType loginAccountType;

    // Client and Admin Menu Options
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private final ObjectProperty<ProfileMenuOptions> profileSelectedMenuItem;
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;

    // Client Views

    private BorderPane dashboardView;
    private BorderPane homeView;
    private BorderPane profileView;
    private BorderPane browsingView;
    private BorderPane notiView;
    private BorderPane bookDetailsView;
    private BorderPane BorrowTransactionView;
    private BorderPane profileDetailView;
    private BorderPane changePasswordView;
    private BorderPane editProfileView;
    private BorderPane deleteAccountView;

    public void resetAllPanes() {
        dashboardView = null;
        homeView = null;
        profileView = null;
        browsingView = null;
        notiView = null;
        booktransactionView = null;
        bookDetailsView = null;
        BorrowTransactionView = null;
        profileDetailView = null;
        changePasswordView = null;
        editProfileView = null;
        deleteAccountView = null;
    }

    // Admin Views
    private BorderPane adminDashboardView;
    private BorderPane adminProfileView;
    private BorderPane adminBookBrowsingView;
    private BorderPane adminClientsBrowsingView;
    private BorderPane adminNotiView;
    private BorderPane adminBookTransactionView;

    public ViewFactory() {
        this.loginAccountType = AccountType.CLIENT; // Default to Client
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.profileSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    // Setters and Getters for Account Type and Menu Selection
    public AccountType getLoginAccountType() {
        return this.loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    // Client Views Section
    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    public ObjectProperty<ProfileMenuOptions> getProfileSelectedMenuItem() {
        return profileSelectedMenuItem;
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

    public BorderPane getBorrowTransactionView() {
        if (BorrowTransactionView == null) {
            try {
                BorrowTransactionView = new FXMLLoader(
                        getClass().getResource("/resources/Fxml/Client/BorrowTransaction.fxml"))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return BorrowTransactionView;
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

    public BorderPane getBookDetailsView(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/Fxml/Client/BookDetailWithReview.fxml"));
            bookDetailsView = loader.load();
            BookDetailWithReviewController bookDetailController = loader.getController();
            bookDetailController.setBook(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookDetailsView;
    }

    public BorderPane getProfileDetailView() {
        if (profileDetailView == null) {
            try {
                profileDetailView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/ProfileDetail.fxml"))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return profileDetailView;
    }

    // Admin Views Section
    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public BorderPane getAdminDashboardView() {
        if (adminDashboardView == null) {
            try {
                adminDashboardView = new FXMLLoader(getClass().getResource("/resources/Fxml/Admin/AdminDashboard.fxml"))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminDashboardView;
    }

    public AnchorPane getChangePasswordView() {
        if (changePasswordView == null) {
            try {
                changePasswordView = new FXMLLoader(
                        getClass().getResource("/resources/Fxml/Client/ChangePassword.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return changePasswordView;
    }

    public BorderPane getEditProfileView() {
        if (editProfileView == null) {
            try {
                editProfileView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/EditProfile.fxml"))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return editProfileView;
    }

    public BorderPane getAdminProfileView() {
        if (adminProfileView == null) {
            try {
                adminProfileView = new FXMLLoader(getClass().getResource("/resources/Fxml/Admin/AdminProfile.fxml"))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminProfileView;
    }

    public AnchorPane getDeleteAccountView() {

        if (deleteAccountView == null) {
            try {
                deleteAccountView = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/DeleteAccount.fxml"))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deleteAccountView;
    }

    public BorderPane getAdminBookBrowsingView() {
        if (adminBookBrowsingView == null) {
            try {
                adminBookBrowsingView = new FXMLLoader(
                        getClass().getResource("/resources/Fxml/Admin/BookBrowsing.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return adminBookBrowsingView;

    }

    public BorderPane getAdminClientsBrowsingView() {
        if (adminClientsBrowsingView == null) {
            try {
                adminClientsBrowsingView = new FXMLLoader(
                        getClass().getResource("/resources/Fxml/Admin/ClientBrowsing.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminClientsBrowsingView;
    }

    public BorderPane getAdminNotiView() {
        if (adminNotiView == null) {
            try {
                adminNotiView = new FXMLLoader(getClass().getResource("/resources/Fxml/Admin/AdminNotification.fxml"))
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminNotiView;
    }

    public BorderPane getAdminBookTransactionView() {
        if (adminBookTransactionView == null) {
            try {
                adminBookTransactionView = new FXMLLoader(
                        getClass().getResource("/resources/Fxml/Admin/AdminBookTransaction.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminBookTransactionView;
    }

    // Show Windows based on the Role (Client or Admin)
    public void showClientWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }

    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
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

            // Đảm bảo thời gian hiển thị lớp phủ tối thiểu bằng thời gian chuẩn bị tài
            // nguyên
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

    public void showResetPasswordWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Fxml/Forgotpassword.fxml"));
        createStage(loader);
    }

    // Show loading screen with a task running in background
    public void showLoading(Runnable task, AnchorPane anchorPane) {
        StackPane loadingOverlay = new StackPane();
        loadingOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        loadingOverlay.setPrefSize(anchorPane.getWidth(), anchorPane.getHeight());

        ProgressIndicator progressIndicator = new ProgressIndicator();
        loadingOverlay.getChildren().add(progressIndicator);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);

        Platform.runLater(() -> {
            if (!anchorPane.getChildren().contains(loadingOverlay)) {
                anchorPane.getChildren().add(loadingOverlay);
            }
        });

        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            task.run();
            long elapsedTime = System.currentTimeMillis() - startTime;
            long minimumDisplayTime = 500; // Minimum display time (500ms)
            long remainingTime = minimumDisplayTime - elapsedTime;

            if (remainingTime > 0) {
                try {
                    Thread.sleep(remainingTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            Platform.runLater(() -> anchorPane.getChildren().remove(loadingOverlay));
        }).start();
    }

    // Helper method to create a new stage (window)
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

    // Close the current stage (window)
    public void closeStage(Stage stage) {
        stage.close();
    }
}
