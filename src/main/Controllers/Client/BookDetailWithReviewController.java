package main.Controllers.Client;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Models.Book;
import main.Models.BookReview;
import main.Models.Model;
import main.Models.Notification;
import main.Models.NotificationRequest;
import main.Views.NotificationType;
import main.Views.RecipientType;
import main.Models.BookCopy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookDetailWithReviewController {

    @FXML
    private ImageView bookImageView;
    @FXML
    private Label labelTitle, labelAuthor, labelISBN, labelGenre, labelLanguage, labelPublicationYear,
            labelAverageRating, labelReviewCount;
    @FXML
    private TextArea textDescription;
    @FXML
    private TableView<BookReview> reviewTable;
    @FXML
    private TableColumn<BookReview, String> colRating;
    @FXML
    private TableColumn<BookReview, String> colComment;
    @FXML
    private TableColumn<BookReview, String> colReviewDate;
    @FXML
    private HBox writeReviewStars;
    @FXML
    private TextArea writeReviewTextArea;
    @FXML
    private Button notifyMeButton;

    private NotificationRequest currentNotificationRequest = null;

    private Book currentBook;
    private BookReview userReview = null;
    private int selectedRating = 0;

    public void setBook(Book book) {
        this.currentBook = book;
        displayBookDetails();
        loadReviews();
        initializeWriteReviewSection();
        checkNotificationRequest();

    }

    private void checkNotificationRequest() {
        int clientId = Model.getInstance().getClient().getClientId();
        int bookId = currentBook.getBook_id();
        currentNotificationRequest = Model.getInstance().getDatabaseDriver().getNotificationRequest(clientId, bookId);

        if (currentNotificationRequest == null) {
            notifyMeButton.setText("Notify Me");
            notifyMeButton.setOnAction(event -> onNotifyMeClick());
        } else {
            notifyMeButton.setText("Cancel Request");
            notifyMeButton.setOnAction(event -> onCancelRequestClick());
        }
    }

    private void displayBookDetails() {
        labelTitle.textProperty().bind(Bindings.concat("Title: ", currentBook.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat("Author: ", currentBook.authorProperty()));
        labelISBN.textProperty().bind(Bindings.concat("ISBN: ", currentBook.isbnProperty()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ", currentBook.genreProperty()));
        labelLanguage.textProperty().bind(Bindings.concat("Language: ", currentBook.languageProperty()));
        labelPublicationYear.textProperty()
                .bind(Bindings.concat("Publication Year: ", currentBook.publication_yearProperty().asString()));
        labelAverageRating.textProperty()
                .bind(Bindings.concat("Average Rating: ", currentBook.average_ratingProperty().asString("%.2f")));
        labelReviewCount.textProperty()
                .bind(Bindings.concat("Number of Reviews: ", currentBook.review_countProperty().asString()));
        textDescription.textProperty().bind(Bindings.concat(currentBook.descriptionProperty()));

        if (currentBook.getImagePath() != null && !currentBook.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(currentBook.getImagePath()));
                bookImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Image not found: " + currentBook.getImagePath());
                bookImageView.setImage(null);
            }
        } else {
            bookImageView.setImage(null);
        }
    }

    private void loadReviews() {
        ObservableList<BookReview> reviews = FXCollections.observableArrayList(
                Model.getInstance().getDatabaseDriver().getAllReviewsForBook(currentBook.getBook_id()));
        reviewTable.setItems(reviews);

        colRating.setCellValueFactory(cellData -> {
            double rating = cellData.getValue().getRating();
            String stars = getStars(rating);
            return new javafx.beans.property.SimpleStringProperty(stars);
        });

        colComment.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getComment()));

        colReviewDate.setCellValueFactory(cellData -> {
            String formattedDate = cellData.getValue().getReviewDate()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });
    }

    private String getStars(double rating) {
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating;
        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        for (int i = fullStars; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }

    private void initializeWriteReviewSection() {
        writeReviewStars.getChildren().clear();
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("☆");
            star.setFont(Font.font(24));
            star.setStyle("-fx-text-fill: gold;");
            final int starValue = i;

            star.setOnMouseClicked(event -> {
                selectedRating = starValue;
                updateWriteReviewStars();
            });

            star.setOnMouseEntered(event -> {
                highlightWriteReviewStars(starValue);
            });

            star.setOnMouseExited(event -> {
                highlightWriteReviewStars(selectedRating);
            });

            writeReviewStars.getChildren().add(star);
        }

        userReview = Model.getInstance().getDatabaseDriver().getUserReview(currentBook.getBook_id(),
                Model.getInstance().getClient().getClientId());
        if (userReview != null) {
            selectedRating = (int) Math.round(userReview.getRating());
            writeReviewTextArea.setText(userReview.getComment() != null ? userReview.getComment() : "");
            updateWriteReviewStars();
        }
    }

    private void updateWriteReviewStars() {
        for (int i = 0; i < 5; i++) {
            Label star = (Label) writeReviewStars.getChildren().get(i);
            if (i < selectedRating) {
                star.setText("★");
            } else {
                star.setText("☆");
            }
        }
    }

    private void highlightWriteReviewStars(int rating) {
        for (int i = 0; i < 5; i++) {
            Label star = (Label) writeReviewStars.getChildren().get(i);
            if (i < rating) {
                star.setText("★");
            } else {
                star.setText("☆");
            }
        }
    }

    @FXML
    private void onSubmitReview() {
        String reviewText = writeReviewTextArea.getText().trim();

        if (reviewText.isEmpty() && selectedRating == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Review");
            alert.setHeaderText(null);
            alert.setContentText("Please provide a rating and/or a comment.");
            alert.showAndWait();
            return;
        }

        boolean success = Model.getInstance().getDatabaseDriver().upsertBookReview(
                currentBook.getBook_id(),
                Model.getInstance().getClient().getClientId(),
                selectedRating > 0 ? (double) selectedRating : null,
                !reviewText.isEmpty() ? reviewText : null);

        if (success) {
            if (userReview == null) {
                currentBook.setReview_count(currentBook.getReview_count() + 1);
            }

            double newAverageRating = calculateNewAverageRating(currentBook);
            currentBook.setAverage_rating(newAverageRating);

            displayBookDetails();
            loadReviews();
            initializeWriteReviewSection();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Review Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Thank you for your review!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to save your review. Please try again later.");
            alert.showAndWait();
        }
    }

    private double calculateNewAverageRating(Book book) {
        int totalReviews = Model.getInstance().getDatabaseDriver().getReviewCount(book.getBook_id());
        double sumRatings = Model.getInstance().getDatabaseDriver().getSumRatings(book.getBook_id());
        if (totalReviews == 0)
            return 0.0;
        return sumRatings / totalReviews;
    }

    @FXML
    private void onCancel() {
        selectedRating = userReview != null ? (int) Math.round(userReview.getRating()) : 0;
        writeReviewTextArea.setText(userReview != null ? userReview.getComment() : "");
        updateWriteReviewStars();
    }

    @FXML
    private void onBorrowClick() {
        int clientId = Model.getInstance().getClient().getClientId();
        int activeBorrows = Model.getInstance().getDatabaseDriver().getActiveBorrowTransactions(clientId).size();
        if (activeBorrows >= 3) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Borrow Limit Reached");
            alert.setHeaderText(null);
            alert.setContentText("You have reached the maximum limit of 3 borrowed books.");
            alert.showAndWait();
            return;
        }

        boolean hasActiveBorrow = Model.getInstance().getDatabaseDriver().hasActiveBorrowForBook(clientId,
                currentBook.getBook_id());
        if (hasActiveBorrow) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Borrow");
            alert.setHeaderText(null);
            alert.setContentText(
                    "You have already borrowed this book. You cannot borrow multiple copies of the same book at the same time.");
            alert.showAndWait();
            return;
        }

        BookCopy availableCopy = Model.getInstance().getDatabaseDriver().getAvailableBookCopy(currentBook.getBook_id());
        if (availableCopy == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Copies Available");
            alert.setHeaderText(null);
            alert.setContentText(
                    "No copies are currently available. You can click 'Notify Me' to be notified when a copy becomes available.");
            alert.showAndWait();
            return;
        }

        boolean transactionCreated = Model.getInstance().getDatabaseDriver().createBorrowTransaction(clientId,
                availableCopy.getCopyId());
        if (transactionCreated) {
            boolean copyUpdated = Model.getInstance().getDatabaseDriver()
                    .updateBookCopyAvailability(availableCopy.getCopyId(), false);
            if (copyUpdated) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Borrow Successful");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully borrowed the book.");
                alert.showAndWait();
                createBorrowConfirmedNotification(clientId, currentBook.getBook_id());
                notifyBorrowTransactionCreated();
                loadReviews();
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

    private void onCancelRequestClick() {
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
                notifyMeButton.setText("Notify Me");
                notifyMeButton.setOnAction(event -> onNotifyMeClick());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to cancel your notification request. Please try again later.");
                alert.showAndWait();
            }
        }
    }

    private void notifyBorrowTransactionCreated() {
        Model.getInstance().notifyBorrowTransactionClientCreatedEvent();
    }

    private void sendNotificationBorrowRequestConfirmed() {
        String bookTitle = currentBook.getTitle();
        int recipientId = Model.getInstance().getClient().getClientId();
        RecipientType recipientType = RecipientType.Client;
        NotificationType notificationType = NotificationType.BorrowRequestConfirmed;
        String message = "Your notification request on " + bookTitle + " has been confirmed.";
        Notification notification = new Notification(recipientId, recipientType, notificationType, message);
        Model.getInstance().insertNotification(notification);
    }

    private void createBorrowConfirmedNotification(int clientId, int bookId) {
        String bookTitle = currentBook.getTitle();
        int recipientId = Model.getInstance().getClient().getClientId();
        RecipientType recipientType = RecipientType.Client;
        NotificationType notificationType = NotificationType.BorrowConfirmed;
        String message = String.format("Bạn đã mượn thành công cuốn sách có ID: %d. Hạn trả sách là ngày %s.",
                bookId, LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        Notification borrowConfirmedNotification = new Notification(recipientId, recipientType, notificationType,
                message);

        Model.getInstance().insertNotification(borrowConfirmedNotification);
    }
}
