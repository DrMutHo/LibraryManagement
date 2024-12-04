package main.Models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Represents a review for a book by a client.
 */
public class BookReview {
    private final IntegerProperty reviewId;
    private final IntegerProperty bookId;
    private final IntegerProperty clientId;
    private final DoubleProperty rating;
    private final StringProperty comment;
    private final ObjectProperty<LocalDateTime> reviewDate;

    /**
     * Constructs a new BookReview instance with the specified values for review ID, book ID, client ID, rating, 
     * comment, and review date.
     *
     * @param reviewId The unique identifier for the review.
     * @param bookId The ID of the book being reviewed.
     * @param clientId The ID of the client who made the review.
     * @param rating The rating given to the book by the client.
     * @param comment The comment made by the client about the book.
     * @param reviewDate The date and time when the review was made.
     */
    public BookReview(int reviewId, int bookId, int clientId, double rating, String comment, LocalDateTime reviewDate) {
        this.reviewId = new SimpleIntegerProperty(reviewId);
        this.bookId = new SimpleIntegerProperty(bookId);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.rating = new SimpleDoubleProperty(rating);
        this.comment = new SimpleStringProperty(comment);
        this.reviewDate = new SimpleObjectProperty<>(reviewDate);
    }

    /**
     * Gets the unique identifier for the review.
     *
     * @return The unique identifier for the review.
     */
    public int getReviewId() {
        return reviewId.get();
    }

    /**
     * Sets the unique identifier for the review.
     *
     * @param value The unique identifier to be assigned to the review.
     */
    public void setReviewId(int value) {
        this.reviewId.set(value);
    }

    /**
     * Gets the property for the review ID.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The IntegerProperty representing the review ID.
     */
    public IntegerProperty reviewIdProperty() {
        return reviewId;
    }

    /**
     * Gets the ID of the book being reviewed.
     *
     * @return The ID of the book being reviewed.
     */
    public int getBookId() {
        return bookId.get();
    }

    /**
     * Sets the ID of the book being reviewed.
     *
     * @param value The ID of the book to be assigned to this review.
     */
    public void setBookId(int value) {
        this.bookId.set(value);
    }

    /**
     * Gets the property for the book ID.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The IntegerProperty representing the book ID.
     */
    public IntegerProperty bookIdProperty() {
        return bookId;
    }

    /**
     * Gets the ID of the client who made the review.
     *
     * @return The ID of the client who made the review.
     */
    public int getClientId() {
        return clientId.get();
    }

    /**
     * Sets the ID of the client who made the review.
     *
     * @param value The ID of the client to be assigned to this review.
     */
    public void setClientId(int value) {
        this.clientId.set(value);
    }

    /**
     * Gets the property for the client ID.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The IntegerProperty representing the client ID.
     */
    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    /**
     * Gets the rating given to the book.
     *
     * @return The rating given to the book.
     */
    public double getRating() {
        return rating.get();
    }

    /**
     * Sets the rating for the book.
     *
     * @param value The rating to be assigned to the book.
     */
    public void setRating(double value) {
        this.rating.set(value);
    }

    /**
     * Gets the property for the rating.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The DoubleProperty representing the rating.
     */
    public DoubleProperty ratingProperty() {
        return rating;
    }

    /**
     * Gets the comment made by the client about the book.
     *
     * @return The comment made by the client about the book.
     */
    public String getComment() {
        return comment.get();
    }

    /**
     * Sets the comment made by the client about the book.
     *
     * @param value The comment to be assigned to the book review.
     */
    public void setComment(String value) {
        this.comment.set(value);
    }

    /**
     * Gets the property for the comment.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The StringProperty representing the comment.
     */
    public StringProperty commentProperty() {
        return comment;
    }

    /**
     * Gets the date and time when the review was made.
     *
     * @return The date and time when the review was made.
     */
    public LocalDateTime getReviewDate() {
        return reviewDate.get();
    }

    /**
     * Sets the date and time when the review was made.
     *
     * @param value The date and time to be assigned to the review.
     */
    public void setReviewDate(LocalDateTime value) {
        this.reviewDate.set(value);
    }

    /**
     * Gets the property for the review date.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The ObjectProperty representing the review date.
     */
    public ObjectProperty<LocalDateTime> reviewDateProperty() {
        return reviewDate;
    }
}
