package main.Services;

import main.Models.BorrowTransaction;
import main.Models.DatabaseDriver;
import main.Models.Notification;
import main.Models.Model;
import main.Views.NotificationType;
import main.Views.RecipientType;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.*;

public class NotificationScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            sendReturnReminders();
        }, getInitialDelay(), 24, TimeUnit.HOURS);
    }

    private long getInitialDelay() {
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalTime now = LocalTime.now();
        long initialDelay = Duration.between(now, midnight).toMillis();
        return initialDelay < 0 ? 0 : initialDelay;
    }

    public void sendReturnReminders() {
        List<BorrowTransaction> activeTransactions = Model.getInstance().getDatabaseDriver()
                .getActiveBorrowTransactions();
        LocalDate currentDate = LocalDate.now();

        for (BorrowTransaction transaction : activeTransactions) {
            LocalDate borrowDate = transaction.getBorrow_date();

            if (borrowDate.plusDays(6).isEqual(currentDate)) {
                int recipient_id = transaction.getClient_id();
                RecipientType recipientType = RecipientType.Client;
                NotificationType notificationType = NotificationType.ReturnReminder;
                String message = "Reminder: You need to return the book you borrowed on " + borrowDate
                        + " within the next day.";
                Notification reminderNotification = new Notification(recipient_id, recipientType, notificationType,
                        message);

                Model.getInstance().insertNotification(reminderNotification);
            }
        }
    }
}
