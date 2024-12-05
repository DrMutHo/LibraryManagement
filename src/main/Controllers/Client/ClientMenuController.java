package main.Controllers.Client;

import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Models.Model;
import main.Views.AccountType;
import main.Views.ClientMenuOptions;
import main.Models.Notification;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for managing the client-side menu. It handles interactions with
 * the client navigation buttons (Dashboard, Home, Profile, etc.) and updates
 * the notification icon based on notification status.
 */
public class ClientMenuController implements Initializable {

    @FXML
    public Button dashboard_btn;
    @FXML
    public Button home_btn;
    @FXML
    public Button profile_btn;
    @FXML
    public Button browsing_btn;
    @FXML
    public Button noti_btn;
    @FXML
    public Button transaction_btn;
    @FXML
    public Button logout_btn;
    @FXML
    public Button report_btn;
    @FXML
    public ImageView noti_img;

    private final Image defaultNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_off.png"));
    private final Image activeNotiIcon = new Image(getClass().getResourceAsStream("/resources/Images/noti_on.png"));

    /**
     * Initializes the client menu by setting up button listeners and checking
     * for any new notifications to update the notification button icon.
     *
     * @param url            The location used to resolve relative paths for the
     *                       root object.
     * @param resourceBundle The resources used to localize the view.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        checkAndUpdateNotificationButton();
    }

    /**
     * Adds action listeners to all the navigation buttons. It also sets up
     * listeners for changes in the notification list to update the notification
     * icon.
     */
    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        home_btn.setOnAction(event -> onHome());
        profile_btn.setOnAction(event -> onProfile());
        browsing_btn.setOnAction(event -> onBrowsing());
        noti_btn.setOnAction(event -> onNotification());
        transaction_btn.setOnAction(event -> onTransaction());
        logout_btn.setOnAction(event -> onLogout());
        report_btn.setOnAction(event -> onReport());

        ObservableList<Notification> notifications = Model.getInstance().getAllNotifications();

        // Add listener to check and update notification icon status when notifications
        // are added
        notifications.addListener((javafx.collections.ListChangeListener.Change<? extends Notification> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Notification notification : c.getAddedSubList()) {
                        notification.isReadProperty().addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(this::checkAndUpdateNotificationButton);
                        });
                    }
                }
                Platform.runLater(this::checkAndUpdateNotificationButton);
            }
        });

        // Check and update notification icon status for each notification
        for (Notification notification : notifications) {
            notification.isReadProperty().addListener((obs, wasRead, isNowRead) -> {
                Platform.runLater(this::checkAndUpdateNotificationButton);
            });
        }
    }

    /**
     * Phương thức này được gọi khi người dùng chọn mục "Dashboard" trong menu.
     * Nó cập nhật lựa chọn menu hiện tại của người dùng sang "Dashboard".
     */
    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    /**
     * Phương thức này được gọi khi người dùng chọn mục "Home" trong menu.
     * Nó cập nhật lựa chọn menu hiện tại của người dùng sang "Home".
     */
    private void onHome() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.HOME);
    }

    /**
     * Phương thức này được gọi khi người dùng chọn mục "Profile" trong menu.
     * Nó cập nhật lựa chọn menu hiện tại của người dùng sang "Profile".
     */
    private void onProfile() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
    }

    /**
     * Phương thức này được gọi khi người dùng chọn mục "Browsing" trong menu.
     * Nó cập nhật lựa chọn menu hiện tại của người dùng sang "Browsing".
     */
    private void onBrowsing() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.BROWSING);
    }

    /**
     * Phương thức này được gọi khi người dùng chọn mục "Notification" trong menu.
     * Nó cập nhật lựa chọn menu hiện tại của người dùng sang "Notification".
     */
    private void onNotification() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.NOTIFICATION);
    }

    /**
     * Phương thức này được gọi khi người dùng thực hiện đăng xuất.
     * Nó sẽ đóng cửa sổ hiện tại (dashboard), hiển thị cửa sổ đăng nhập và
     * đặt cờ đăng nhập của người dùng thành false.
     */
    @FXML
    private void onLogout() {
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().reset();
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().setClientLoginSuccessFlag(false);
    }

    /**
     * Phương thức này được gọi khi người dùng yêu cầu xem báo cáo lỗi.
     * Nó sẽ tạo một cửa sổ mới để hiển thị báo cáo lỗi, với giao diện FXML được
     * định nghĩa trong "Report.fxml".
     * Cửa sổ này sẽ mở dưới dạng một cửa sổ modal (chặn cửa sổ chính).
     */
    @FXML
    private void onReport() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/FXML/Client/Report.fxml"));
            ReportController reportController = new ReportController();
            loader.setController(reportController);
            VBox bugReportRoot = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Bug Report");
            stage.setScene(new Scene(bugReportRoot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức này kiểm tra số lượng thông báo chưa đọc và cập nhật trạng thái
     * của nút thông báo.
     * Nếu có thông báo chưa đọc, nó sẽ thay đổi biểu tượng và màu sắc của nút thông
     * báo.
     * Nếu không có thông báo chưa đọc, nó sẽ khôi phục lại biểu tượng và màu sắc
     * mặc định.
     */
    private void checkAndUpdateNotificationButton() {
        int unreadCount = (Model.getInstance().getViewFactory().getLoginAccountType().equals(AccountType.CLIENT))
                ? Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Client")
                : Model.getInstance().getDatabaseDriver()
                        .countUnreadNotifications(Model.getInstance().getClient().getClientId(), "Admin");

        if (unreadCount > 0) {
            noti_img.setImage(activeNotiIcon);
            noti_btn.setStyle("-fx-background-color: #FFCCCC;");
            noti_btn.setText("Notification");
        } else {
            noti_img.setImage(defaultNotiIcon);
            noti_btn.setStyle("");
            noti_btn.setText("Notification");
        }
    }

    /**
     * Phương thức này được gọi khi người dùng chọn mục "Transaction" trong menu.
     * Nó cập nhật lựa chọn menu hiện tại của người dùng sang "Borrow Transaction".
     */
    private void onTransaction() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.BORROWTRANSACTION);
    }

}
