package main.Models;

import main.Views.NotificationType;
import main.Views.RecipientType;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Notification {
    private final IntegerProperty notificationId;
    private final IntegerProperty recipientId;
    private final ObjectProperty<RecipientType> recipientType;
    private final ObjectProperty<NotificationType> notificationType;
    private final StringProperty message;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final BooleanProperty isRead;

    /**
     * Constructs a new Notification object with the specified details.
     * 
     * This constructor initializes a Notification with the recipient's ID,
     * recipient type, notification type,
     * a message, the current timestamp, and sets the read status to false by
     * default.
     * 
     * @param recipientId      The ID of the recipient of the notification.
     * @param recipientType    The type of the recipient (e.g., client, admin,
     *                         etc.).
     * @param notificationType The type of the notification (e.g., alert, reminder,
     *                         etc.).
     * @param message          The message content of the notification.
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
     * Constructs a new Notification object with the specified details.
     * 
     * This constructor initializes a Notification with the provided notification
     * ID, recipient's ID, recipient type,
     * notification type, message, timestamp, and read status.
     * 
     * @param notificationId   The unique ID of the notification.
     * @param recipientId      The ID of the recipient of the notification.
     * @param recipientType    The type of the recipient (e.g., client, admin,
     *                         etc.).
     * @param notificationType The type of the notification (e.g., alert, reminder,
     *                         etc.).
     * @param message          The message content of the notification.
     * @param createdAt        The timestamp when the notification was created.
     * @param isRead           The read status of the notification (true if read,
     *                         false if unread).
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
     * Retrieves the unique ID of the notification.
     * 
     * @return The unique ID of the notification.
     */
    public int getNotificationId() {
        return notificationId.get();
    }

    /**
     * Sets the unique ID for the notification.
     * 
     * @param value The unique ID to set for the notification.
     */
    public void setNotificationId(int value) {
        notificationId.set(value);
    }

    /**
     * Gets the property for the notification's unique ID.
     * This allows for binding to the notificationId property in a UI context.
     * 
     * @return The IntegerProperty representing the notification's unique ID.
     */
    public IntegerProperty notificationIdProperty() {
        return notificationId;
    }

    /**
     * Gets the recipient's ID associated with the notification.
     * 
     * @return The recipient's ID.
     */
    public int getRecipientId() {
        return recipientId.get();
    }

    /**
     * Sets the recipient's ID associated with the notification.
     * 
     * @param value The recipient's ID to set.
     */
    public void setRecipientId(int value) {
        recipientId.set(value);
    }

    /**
     * Gets the property for the recipient's ID associated with the notification.
     * This allows for binding to the recipientId property in a UI context.
     * 
     * @return The IntegerProperty representing the recipient's ID.
     */
    public IntegerProperty recipientIdProperty() {
        return recipientId;
    }

    /**
     * Gets the recipient type associated with the notification.
     * 
     * @return The recipient type.
     */
    public RecipientType getRecipientType() {
        return recipientType.get();
    }

    /**
     * Sets the recipient type associated with the notification.
     * 
     * @param value The recipient type to set.
     */
    public void setRecipientType(RecipientType value) {
        recipientType.set(value);
    }

    /**
     * Gets the property for the recipient type associated with the notification.
     * This allows for binding to the recipientType property in a UI context.
     * 
     * @return The ObjectProperty representing the recipient type.
     */
    public ObjectProperty<RecipientType> recipientTypeProperty() {
        return recipientType;
    }

    /**
     * Gets the notification type associated with the notification.
     * 
     * @return The notification type.
     */
    public NotificationType getNotificationType() {
        return notificationType.get();
    }

    /**
     * Sets the notification type associated with the notification.
     * 
     * @param value The notification type to set.
     */
    public void setNotificationType(NotificationType value) {
        notificationType.set(value);
    }

    /**
     * Gets the property for the notification type associated with the notification.
     * This allows for binding to the notificationType property in a UI context.
     * 
     * @return The ObjectProperty representing the notification type.
     */
    public ObjectProperty<NotificationType> notificationTypeProperty() {
        return notificationType;
    }

    /**
     * Gets the message associated with the notification.
     * 
     * @return The message content.
     */
    public String getMessage() {
        return message.get();
    }

    /**
     * Sets the message associated with the notification.
     * 
     * @param value The message content to set.
     */
    public void setMessage(String value) {
        message.set(value);
    }

    /**
     * Gets the property for the message associated with the notification.
     * This allows for binding to the message property in a UI context.
     * 
     * @return The StringProperty representing the message.
     */
    public StringProperty messageProperty() {
        return message;
    }

    /**
     * Gets the creation timestamp of the notification.
     * 
     * @return The creation timestamp of the notification.
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
     * Gets the property for the creation timestamp of the notification.
     * This allows for binding to the createdAt property in a UI context.
     * 
     * @return The ObjectProperty representing the creation timestamp.
     */
    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    /**
     * Checks whether the notification has been read.
     * 
     * @return True if the notification has been read, otherwise false.
     */
    public boolean isRead() {
        return isRead.get();
    }

    /**
     * Sets whether the notification has been read.
     * 
     * @param value True to mark the notification as read, false to mark it as
     *              unread.
     */
    public void setRead(boolean value) {
        isRead.set(value);
    }

    /**
     * Gets the property for the read status of the notification.
     * This allows for binding to the isRead property in a UI context.
     * 
     * @return The BooleanProperty representing the read status.
     */
    public BooleanProperty isReadProperty() {
        return isRead;
    }

}
