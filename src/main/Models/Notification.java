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

    public int getNotificationId() {
        return notificationId.get();
    }

    public void setNotificationId(int value) {
        notificationId.set(value);
    }

    public IntegerProperty notificationIdProperty() {
        return notificationId;
    }

    public int getRecipientId() {
        return recipientId.get();
    }

    public void setRecipientId(int value) {
        recipientId.set(value);
    }

    public IntegerProperty recipientIdProperty() {
        return recipientId;
    }

    public RecipientType getRecipientType() {
        return recipientType.get();
    }

    public void setRecipientType(RecipientType value) {
        recipientType.set(value);
    }

    public ObjectProperty<RecipientType> recipientTypeProperty() {
        return recipientType;
    }

    public NotificationType getNotificationType() {
        return notificationType.get();
    }

    public void setNotificationType(NotificationType value) {
        notificationType.set(value);
    }

    public ObjectProperty<NotificationType> notificationTypeProperty() {
        return notificationType;
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String value) {
        message.set(value);
    }

    public StringProperty messageProperty() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(LocalDateTime value) {
        createdAt.set(value);
    }

    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    public boolean isRead() {
        return isRead.get();
    }

    public void setRead(boolean value) {
        isRead.set(value);
    }

    public BooleanProperty isReadProperty() {
        return isRead;
    }
}
