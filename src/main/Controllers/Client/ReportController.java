package main.Controllers.Client;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Views.NotificationType;
import main.Views.RecipientType;
import main.Models.Model;
import main.Models.Notification;;

public class ReportController implements Initializable {
    @FXML
    private TextField briefDesciption;

    @FXML
    private TextArea detailDesciption;

    @FXML
    private ImageView image;

    @FXML
    private ComboBox<String> severity;

    @FXML
    private Button submit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submit.setOnAction(event -> onSubmit());
    }

    private void onSubmit() {
        RecipientType recipientType = RecipientType.Admin;
        NotificationType notificationType = NotificationType.ReportBug;

        String reportTitle = briefDesciption.getText();
        String message = "A new report has been submitted with the title: '" + reportTitle + "'."
                + "\nDetails: " + detailDesciption.getText();

        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getAllAdminIDs();

        try {
            while (resultSet.next()) {
                int adminId = resultSet.getInt("admin_id");
                Notification notification = new Notification(
                        adminId,
                        recipientType,
                        notificationType,
                        message);
                Model.getInstance().sendNotification(notification);
            }
            showAlert("Report sent successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
