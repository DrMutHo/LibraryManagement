package main.Models;

import main.Controllers.Client.ClientController;
import main.Controllers.Client.ClientMenuController;
import main.Controllers.Client.NotificationsController;
import main.Models.Client;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Views.NotificationType;
import main.Views.RecipientType;
import main.Views.ViewFactory;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private boolean clientLoginSuccessFlag;
    private final DatabaseDriver databaseDriver;
    private final ObservableList<Book> allBook;
    private final ObservableList<Notification> allNotifications;
    private final ObservableList<Book> recentlyAddBook;
    private final ObservableList<BookTransaction> bookTransactions;

    private final List<ModelListenerClient> listenersClient;
    private final List<ModelListenerAdmin> listenersAdmin;
    private ClientController clientController;
    private ObjectProperty<Book> selectedBook;
    private final Client client;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.clientLoginSuccessFlag = false;
        this.databaseDriver = new DatabaseDriver();
        this.allBook = FXCollections.observableArrayList();
        this.allNotifications = FXCollections.observableArrayList();
        this.listenersClient = FXCollections.observableArrayList();
        this.listenersAdmin = FXCollections.observableArrayList();
        this.recentlyAddBook = FXCollections.observableArrayList();
        this.bookTransactions = FXCollections.observableArrayList();
        selectedBook = new SimpleObjectProperty<>(null);

        this.client = new Client(0, "", "", "", "", "", null, 0, "", "");
    }

    public interface ModelListenerClient {
        void onBorrowTransactionClientCreated();
    }

    public void addListener(ModelListenerClient listener) {
        listenersClient.add(listener);
    }

    public void removeListener(ModelListenerClient listener) {
        listenersClient.remove(listener);
    }

    public void notifyBorrowTransactionClientCreated() {
        for (ModelListenerClient listener : listenersClient) {
            listener.onBorrowTransactionClientCreated();
        }
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public boolean getClientLoginSuccessFlag() {
        return this.clientLoginSuccessFlag;
    }

    public void setclientLoginSuccessFlag(boolean flag) {
        this.clientLoginSuccessFlag = flag;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    public Client getClient() {
        return client;
    }

    public void setClientController(ClientController controller) {
        this.clientController = controller;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public ObjectProperty<Book> getBookSelectionListener() {
        return selectedBook;
    }

    public void setSelectedBook(Book book) {
        selectedBook.set(book);
    }

    public void setAllBook() {
        allBook.clear();
        ResultSet resultSet = databaseDriver.getAllBookData();
        try {
            while (resultSet.next()) {
                int book_id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbn = resultSet.getString("isbn");
                String genre = resultSet.getString("genre");
                String language = resultSet.getString("language");
                String description = resultSet.getString("description");
                int publication_year = resultSet.getInt("publication_year");
                String image_path = resultSet.getString("image_path");
                Double average_rating = resultSet.getDouble("average_rating");
                int review_count = resultSet.getInt("review_count");

                int quantity = databaseDriver.countBookCopies(book_id);

                Book book = new Book(book_id, title, author, isbn, genre, language, description, publication_year,
                        image_path, average_rating, review_count, quantity);

                allBook.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRecentlyBook() {
        ResultSet resultSet = databaseDriver.getBookByClientID(Model.getInstance().getClient().getClientId());
        try {
            while (resultSet.next()) {
                int book_id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbn = resultSet.getString("isbn");
                String genre = resultSet.getString("genre");
                String language = resultSet.getString("language");
                String description = resultSet.getString("description");
                int publication_year = resultSet.getInt("publication_year");
                String image_path = resultSet.getString("image_path");
                Double average_rating = resultSet.getDouble("average_rating");
                int review_count = resultSet.getInt("review_count");

                int quantity = databaseDriver.countBookCopies(book_id);

                Book book = new Book(book_id, title, author, isbn, genre, language, description, publication_year,
                        image_path, average_rating, review_count, quantity);

                recentlyAddBook.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBookTransaction() {
        ResultSet resultSet = databaseDriver.getTransactionByClientID(Model.getInstance().getClient().getClientId());
        try {
            while (resultSet.next()) {
                int transactionId = resultSet.getInt("transaction_id");
                String title = resultSet.getString("title");
                int copyId = resultSet.getInt("copy_id");
                LocalDate borrowDate = resultSet.getDate("borrow_date").toLocalDate();
                LocalDate returnDate = resultSet.getDate("return_date") != null
                        ? resultSet.getDate("return_date").toLocalDate()
                        : null;
                String status = resultSet.getString("status");

                BookTransaction transaction = new BookTransaction(transactionId, title, copyId, borrowDate, returnDate,
                        status);
                bookTransactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<BookTransaction> getBookTransaction() {
        return bookTransactions;
    }

    public ObservableList<Book> getAllBook() {
        return allBook;
    }

    public ObservableList<Book> getRecentlyAddBook() {
        return recentlyAddBook;
    }

    public Book findBookByISBN(String ISBN) {
        for (Book book : allBook) {
            if (book.getIsbn().equals(ISBN))
                return book;
        }
        return null;
    }

    public void evaluateClientCred(String username) {
        ResultSet resultSet = databaseDriver.getClientData(username);
        try {
            if (resultSet != null && resultSet.next()) {
                this.client.setClientId(resultSet.getInt("client_id"));
                this.client.setName(resultSet.getString("name"));
                this.client.setLibraryCardNumber(resultSet.getString("library_card_number"));
                this.client.setEmail(resultSet.getString("email"));
                this.client.setPhoneNumber(resultSet.getString("phone_number"));
                this.client.setAddress(resultSet.getString("address"));
                this.client.setRegistrationDate(resultSet.getDate("registration_date"));
                this.client.setOutstandingFees(resultSet.getDouble("outstanding_fees"));
                this.client.setUsername(resultSet.getString("username"));
                this.client.setPasswordHash(resultSet.getString("password_hash"));
                this.clientLoginSuccessFlag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void prepareNotifications(ObservableList<Notification> notifications, int limit) {
        ResultSet resultSet = databaseDriver.getNotifications(this.client.getClientId(), limit);
        try {
            while (resultSet != null && resultSet.next()) {
                int notificationId = resultSet.getInt("notification_id");
                int recipientId = resultSet.getInt("recipient_id");
                String recipientTypeStr = resultSet.getString("recipient_type");
                RecipientType recipientType = RecipientType.fromString(recipientTypeStr);
                String notificationTypeStr = resultSet.getString("notification_type");
                NotificationType notificationType = NotificationType.fromString(notificationTypeStr);
                String message = resultSet.getString("message");
                Timestamp timestamp = resultSet.getTimestamp("created_at");
                LocalDateTime createdAt = timestamp.toLocalDateTime();
                boolean isRead = resultSet.getBoolean("is_read");

                Notification notification = new Notification(
                        notificationId,
                        recipientId,
                        recipientType,
                        notificationType,
                        message,
                        createdAt,
                        isRead);

                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteNotification(Notification notification) {
        databaseDriver.deleteNotification(notification.getNotificationId());
        allNotifications.remove(notification);
    }

    public void updateNotification(Notification notification) {
        databaseDriver.updateNotification(notification.getNotificationId(), true);
        notification.setRead(true);
    }

    public void insertNotification(Notification notification) {
        if (databaseDriver.insertNotification(notification)) {
            Platform.runLater(() -> {
                allNotifications.add(notification);
            });
        }
    }

    public void markAllNotificationsAsRead(int recipientId) {
        databaseDriver.markAllNotificationsAsRead(recipientId);

        for (Notification notification : allNotifications) {
            notification.setRead(true);
        }
    }

    public void setAllNotifications() {
        prepareNotifications(this.allNotifications, -1);
    }

    public ObservableList<Notification> getAllNotifications() {
        return allNotifications;
    }

    public void notifyBorrowTransactionClientCreatedEvent() {
        notifyBorrowTransactionClientCreated();
    }

    // Admin section //

    public interface ModelListenerAdmin {
        void onBorrowTransactionAdminCreated();

        void onBookReturnProcessed();
    }

    public void notifyBookReturnProcessed() {
        for (ModelListenerAdmin listener : listenersAdmin) {
            listener.onBookReturnProcessed();
        }
    }

    public void notifyBorrowTransactionAdminCreated() {
        for (ModelListenerAdmin listener : listenersAdmin) {
            listener.onBorrowTransactionAdminCreated();
        }
    }

    public void notifyBorrowTransactionAdminCreatedEvent() {
        notifyBorrowTransactionAdminCreated();
    }

}
