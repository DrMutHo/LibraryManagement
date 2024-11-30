package main.Models;

import java.time.LocalDateTime;

public class NotificationRequest {
    private int requestId;
    private int clientId;
    private int bookId;
    private LocalDateTime requestDate;

    public NotificationRequest(int requestId, int clientId, int bookId, LocalDateTime requestDate) {
        this.requestId = requestId;
        this.clientId = clientId;
        this.bookId = bookId;
        this.requestDate = requestDate;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
