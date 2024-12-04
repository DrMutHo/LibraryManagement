package main.Controllers.Admin;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Models.Book;
import main.Models.BookReview;
import main.Models.Model;
import main.Models.Notification;
import main.Models.NotificationRequest;
import main.Views.ClientMenuOptions;
import main.Views.NotificationType;
import main.Views.RecipientType;
import main.Models.BookCopy;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BookDetailWithReviewController {

    @FXML
    private ImageView bookImageView;
    @FXML
    private Label labelTitle, labelAuthor, labelISBN, labelGenre, labelLanguage, labelPublicationYear,
            labelAverageRating, labelReviewCount, labelDescription;
    @FXML
    private TableView<BookReview> reviewTable;
    @FXML
    private TableColumn<BookReview, String> colRating;
    @FXML
    private TableColumn<BookReview, String> colComment;
    @FXML
    private TableColumn<BookReview, String> colReviewDate;
    @FXML
    private TableColumn<BookReview, String> colReviewer;

    private Book currentBook;

    /**
     * Sets the current book and updates the UI with the book's details, reviews, 
     * and the review submission section. It also checks if the client has a notification 
     * request for this book, such as if they want to be notified when the book is available.
     * 
     * @param book The {@link Book} object to be displayed in the view.
     */
    public void setBook(Book book) {
        this.currentBook = book;
        System.out.println(currentBook.titleProperty());  // Prints the title of the book for debugging
        displayBookDetails();  // Updates the UI with the book's details
        loadReviews();  // Loads the reviews for the current book
        initializeWriteReviewSection();  // Initializes the section for writing a review
        checkNotificationRequest();  // Checks if there's an existing notification request for the book
    }


    /**
     * Checks if there is an existing notification request for the current book by the logged-in client.
     * If no request exists, the "Notify Me" button is displayed, allowing the client to request a notification
     * when the book becomes available. If a request already exists, the button text changes to "Cancel Request"
     * and allows the client to cancel the notification request.
     */
    private void checkNotificationRequest() {
        int clientId = Model.getInstance().getClient().getClientId();  // Gets the client ID of the logged-in user
        int bookId = currentBook.getBook_id();  // Gets the ID of the current book
        currentNotificationRequest = Model.getInstance().getDatabaseDriver().getNotificationRequest(clientId, bookId);  // Fetches any existing notification request

        if (currentNotificationRequest == null) {
            // If no notification request exists, show the "Notify Me" button
            notifyMeButton.setText("Notify Me");
            notifyMeButton.setOnAction(event -> onNotifyMeClick());  // When clicked, triggers onNotifyMeClick
        } else {
            // If a notification request already exists, show the "Cancel Request" button
            notifyMeButton.setText("Cancel Request");
            notifyMeButton.setOnAction(event -> onCancelRequestClick());  // When clicked, triggers onCancelRequestClick
        }
    }


    /**
     * Displays the details of the current book in the respective UI components.
     * This includes binding the book's properties to the respective labels and 
     * setting the book image if available.
     */
    private void displayBookDetails() {
        // Bind the book's properties to the UI labels
        labelTitle.textProperty().bind(Bindings.concat(currentBook.titleProperty()));
        labelAuthor.textProperty().bind(Bindings.concat(currentBook.authorProperty()));
        labelISBN.textProperty().bind(Bindings.concat("ISBN: ", currentBook.isbnProperty()));
        labelGenre.textProperty().bind(Bindings.concat("Genre: ", currentBook.genreProperty()));
        labelLanguage.textProperty().bind(Bindings.concat("Language: ", currentBook.languageProperty()));
        if (currentBook.getPublication_year() == -1) {
            labelPublicationYear.textProperty()
                    .bind(Bindings.concat("Publication Year: No Publication Year",
                            currentBook.publication_yearProperty().asString()));
        } else
            labelPublicationYear.textProperty()
                    .bind(Bindings.concat("Publication Year: ",
                            currentBook.publication_yearProperty().asString()));
        labelAverageRating.textProperty()
                .bind(Bindings.createStringBinding(() -> getStars(currentBook.average_ratingProperty().get()),
                        currentBook.average_ratingProperty()));
        labelAverageRating.setStyle("-fx-text-fill: gold;");
        labelReviewCount.textProperty()
                .bind(Bindings.concat("(", currentBook.review_countProperty().asString(), " ratings)"));
        labelDescription.textProperty().bind(Bindings.concat(currentBook.descriptionProperty()));

        // Set the book image if a valid path is provided
        if (currentBook.getImage_path() != null && !currentBook.getImage_path().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(currentBook.getImage_path()));
                bookImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Image not found: " + currentBook.getImage_path());
                bookImageView.setImage(null);
            }
        } else {
            bookImageView.setImage(null);
        }
    }


    /**
     * Loads and displays all reviews for the current book in the review table.
     * Reviews are displayed in the table with rating, comment, reviewer name, and review date.
     * Comments that are too long are wrapped in a TextFlow to ensure they fit within the table cell.
     */
    private void loadReviews() {
        // Load all reviews for the current book from the database
        ObservableList<BookReview> reviews = FXCollections.observableArrayList(
                Model.getInstance().getDatabaseDriver().getAllReviewsForBook(currentBook.getBook_id()));
        reviewTable.setItems(reviews); // Set the reviews to be displayed in the table

        // Rating column: Display rating as stars
        colRating.setCellValueFactory(cellData -> {
            double rating = cellData.getValue().getRating();
            String stars = getStars(rating);
            return new javafx.beans.property.SimpleStringProperty(stars);
        });

        // Reviewer column: Display reviewer name
        colReviewer.setCellValueFactory(cellData -> {
            String clientName = Model.getInstance().getDatabaseDriver()
                    .getClientNameById(cellData.getValue().getClientId());
            return new javafx.beans.property.SimpleStringProperty(clientName);
        });

        // Comment column: Display the comment with wrapping for long comments
        colComment.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getComment()));

        colComment.setCellFactory(column -> {
            return new TableCell<BookReview, String>() {
                private final TextFlow textFlow = new TextFlow();

                {
                    textFlow.setMaxWidth(Double.MAX_VALUE); // Make sure TextFlow takes up the full width of the cell
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        Text text = new Text(item); // Create a Text node for the comment
                        text.setWrappingWidth(300); // Set wrapping width to wrap long comments
                        textFlow.getChildren().clear();
                        textFlow.getChildren().add(text); // Add the Text node to TextFlow
                        setGraphic(textFlow); // Set TextFlow as the graphic of the cell

                        // Adjust the height of the cell based on the height of the comment
                        double height = getTextHeight(item);
                        setPrefHeight(height);
                    }
                }

                private double getTextHeight(String comment) {
                    Text text = new Text(comment);
                    text.setWrappingWidth(300); // Set wrapping width for proper line wrapping
                    text.setFont(Font.getDefault()); // Use the default font for height calculation
                    return Math.max(40, text.getLayoutBounds().getHeight()); // Ensure a minimum height for the cell
                }
            };
        });

        // Review Date column: Format and display the review date
        colReviewDate.setCellValueFactory(cellData -> {
            String formattedDate = cellData.getValue().getReviewDate()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });
    }


    /**
     * Converts a numeric rating to a string representation of stars.
     * Full stars are represented by "★" and empty stars by "☆".
     * The number of full stars corresponds to the integer part of the rating,
     * and the number of empty stars completes the total to 5.
     *
     * @param rating The numeric rating value (e.g., from 0 to 5).
     * @return A string containing the star rating representation (e.g., "★★★☆☆").
     */
    private String getStars(double rating) {
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating; // Get the integer part of the rating for full stars
        for (int i = 0; i < fullStars; i++) {
            stars.append("★"); // Add full star
        }
        for (int i = fullStars; i < 5; i++) {
            stars.append("☆"); // Add empty star to fill up to 5
        }
        return stars.toString(); // Return the constructed star rating string
    }


    /**
     * Initializes the section where the user can write and rate a book review.
     * This includes setting up the star rating system and displaying the existing review (if any).
     * 
     * The star rating system allows the user to select a rating between 1 and 5 by clicking on stars.
     * When the user clicks a star, it updates the selected rating and visually highlights the stars.
     * If the user has already submitted a review for the current book, it will be pre-filled in the review section.
     */
    private void initializeWriteReviewSection() {
        // Clear previous star ratings if any
        writeReviewStars.getChildren().clear();
        
        // Create the star rating system with 5 stars
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("☆");
            star.setFont(Font.font(24)); // Set font size for the stars
            star.setStyle("-fx-text-fill: gold;"); // Set star color to gold
            final int starValue = i;

            // Add event listener for star click to select rating
            star.setOnMouseClicked(event -> {
                selectedRating = starValue;
                updateWriteReviewStars(); // Update star visuals based on selected rating
            });

            // Add event listener for mouse enter to highlight stars
            star.setOnMouseEntered(event -> {
                highlightWriteReviewStars(starValue); // Highlight stars up to the hovered one
            });

            // Add event listener for mouse exit to revert the highlight to the selected rating
            star.setOnMouseExited(event -> {
                highlightWriteReviewStars(selectedRating); // Revert highlight to the current selection
            });

            // Add the star to the UI
            writeReviewStars.getChildren().add(star);
        }

        // Retrieve the user's previous review for the current book, if any
        userReview = Model.getInstance().getDatabaseDriver().getUserReview(currentBook.getBook_id(),
                Model.getInstance().getClient().getClientId());

        // If a review exists, pre-fill the review section
        if (userReview != null) {
            selectedRating = (int) Math.round(userReview.getRating()); // Set the rating to the user's previous rating
            writeReviewTextArea.setText(userReview.getComment() != null ? userReview.getComment() : ""); // Pre-fill the comment
            updateWriteReviewStars(); // Update star visuals based on the stored rating
        }
    }


    /**
     * Updates the star rating display based on the current selected rating.
     * This method sets the text of each star to a filled star (★) for ratings less than the selected rating
     * and to an empty star (☆) for ratings greater than or equal to the selected rating.
     */
    private void updateWriteReviewStars() {
        for (int i = 0; i < 5; i++) {
            Label star = (Label) writeReviewStars.getChildren().get(i);
            // Set star to filled (★) if its index is less than the selected rating
            if (i < selectedRating) {
                star.setText("★");
            } else {
                // Otherwise set it to empty (☆)
                star.setText("☆");
            }
        }
    }

    /**
     * Highlights the stars up to the specified rating value.
     * This method updates the display of the stars to reflect the rating being hovered over by the user.
     * Stars up to the specified rating are shown as filled (★), and the rest are shown as empty (☆).
     *
     * @param rating The rating value to highlight (1 to 5).
     */
    private void highlightWriteReviewStars(int rating) {
        for (int i = 0; i < 5; i++) {
            Label star = (Label) writeReviewStars.getChildren().get(i);
            // Highlight stars up to the specified rating
            if (i < rating) {
                star.setText("★");
            } else {
                // Set the rest of the stars as empty
                star.setText("☆");
            }
        }
    }


    /**
     * Handles the submission of a book review.
     * This method checks if the user has provided a rating and/or a comment. If both are missing,
     * a warning alert is shown. If a rating or comment is provided, the review is saved to the database.
     * Upon successful submission, the book's average rating and review count are updated,
     * and the UI is refreshed to reflect the changes.
     * 
     * If the submission is successful, a confirmation alert is shown. If there is an error,
     * an error alert is displayed.
     */
    @FXML
    private void onSubmitReview() {
        String reviewText = writeReviewTextArea.getText().trim();

        // Check if both the rating and comment are empty
        if (reviewText.isEmpty() && selectedRating == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Review");
            alert.setHeaderText(null);
            alert.setContentText("Please provide a rating and/or a comment.");
            alert.showAndWait();
            return;
        }

        // Submit the review to the database
        boolean success = Model.getInstance().getDatabaseDriver().upsertBookReview(
                currentBook.getBook_id(),
                Model.getInstance().getClient().getClientId(),
                selectedRating > 0 ? (double) selectedRating : null,
                !reviewText.isEmpty() ? reviewText : null);

        // If the review is successfully saved, update book details and reviews
        if (success) {
            if (userReview == null) {
                currentBook.setReview_count(currentBook.getReview_count() + 1);
            }

            // Calculate the new average rating
            double newAverageRating = calculateNewAverageRating(currentBook);
            currentBook.setAverage_rating(newAverageRating);

            // Refresh the UI to display the updated book details and reviews
            displayBookDetails();
            loadReviews();
            initializeWriteReviewSection();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Review Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Thank you for your review!");
            alert.showAndWait();
        } else {
            // Show error message if submission failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to save your review. Please try again later.");
            alert.showAndWait();
        }
    }


    /**
     * Calculates the new average rating for a book after a review is submitted.
     * This method retrieves the total number of reviews and the sum of ratings for the given book
     * from the database, then calculates and returns the new average rating.
     * 
     * If there are no reviews (totalReviews is 0), the method returns 0.0.
     * 
     * @param book The book for which the average rating is being calculated.
     * @return The new average rating for the book, calculated as the sum of ratings divided by the total number of reviews.
     */
    private double calculateNewAverageRating(Book book) {
        int totalReviews = Model.getInstance().getDatabaseDriver().getReviewCount(book.getBook_id());
        double sumRatings = Model.getInstance().getDatabaseDriver().getSumRatings(book.getBook_id());
        
        // If there are no reviews, return a default rating of 0.0
        if (totalReviews == 0)
            return 0.0;
        
        // Return the average rating
        return sumRatings / totalReviews;
    }


   /**
     * Cancels the review editing process by resetting the review input fields to their initial values.
     * This method is triggered when the user decides to cancel their review submission.
     * 
     * It resets the selected rating and review comment fields to the values of the previously submitted review
     * (if any). The stars and the text area are updated accordingly.
     */
    @FXML
    private void onCancel() {
        // Reset the selected rating to the value of the user's previous review, or 0 if no review exists
        selectedRating = userReview != null ? (int) Math.round(userReview.getRating()) : 0;
        
        // Reset the text area to the previous comment, or an empty string if no review exists
        writeReviewTextArea.setText(userReview != null ? userReview.getComment() : "");
        
        // Update the star ratings to reflect the current state (either the previous rating or default to 0)
        updateWriteReviewStars();
    }


    /**
     * Handles the borrowing process when a user clicks on the "Borrow" button for a book.
     * This method checks the number of active borrow transactions for the logged-in client
     * and ensures that they do not exceed the maximum allowed borrow limit (3 books).
     * 
     * The method also ensures that:
     * - The user cannot borrow the same book multiple times.
     * - The user is informed if no copies of the book are available.
     * - The user receives feedback on the status of the borrow transaction.
     * 
     * If the client has already borrowed 3 books, a warning alert is displayed, and the borrowing 
     * action is not processed.
     * 
     * If the client is eligible to borrow, the method checks if the client has an active borrow 
     * transaction for the selected book. If the client has already borrowed the book, a warning 
     * is displayed. If the book has available copies, the borrowing transaction is created, the 
     * book's availability is updated, and a success notification is displayed.
     * 
     * If there are any errors in the process, appropriate error alerts are shown to the user.
     */
    @FXML
    private void onBorrowClick() {
        // Get the client ID of the logged-in user
        int clientId = Model.getInstance().getClient().getClientId();
        
        // Get the number of active borrow transactions for the client
        int activeBorrows = Model.getInstance().getDatabaseDriver().getActiveBorrowTransactions(clientId).size();
        
        // Check if the client has already borrowed the maximum allowed number of books (3)
        if (activeBorrows >= 3) {
            // Display a warning alert if the borrow limit has been reached
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Borrow Limit Reached");
            alert.setHeaderText(null);
            alert.setContentText("You have reached the maximum limit of 3 borrowed books.");
            alert.showAndWait();
            return;
        }

        // Check if the client already has an active borrow for the current book
        boolean hasActiveBorrow = Model.getInstance().getDatabaseDriver().hasActiveBorrowForBook(clientId, currentBook.getBook_id());
        if (hasActiveBorrow) {
            // Display a warning alert if the client has already borrowed this book
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Borrow");
            alert.setHeaderText(null);
            alert.setContentText("You have already borrowed this book. You cannot borrow multiple copies of the same book at the same time.");
            alert.showAndWait();
            return;
        }

        // Check if an available copy of the book exists
        BookCopy availableCopy = Model.getInstance().getDatabaseDriver().getAvailableBookCopy(currentBook.getBook_id());
        if (availableCopy == null) {
            // Display an information alert if no copies are available
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Copies Available");
            alert.setHeaderText(null);
            alert.setContentText("No copies are currently available. You can click 'Notify Me' to be notified when a copy becomes available.");
            alert.showAndWait();
            return;
        }

        // Create a borrow transaction for the client
        boolean transactionCreated = Model.getInstance().getDatabaseDriver().createBorrowTransaction(clientId, availableCopy.getCopyId());
        if (transactionCreated) {
            // Update the availability of the book copy
            boolean copyUpdated = Model.getInstance().getDatabaseDriver().updateBookCopyAvailability(availableCopy.getCopyId(), false);
            if (copyUpdated) {
                // Update the quantity of available copies for the book
                currentBook.setQuantity(Model.getInstance().getDatabaseDriver().countBookCopies(currentBook.getBook_id()));
                
                // Display a success alert indicating the borrow was successful
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Borrow Successful");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully borrowed the book.");
                alert.showAndWait();
                
                // Create a notification for the successful borrow transaction
                createBorrowConfirmedNotification(clientId, currentBook.getBook_id());
                
                // Notify about the borrow transaction and update reviews
                notifyBorrowTransactionCreated();
                loadReviews();
            } else {
                // Display an error alert if updating the book copy availability failed
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to update book copy availability.");
                alert.showAndWait();
            }
        } else {
            // Display an error alert if creating the borrow transaction failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to create borrow transaction.");
            alert.showAndWait();
        }
    }


    /**
     * Handles the process when a user clicks on the "Notify Me" button to request a notification
     * when a book becomes available for borrowing.
     * 
     * This method performs the following steps:
     * - It creates a notification request for the user (client) by calling the corresponding
     *   method in the database driver, which saves the request for the book they are interested in.
     * - If the request is successfully created, an informational alert is shown to the user,
     *   confirming that they will be notified when the book becomes available.
     * - It also sends a notification to confirm the borrow request.
     * - The method then checks the current notification request and updates the "Notify Me" button
     *   text to indicate the user's current status regarding the request (either "Notify Me" or "Cancel Request").
     * 
     * If the request cannot be created, an error alert is displayed to inform the user of the failure.
     */
    @FXML
    private void onNotifyMeClick() {
        // Get the client ID of the logged-in user
        int clientId = Model.getInstance().getClient().getClientId();
        
        // Create a notification request for the selected book
        boolean requestCreated = Model.getInstance().getDatabaseDriver().createNotificationRequest(clientId, currentBook.getBook_id());
        
        if (requestCreated) {
            // Display an informational alert to notify the user that the request was successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification Request");
            alert.setHeaderText(null);
            alert.setContentText("You will be notified when the book becomes available.");
            alert.showAndWait();
            
            // Send a notification to confirm the borrow request
            sendNotificationBorrowRequestConfirmed();
            
            // Check the notification status and update the button text
            checkNotificationRequest();
        } else {
            // Display an error alert if the notification request could not be created
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to create notification request.");
            alert.showAndWait();
        }
    }


    /**
     * Handles the process when a user clicks on the "Cancel Request" button to cancel their 
     * notification request for a book.
     * 
     * This method performs the following steps:
     * - It checks if there is an existing notification request for the current book.
     * - If a request exists, it calls the method to delete the notification request from the database.
     * - If the cancellation is successful, an informational alert is displayed to inform the user that their request has been canceled.
     * - The button's text is updated to "Notify Me" and the action is reset to allow the user to submit a new notification request.
     * - If the cancellation fails, an error alert is displayed to notify the user of the failure.
     */
    private void onCancelRequestClick() {
        // Check if there is an active notification request
        if (currentNotificationRequest != null) {
            // Attempt to delete the notification request from the database
            boolean success = Model.getInstance().getDatabaseDriver()
                    .deleteNotificationRequest(currentNotificationRequest.getRequestId());
            
            if (success) {
                // If deletion is successful, show a confirmation alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Request Canceled");
                alert.setHeaderText(null);
                alert.setContentText("Your notification request has been canceled.");
                alert.showAndWait();
                
                // Clear the current notification request and reset the button
                currentNotificationRequest = null;
                notifyMeButton.setText("Notify Me");
                notifyMeButton.setOnAction(event -> onNotifyMeClick());
            } else {
                // If deletion fails, show an error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to cancel your notification request. Please try again later.");
                alert.showAndWait();
            }
        }
    }


    /**
     * Notifies the system that a borrow transaction has been created for a client.
     * 
     * This method triggers the event that indicates a borrow transaction has been successfully created 
     * for the logged-in client, allowing any other components or listeners in the system to react accordingly.
     */
    private void notifyBorrowTransactionCreated() {
        // Notify the system of a borrow transaction creation
        Model.getInstance().notifyBorrowTransactionClientCreatedEvent();
    }


    /**
     * Sends a notification to the client confirming that their borrow request has been received and 
     * is being processed.
     * 
     * This method creates a new notification for the client with a specific message about the status of their borrow 
     * request. The notification is then inserted into the system for the client to view.
     * 
     * @param currentBook The book that the borrow request is associated with.
     * @param recipientId The client ID who made the borrow request.
     */
    private void sendNotificationBorrowRequestConfirmed() {
        // Get the book title for the notification message
        String bookTitle = currentBook.getTitle();
        
        // Define the recipient and message content
        int recipientId = Model.getInstance().getClient().getClientId();
        RecipientType recipientType = RecipientType.Client;
        NotificationType notificationType = NotificationType.BorrowRequestConfirmed;
        String message = "Your notification request on " + bookTitle + " has been confirmed.";
        
        // Create a new notification object
        Notification notification = new Notification(recipientId, recipientType, notificationType, message);
        
        // Insert the notification into the system (database)
        Model.getInstance().insertNotification(notification);
    }


    /**
     * Creates and sends a notification to the client confirming the successful borrowing of a book.
     * 
     * This method generates a notification that informs the client of the successful borrowing of a book,
     * including the book ID and the due date for returning the book (7 days from the borrowing date).
     * The notification is then inserted into the system for the client to view.
     * 
     * @param clientId The ID of the client who has borrowed the book.
     * @param bookId The ID of the book that has been borrowed.
     */
    private void createBorrowConfirmedNotification(int clientId, int bookId) {
        // Get the book title and recipient details
        String bookTitle = currentBook.getTitle();
        int recipientId = Model.getInstance().getClient().getClientId();
        RecipientType recipientType = RecipientType.Client;
        NotificationType notificationType = NotificationType.BorrowConfirmed;

        // Format the message with the book ID and due date (7 days from today)
        String message = String.format("Bạn đã mượn thành công cuốn sách có ID: %d. Hạn trả sách là ngày %s.",
                bookId, LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        // Create the notification
        Notification borrowConfirmedNotification = new Notification(recipientId, recipientType, notificationType, message);

        // Insert the notification into the system
        Model.getInstance().insertNotification(borrowConfirmedNotification);
    }


    /**
     * Handles the action when the user clicks on the "Back" button.
     * 
     * This method checks which menu option was selected by the user and navigates the user 
     * back to the corresponding screen. The navigation depends on the selected option from the 
     * client menu. If no valid option is selected, it prints an error message to the console.
     * 
     * The method ensures that the user is returned to the appropriate view based on their 
     * last action, such as browsing books, viewing transactions, or going back to the home screen.
     * 
     * If no valid option is found or the selected option is null, an error message is printed.
     */
    @FXML
    private void onBackButtonClick() {
        // Retrieve the currently selected menu option
        ObjectProperty<ClientMenuOptions> selectedMenuItem = Model.getInstance().getViewFactory()
                .getClientSelectedMenuItem();

        // Check if the selected option is not null and valid
        if (selectedMenuItem != null && selectedMenuItem.get() != null) {
            ClientMenuOptions selectedOption = selectedMenuItem.get();

            // Navigate back to the appropriate screen based on the selected menu option
            if (selectedOption == ClientMenuOptions.BROWSING) {
                Model.getInstance().getClientController().goBackToBrowsing();
            } else if (selectedOption == ClientMenuOptions.BORROWTRANSACTION) {
                Model.getInstance().getClientController().goBackToTransaction();
            } else if (selectedOption == ClientMenuOptions.HOME) {
                Model.getInstance().getClientController().goBackToHome();
            } else {
                // If an unknown option is selected, print an error message
                System.out.println("Unknown selected option");
            }
        } else {
            // If no valid option is selected, print an error message
            System.out.println("No option selected or invalid selection");
        }
    }
}
