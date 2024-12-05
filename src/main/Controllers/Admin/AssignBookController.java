package main.Controllers.Admin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.Models.*;
import main.Views.NotificationType;
import main.Views.RecipientType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AssignBookController {

    @FXML
    private TextField textTitle1; // BookID field

    @FXML
    private TextField textPublicationYear1; // ClientID field

    @FXML
    private TextField Quantity; // Quantity field

    @FXML
    private Button onSubmitReview; // Save Changes button

    @FXML
    private Button notifyMeButton; // Cancel button

    private NotificationRequest currentNotificationRequest = null;

    /**
     * Handle the "Save Changes" button click.
     */

     private void checkNotificationRequest() {
        int clientId = Model.getInstance().getClient().getClientId();
        int bookId = currentBook.getBook_id();
        currentNotificationRequest = Model.getInstance().getDatabaseDriver().getNotificationRequest(clientId, bookId);

        if (currentNotificationRequest == null) {
            notifyMeButton.setText("Notify Me");
            notifyMeButton.setOnAction(event -> onNotifyMeClick());
        } else {
            notifyMeButton.setText("Cancel Request");
            notifyMeButton.setOnAction(event -> onCancel());
        }
    }
    @FXML
    private void onSubmitReview() {
        String bookId = textTitle1.getText();
        String clientId = textPublicationYear1.getText();

        int bookIdint = Integer.parseInt(bookId);
        int clientIdint = Integer.parseInt(clientId);

        if (bookId.isEmpty() || clientId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please fill in all fields before saving changes.");
            return;
        }

        Boolean check = Model.getInstance().getDatabaseDriver().CheckIfClientReal(clientIdint);
        if(check == false){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please sign up for a valid client id.");
        }


        int activeBorrows = Model.getInstance().getDatabaseDriver().getActiveBorrowTransactions(clientIdint).size();
        if (activeBorrows >= 3) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Borrow Limit Reached");
            alert.setHeaderText(null);
            alert.setContentText("You have reached the maximum limit of 3 borrowed books.");
            alert.showAndWait();
            return;
        }
        boolean hasActiveBorrow = Model.getInstance().getDatabaseDriver().hasActiveBorrowForBook(clientIdint,
                bookIdint);
        if (hasActiveBorrow) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Borrow");
            alert.setHeaderText(null);
            alert.setContentText(
                    "You have already borrowed this book. You cannot borrow multiple copies of the same book at the same time.");
            alert.showAndWait();
            return;
        }
        BookCopy availableCopy = Model.getInstance().getDatabaseDriver().getAvailableBookCopy(bookIdint);
        if (availableCopy == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Copies Available");
            alert.setHeaderText(null);
            alert.setContentText(
                    "No copies are currently available. You can click 'Notify Me' to be notified when a copy becomes available.");
            alert.showAndWait();
            return;
        }

        Book currentBook = Model.getInstance().getBookDataByCopyID(bookIdint);

        boolean transactionCreated = Model.getInstance().getDatabaseDriver().createBorrowTransaction(clientIdint,
        bookIdint);

        if (transactionCreated) {
            boolean copyUpdated = Model.getInstance().getDatabaseDriver()
                    .updateBookCopyAvailability(availableCopy.getCopyId(), false);
            if (copyUpdated) {
                currentBook
                        .setQuantity(Model.getInstance().getDatabaseDriver().countBookCopies(bookIdint));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Borrow Successful");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully borrowed the book.");
                alert.showAndWait();
                createBorrowConfirmedNotification(clientIdint, bookIdint);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to update book copy availability.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to create borrow transaction.");
            alert.showAndWait();
        }
    }

    private void createBorrowConfirmedNotification(int clientId, int bookId) {
        Book currentBook = Model.getInstance().getBookDataByCopyID(bookIdint);
        String bookTitle = currentBook.getTitle();
        int recipientId = clientId;
        RecipientType recipientType = RecipientType.Client;
        NotificationType notificationType = NotificationType.BorrowConfirmed;
        String message = String.format("Bạn đã mượn thành công cuốn sách có ID: %d. Hạn trả sách là ngày %s.",
                bookId, LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        Notification borrowConfirmedNotification = new Notification(recipientId, recipientType, notificationType,
                message);

        Model.getInstance().insertNotification(borrowConfirmedNotification);
    }
   

    private void onCancel() {
        if (currentNotificationRequest != null) {
            boolean success = Model.getInstance().getDatabaseDriver()
                    .deleteNotificationRequest(currentNotificationRequest.getRequestId());
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Request Canceled");
                alert.setHeaderText(null);
                alert.setContentText("Your notification request has been canceled.");
                alert.showAndWait();

                currentNotificationRequest = null;
              
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to cancel your notification request. Please try again later.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onNotifyMeClick() {
        int clientId = Model.getInstance().getClient().getClientId();
        boolean requestCreated = Model.getInstance().getDatabaseDriver().createNotificationRequest(clientId,
                currentBook.getBook_id());
        if (requestCreated) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification Request");
            alert.setHeaderText(null);
            alert.setContentText("You will be notified when the book becomes available.");
            alert.showAndWait();
            sendNotificationBorrowRequestConfirmed();
            checkNotificationRequest();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to create notification request.");
            alert.showAndWait();
        }
    }

    /**
     * Show an error message in an alert dialog.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
