package main.Models;

import java.time.LocalDateTime;

/**
 * The {@code NotificationRequest} class represents a request for a notification
 * within the system. It encapsulates details such as the request ID, client ID,
 * book ID, and the date the request was made.
 */
public class NotificationRequest {
    private int requestId;
    private int clientId;
    private int bookId;
    private LocalDateTime requestDate;

    /**
     * Constructs a new {@code NotificationRequest} with the specified details.
     *
     * @param requestId   The unique identifier for the notification request.
     * @param clientId    The ID of the client making the request.
     * @param bookId      The ID of the book associated with the request.
     * @param requestDate The date and time when the request was made.
     */
    public NotificationRequest(int requestId, int clientId, int bookId, LocalDateTime requestDate) {
        this.requestId = requestId;
        this.clientId = clientId;
        this.bookId = bookId;
        this.requestDate = requestDate;
    }

    /**
     * Retrieves the request ID.
     *
     * @return The unique identifier of the notification request.
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Sets the request ID.
     *
     * @param requestId The unique identifier to set for the notification request.
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * Retrieves the client ID associated with the request.
     *
     * @return The ID of the client who made the request.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID for the request.
     *
     * @param clientId The ID of the client to associate with the notification request.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Retrieves the book ID associated with the request.
     *
     * @return The ID of the book related to the notification request.
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Sets the book ID for the request.
     *
     * @param bookId The ID of the book to associate with the notification request.
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Retrieves the date and time when the request was made.
     *
     * @return The {@code LocalDateTime} representing when the notification request was created.
     */
    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the date and time for the request.
     *
     * @param requestDate The {@code LocalDateTime} to set as the request date and time.
     */
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
