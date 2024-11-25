package main.Models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class BookReview {
    private final IntegerProperty reviewId;
    private final IntegerProperty bookId;
    private final IntegerProperty clientId;
    private final DoubleProperty rating;
    private final StringProperty comment;
    private final ObjectProperty<LocalDateTime> reviewDate;

    public BookReview(int reviewId, int bookId, int clientId, double rating, String comment, LocalDateTime reviewDate) {
        this.reviewId = new SimpleIntegerProperty(reviewId);
        this.bookId = new SimpleIntegerProperty(bookId);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.rating = new SimpleDoubleProperty(rating);
        this.comment = new SimpleStringProperty(comment);
        this.reviewDate = new SimpleObjectProperty<>(reviewDate);
    }

    public int getReviewId() {
        return reviewId.get();
    }

    public void setReviewId(int value) {
        this.reviewId.set(value);
    }

    public IntegerProperty reviewIdProperty() {
        return reviewId;
    }

    public int getBookId() {
        return bookId.get();
    }

    public void setBookId(int value) {
        this.bookId.set(value);
    }

    public IntegerProperty bookIdProperty() {
        return bookId;
    }

    public int getClientId() {
        return clientId.get();
    }

    public void setClientId(int value) {
        this.clientId.set(value);
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public double getRating() {
        return rating.get();
    }

    public void setRating(double value) {
        this.rating.set(value);
    }

    public DoubleProperty ratingProperty() {
        return rating;
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String value) {
        this.comment.set(value);
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate.get();
    }

    public void setReviewDate(LocalDateTime value) {
        this.reviewDate.set(value);
    }

    public ObjectProperty<LocalDateTime> reviewDateProperty() {
        return reviewDate;
    }
}
