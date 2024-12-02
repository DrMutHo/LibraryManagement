package test.Models;

import main.Models.Notification;

import main.Views.NotificationType;
import main.Views.RecipientType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void testNotificationConstructor() {
        // Given
        int recipientId = 123;
        RecipientType recipientType = RecipientType.Client;
        NotificationType notificationType = NotificationType.BookAvailable;
        String message = "Test message";

        // When
        Notification notification = new Notification(recipientId, recipientType, notificationType, message);

        // Then
        assertEquals(recipientId, notification.getRecipientId());
        assertEquals(recipientType, notification.getRecipientType());
        assertEquals(notificationType, notification.getNotificationType());
        assertEquals(message, notification.getMessage());
        assertNotNull(notification.getCreatedAt()); // Created at should be set to current time
        assertFalse(notification.isRead()); // By default, isRead should be false
    }

    @Test
    void testNotificationSettersAndGetters() {
        // Given
        Notification notification = new Notification(1, RecipientType.Client, NotificationType.BookAvailable,
                "Test message");

        // When
        notification.setRecipientId(456);
        notification.setRecipientType(RecipientType.Admin);
        notification.setNotificationType(NotificationType.BorrowRequestConfirmed);
        notification.setMessage("Updated message");
        notification.setCreatedAt(LocalDateTime.of(2024, 12, 1, 10, 0));
        notification.setRead(true);

        // Then
        assertEquals(456, notification.getRecipientId());
        assertEquals(RecipientType.Admin, notification.getRecipientType());
        assertEquals(NotificationType.BorrowRequestConfirmed, notification.getNotificationType());
        assertEquals("Updated message", notification.getMessage());
        assertEquals(LocalDateTime.of(2024, 12, 1, 10, 0), notification.getCreatedAt());
        assertTrue(notification.isRead());
    }

    @Test
    void testNotificationId() {
        // Given
        Notification notification = new Notification(1, RecipientType.Client, NotificationType.BookAvailable,
                "Test message");

        // When
        notification.setNotificationId(999);

        // Then
        assertEquals(999, notification.getNotificationId());
    }

    @Test
    void testNotificationReadProperty() {
        // Given
        Notification notification = new Notification(1, RecipientType.Client, NotificationType.BookAvailable,
                "Test message");

        // When
        notification.setRead(true);

        // Then
        assertTrue(notification.isRead());
        assertTrue(notification.isReadProperty().get()); // Check the BooleanProperty value

        // When
        notification.setRead(false);

        // Then
        assertFalse(notification.isRead());
        assertFalse(notification.isReadProperty().get()); // Check the BooleanProperty value
    }

    @Test
    void testNotificationMessageProperty() {
        // Given
        Notification notification = new Notification(1, RecipientType.Client, NotificationType.BookAvailable,
                "Test message");

        // When
        notification.setMessage("New message");

        // Then
        assertEquals("New message", notification.getMessage());
        assertEquals("New message", notification.messageProperty().get()); // Check StringProperty value
    }

    @Test
    void testNotificationCreatedAtProperty() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Notification notification = new Notification(1, RecipientType.Client, NotificationType.BookAvailable,
                "Test message");

        // When
        notification.setCreatedAt(now);

        // Then
        assertEquals(now, notification.getCreatedAt());
        assertEquals(now, notification.createdAtProperty().get()); // Check ObjectProperty value
    }
}
