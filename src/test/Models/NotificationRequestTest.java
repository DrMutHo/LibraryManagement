package test.Models;

import main.Models.NotificationRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRequestTest {

    @Test
    void testNotificationRequestConstructor() {
        // Given
        int requestId = 1;
        int clientId = 101;
        int bookId = 202;
        LocalDateTime requestDate = LocalDateTime.now();

        // When
        NotificationRequest notificationRequest = new NotificationRequest(requestId, clientId, bookId, requestDate);

        // Then
        assertEquals(requestId, notificationRequest.getRequestId());
        assertEquals(clientId, notificationRequest.getClientId());
        assertEquals(bookId, notificationRequest.getBookId());
        assertEquals(requestDate, notificationRequest.getRequestDate());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        NotificationRequest notificationRequest = new NotificationRequest(1, 101, 202, LocalDateTime.now());

        // When
        notificationRequest.setRequestId(2);
        notificationRequest.setClientId(102);
        notificationRequest.setBookId(203);
        LocalDateTime newRequestDate = LocalDateTime.of(2024, 12, 1, 10, 0);
        notificationRequest.setRequestDate(newRequestDate);

        // Then
        assertEquals(2, notificationRequest.getRequestId());
        assertEquals(102, notificationRequest.getClientId());
        assertEquals(203, notificationRequest.getBookId());
        assertEquals(newRequestDate, notificationRequest.getRequestDate());
    }

    @Test
    void testRequestId() {
        // Given
        NotificationRequest notificationRequest = new NotificationRequest(1, 101, 202, LocalDateTime.now());

        // When
        notificationRequest.setRequestId(10);

        // Then
        assertEquals(10, notificationRequest.getRequestId());
    }

    @Test
    void testClientId() {
        // Given
        NotificationRequest notificationRequest = new NotificationRequest(1, 101, 202, LocalDateTime.now());

        // When
        notificationRequest.setClientId(202);

        // Then
        assertEquals(202, notificationRequest.getClientId());
    }

    @Test
    void testBookId() {
        // Given
        NotificationRequest notificationRequest = new NotificationRequest(1, 101, 202, LocalDateTime.now());

        // When
        notificationRequest.setBookId(303);

        // Then
        assertEquals(303, notificationRequest.getBookId());
    }

    @Test
    void testRequestDate() {
        // Given
        LocalDateTime requestDate = LocalDateTime.of(2024, 12, 1, 10, 0);
        NotificationRequest notificationRequest = new NotificationRequest(1, 101, 202, requestDate);

        // When
        LocalDateTime newRequestDate = LocalDateTime.of(2024, 12, 2, 15, 0);
        notificationRequest.setRequestDate(newRequestDate);

        // Then
        assertEquals(newRequestDate, notificationRequest.getRequestDate());
    }
}
