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
     * Constructor for creating a NotificationRequest instance.
     * 
     * @param requestId   The unique ID of the request.
     * @param clientId    The ID of the client making the request.
     * @param bookId      The ID of the book being requested.
     * @param requestDate The date and time when the request was made.
     */
    public NotificationRequest(int requestId, int clientId, int bookId, LocalDateTime requestDate) {
        this.requestId = requestId;
        this.clientId = clientId;
        this.bookId = bookId;
        this.requestDate = requestDate;
    }

    /**
     * Gets the unique request ID.
     * 
     * @return The unique request ID.
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Sets the unique request ID.
     * 
     * @param requestId The request ID to set.
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the client ID of the user who made the request.
     * 
     * @return The client ID.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID of the user making the request.
     * 
     * @param clientId The client ID to set.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the book ID of the book being requested.
     * 
     * @return The book ID.
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Sets the book ID of the book being requested.
     * 
     * @param bookId The book ID to set.
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Gets the date and time when the request was made.
     * 
     * @return The request date and time.
     */
    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the date and time when the request was made.
     * 
     * @param requestDate The request date and time to set.
     */
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
