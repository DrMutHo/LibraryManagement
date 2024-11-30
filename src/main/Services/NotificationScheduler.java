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
            sendOverdueAlerts();
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
            LocalDate borrowDate = transaction.getBorrowDate();

            int copyId = transaction.getCopyId();
            int bookId = Model.getInstance().getDatabaseDriver().getBookIdByCopyId(copyId);
            String bookTitle = Model.getInstance().getDatabaseDriver().getBookTitleById(bookId);

            if (borrowDate.plusDays(6).isEqual(currentDate)) {
                int recipient_id = transaction.getClientId();
                RecipientType recipientType = RecipientType.Client;
                NotificationType notificationType = NotificationType.ReturnReminder;
                String message = "Reminder: You need to return the book '" + bookTitle + "' that you borrowed on "
                        + borrowDate + " within the next day.";
                Notification reminderNotification = new Notification(recipient_id, recipientType, notificationType,
                        message);

                Model.getInstance().insertNotification(reminderNotification);
            }
        }
    }

    public void sendOverdueAlerts() {
        List<BorrowTransaction> activeTransactions = Model.getInstance().getDatabaseDriver()
                .getActiveBorrowTransactions();
        LocalDate currentDate = LocalDate.now();

        for (BorrowTransaction transaction : activeTransactions) {
            LocalDate borrowDate = transaction.getBorrowDate();

            int copyId = transaction.getCopyId();
            int bookId = Model.getInstance().getDatabaseDriver().getBookIdByCopyId(copyId);
            String bookTitle = Model.getInstance().getDatabaseDriver().getBookTitleById(bookId);

            if (borrowDate.plusDays(7).isBefore(currentDate)) {
                long overdueDays = Duration.between(borrowDate.plusDays(7).atStartOfDay(), currentDate.atStartOfDay())
                        .toDays();
                int recipient_id = transaction.getClientId();
                RecipientType recipientType = RecipientType.Client;
                NotificationType notificationType = NotificationType.OverdueAlert;

                double overdueFee = overdueDays * 2.0;

                String message = "You are " + overdueDays + " days overdue for the book '" + bookTitle
                        + "'. Your outstanding fee is $"
                        + overdueFee + ".";
                Notification overdueNotification = new Notification(recipient_id, recipientType, notificationType,
                        message);

                Model.getInstance().insertNotification(overdueNotification);
            }
        }
    }
}
