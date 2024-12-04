package main.Models;

import main.Views.NotificationType;
import main.Views.RecipientType;
import javafx.beans.property.*;

import java.time.LocalDateTime;

/**
 * The {@code Notification} class represents a notification within the system.
 * <p>
 * It encapsulates details such as the notification ID, recipient information,
 * notification type, message content, creation timestamp, and read status.
 * This class utilizes JavaFX properties to allow for property bindings in the UI.
 * </p>
 */
public class Notification {
    private final IntegerProperty notificationId;
    private final IntegerProperty recipientId;
    private final ObjectProperty<RecipientType> recipientType;
    private final ObjectProperty<NotificationType> notificationType;
    private final StringProperty message;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final BooleanProperty isRead;

    /**
     * Constructs a new {@code Notification} with the specified recipient details and message.
     * <p>
     * The {@code createdAt} timestamp is set to the current date and time, and the notification
     * is marked as unread by default.
     * </p>
     *
     * @param recipientId       The ID of the recipient.
     * @param recipientType     The type of the recipient (e.g., Client, Admin).
     * @param notificationType  The type of the notification (e.g., Info, Alert).
     * @param message           The message content of the notification.
     */
    public Notification(int recipientId, RecipientType recipientType, NotificationType notificationType,
                        String message) {
        this.notificationId = new SimpleIntegerProperty();
        this.recipientId = new SimpleIntegerProperty(recipientId);
        this.recipientType = new SimpleObjectProperty<>(recipientType);
        this.notificationType = new SimpleObjectProperty<>(notificationType);
        this.message = new SimpleStringProperty(message);
        this.createdAt = new SimpleObjectProperty<>(LocalDateTime.now());
        this.isRead = new SimpleBooleanProperty(false);
    }

    /**
     * Constructs a new {@code Notification} with all fields specified.
     *
     * @param notificationId    The unique ID of the notification.
     * @param recipientId       The ID of the recipient.
     * @param recipientType     The type of the recipient (e.g., Client, Admin).
     * @param notificationType  The type of the notification (e.g., Info, Alert).
     * @param message           The message content of the notification.
     * @param createdAt         The timestamp when the notification was created.
     * @param isRead            The read status of the notification.
     */
    public Notification(int notificationId, int recipientId, RecipientType recipientType,
                        NotificationType notificationType, String message, LocalDateTime createdAt, boolean isRead) {
        this.notificationId = new SimpleIntegerProperty(notificationId);
        this.recipientId = new SimpleIntegerProperty(recipientId);
        this.recipientType = new SimpleObjectProperty<>(recipientType);
        this.notificationType = new SimpleObjectProperty<>(notificationType);
        this.message = new SimpleStringProperty(message);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.isRead = new SimpleBooleanProperty(isRead);
    }

    /**
     * Retrieves the notification ID.
     *
     * @return The notification ID.
     */
    public int getNotificationId() {
        return notificationId.get();
    }

    /**
     * Sets the notification ID.
     *
     * @param value The notification ID to set.
     */
    public void setNotificationId(int value) {
        notificationId.set(value);
    }

    /**
     * Gets the {@code IntegerProperty} for the notification ID.
     *
     * @return The notification ID property.
     */
    public IntegerProperty notificationIdProperty() {
        return notificationId;
    }

    /**
     * Retrieves the recipient ID.
     *
     * @return The recipient ID.
     */
    public int getRecipientId() {
        return recipientId.get();
    }

    /**
     * Sets the recipient ID.
     *
     * @param value The recipient ID to set.
     */
    public void setRecipientId(int value) {
        recipientId.set(value);
    }

    /**
     * Gets the {@code IntegerProperty} for the recipient ID.
     *
     * @return The recipient ID property.
     */
    public IntegerProperty recipientIdProperty() {
        return recipientId;
    }

    /**
     * Retrieves the recipient type.
     *
     * @return The recipient type.
     */
    public RecipientType getRecipientType() {
        return recipientType.get();
    }

    /**
     * Sets the recipient type.
     *
     * @param value The recipient type to set.
     */
    public void setRecipientType(RecipientType value) {
        recipientType.set(value);
    }

    /**
     * Gets the {@code ObjectProperty} for the recipient type.
     *
     * @return The recipient type property.
     */
    public ObjectProperty<RecipientType> recipientTypeProperty() {
        return recipientType;
    }

    /**
     * Retrieves the notification type.
     *
     * @return The notification type.
     */
    public NotificationType getNotificationType() {
        return notificationType.get();
    }

    /**
     * Sets the notification type.
     *
     * @param value The notification type to set.
     */
    public void setNotificationType(NotificationType value) {
        notificationType.set(value);
    }

    /**
     * Gets the {@code ObjectProperty} for the notification type.
     *
     * @return The notification type property.
     */
    public ObjectProperty<NotificationType> notificationTypeProperty() {
        return notificationType;
    }

    /**
     * Retrieves the message content of the notification.
     *
     * @return The notification message.
     */
    public String getMessage() {
        return message.get();
    }

    /**
     * Sets the message content of the notification.
     *
     * @param value The message to set.
     */
    public void setMessage(String value) {
        message.set(value);
    }

    /**
     * Gets the {@code StringProperty} for the message.
     *
     * @return The message property.
     */
    public StringProperty messageProperty() {
        return message;
    }

    /**
     * Retrieves the creation timestamp of the notification.
     *
     * @return The creation timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    /**
     * Sets the creation timestamp of the notification.
     *
     * @param value The creation timestamp to set.
     */
    public void setCreatedAt(LocalDateTime value) {
        createdAt.set(value);
    }

    /**
     * Gets the {@code ObjectProperty} for the creation timestamp.
     *
     * @return The creation timestamp property.
     */
    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    /**
     * Checks whether the notification has been read.
     *
     * @return {@code true} if the notification is read; {@code false} otherwise.
     */
    public boolean isRead() {
        return isRead.get();
    }

    /**
     * Marks the notification as read or unread.
     *
     * @param value {@code true} to mark as read; {@code false} to mark as unread.
     */
    public void setRead(boolean value) {
        isRead.set(value);
    }

    /**
     * Gets the {@code BooleanProperty} for the read status.
     *
     * @return The read status property.
     */
    public BooleanProperty isReadProperty() {
        return isRead;
    }
}
