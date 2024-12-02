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
import main.Models.Notification;

/**
 * Controller for the Report feature in the client interface.
 * This controller handles the submission of bug reports and notifications to admins.
 */
public class ReportController implements Initializable {

    @FXML
    private TextField briefDesciption;  // TextField for the brief description of the report

    @FXML
    private TextArea detailDesciption;  // TextArea for the detailed description of the report

    @FXML
    private ImageView image;  // ImageView for displaying any attached image (optional)

    @FXML
    private ComboBox<String> severity;  // ComboBox for selecting the severity of the report

    @FXML
    private Button submit;  // Button to submit the report

    /**
     * Initializes the ReportController by setting up the submit button action.
     * 
     * @param location the location used to resolve relative paths for resources.
     * @param resources the resources used for localization.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submit.setOnAction(event -> onSubmit());  // Set the action for the submit button
    }

    /**
     * Handles the submission of the bug report. It collects the report details and sends a notification
     * to all admins with the report information.
     */
    private void onSubmit() {
        RecipientType recipientType = RecipientType.Admin;  // Recipient type is set to Admin
        NotificationType notificationType = NotificationType.ReportBug;  // Notification type is set to ReportBug

        // Collect the title and message of the report
        String reportTitle = briefDesciption.getText();
        String message = "A new report has been submitted with the title: '" + reportTitle + "'."
                + "\nDetails: " + detailDesciption.getText();

        // Retrieve all admin IDs from the database to send notifications
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getAllAdminIDs();

        try {
            // Iterate through all admins and send the notification
            while (resultSet.next()) {
                int adminId = resultSet.getInt("admin_id");
                Notification notification = new Notification(
                        adminId,
                        recipientType,
                        notificationType,
                        message);
                Model.getInstance().sendNotification(notification);
            }
            // Show an alert confirming the report was sent successfully
            showAlert("Report sent successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions
        }
    }

    /**
     * Displays an alert with the specified message and alert type.
     * 
     * @param message the message to be displayed in the alert.
     * @param alertType the type of the alert (e.g., INFORMATION, ERROR).
     */
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();  // Show the alert and wait for user response
    }
}
