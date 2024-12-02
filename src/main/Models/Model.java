package main.Models;

import main.Controllers.Client.ClientController;
import main.Controllers.Client.ClientMenuController;
import main.Controllers.Client.NotificationsController;
import main.Models.Client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Views.AccountType;
import main.Views.NotificationType;
import main.Views.RecipientType;
import main.Views.ViewFactory;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private boolean clientLoginSuccessFlag;
    private boolean adminLoginSuccessFlag;
    private final DatabaseDriver databaseDriver;
    private final ObservableList<Book> allBook;
    private final ObservableList<Book> HighestRatedBooks;
    private final ObservableList<BorrowTransaction> BorrowTransactions;
    private final ObservableList<Notification> allNotifications;
    private final ObservableList<Book> recentlyAddBook;

    private final List<ModelListenerClient> listenersClient;
    private final List<ModelListenerAdmin> listenersAdmin;
    private ClientController clientController;
    private ObjectProperty<Book> selectedBook;
    private final Client client;
    private final Admin admin;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.clientLoginSuccessFlag = false;
        this.adminLoginSuccessFlag = false;
        this.databaseDriver = new DatabaseDriver();
        this.allBook = FXCollections.observableArrayList();
        this.HighestRatedBooks = FXCollections.observableArrayList();
        this.allNotifications = FXCollections.observableArrayList();
        this.listenersClient = FXCollections.observableArrayList();
        this.listenersAdmin = FXCollections.observableArrayList();
        this.recentlyAddBook = FXCollections.observableArrayList();
        selectedBook = new SimpleObjectProperty<>(null);
        this.BorrowTransactions = FXCollections.observableArrayList();

        this.client = new Client(0, "", "", "", "", "", null, 0, "", "", "");
        this.admin = new Admin(0, "", "", "");
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

    public void setClientLoginSuccessFlag(boolean flag) {
        this.clientLoginSuccessFlag = flag;
    }

    public boolean getAdminLoginSuccessFlag() {
        return this.clientLoginSuccessFlag;
    }

    public void setAdminLoginSuccessFlag(boolean flag) {
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

    public Admin getAdmin() {
        return admin;
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

    public void setHighestRatedBooks(String genre) {
        HighestRatedBooks.clear();

        ResultSet resultSet;
        if (genre.equalsIgnoreCase("ALL")) {
            resultSet = databaseDriver.getHighestRatingBooks();
        } else {
            resultSet = databaseDriver.getHighestRatingBooksByGenre(genre);
        }

        try {
            while (resultSet.next()) {
                // Lấy thông tin từ ResultSet
                int book_id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbn = resultSet.getString("isbn");
                String genreResult = resultSet.getString("genre");
                String language = resultSet.getString("language");
                String description = resultSet.getString("description");
                int publication_year = resultSet.getInt("publication_year");
                String image_path = resultSet.getString("image_path");
                Double average_rating = resultSet.getDouble("average_rating");
                int review_count = resultSet.getInt("review_count");

                int quantity = databaseDriver.countBookCopies(book_id);

                Book book = new Book(book_id, title, author, isbn, genre, language, description, publication_year,
                        image_path, average_rating, review_count, quantity);

                HighestRatedBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void exportClientBorrowTransactionsToExcel(String filePath) {
        try {
            ResultSet resultSet = databaseDriver
                    .getTransactionByClientID(Model.getInstance().getClient().getClientId());

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Transactions");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Transaction ID");
            headerRow.createCell(1).setCellValue("Copy ID");
            headerRow.createCell(2).setCellValue("Borrow Date");
            headerRow.createCell(3).setCellValue("Return Date");
            headerRow.createCell(4).setCellValue("Status");

            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getInt("transaction_id"));
                row.createCell(1).setCellValue(resultSet.getInt("copy_id"));
                row.createCell(2).setCellValue(resultSet.getDate("borrow_date").toString());
                row.createCell(3).setCellValue(
                        resultSet.getDate("return_date") != null ? resultSet.getDate("return_date").toString() : "");
                row.createCell(4).setCellValue(resultSet.getString("status"));
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            workbook.close();
            resultSet.close();
            System.out.println("Excel file generated successfully: " + filePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void exportBorrowTransactionsToExcel(String filePath) {
        try {
            ResultSet resultSet = databaseDriver.getAllBorrowTransactions();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Transactions");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Transaction ID");
            headerRow.createCell(1).setCellValue("Client ID");
            headerRow.createCell(2).setCellValue("Copy ID");
            headerRow.createCell(3).setCellValue("Borrow Date");
            headerRow.createCell(4).setCellValue("Return Date");
            headerRow.createCell(5).setCellValue("Status");

            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getInt("transaction_id"));
                row.createCell(1).setCellValue(resultSet.getInt("client_id"));
                row.createCell(2).setCellValue(resultSet.getInt("copy_id"));
                row.createCell(3).setCellValue(resultSet.getDate("borrow_date").toString());
                row.createCell(4).setCellValue(
                        resultSet.getDate("return_date") != null ? resultSet.getDate("return_date").toString() : "");
                row.createCell(5).setCellValue(resultSet.getString("status"));
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            workbook.close();
            resultSet.close();
            System.out.println("Excel file generated successfully: " + filePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setBorrowTransaction() {
        ResultSet resultSet = databaseDriver.getTransactionByClientID(Model.getInstance().getClient().getClientId());
        try {
            while (resultSet.next()) {
                int transactionId = resultSet.getInt("transaction_id");
                int clientId = Model.getInstance().getClient().getClientId();
                String title = resultSet.getString("title");
                int copyId = resultSet.getInt("copy_id");
                LocalDate borrowDate = resultSet.getDate("borrow_date").toLocalDate();
                LocalDate returnDate = resultSet.getDate("return_date") != null
                        ? resultSet.getDate("return_date").toLocalDate()
                        : null;
                String status = resultSet.getString("status");

                BorrowTransaction transaction = new BorrowTransaction(transactionId, clientId, title, copyId,
                        borrowDate,
                        returnDate,
                        status);
                BorrowTransactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Book getBookDataByCopyID(int copy_id) {
        ResultSet resultSet = databaseDriver.getBookDataByCopyID(copy_id);
        Book res = new Book();
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
                res = book;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Book getReadingBook() {
        ResultSet resultSet = databaseDriver.get1BookDataByCopyID(Model.getInstance().getClient().getClientId());
        Book res = new Book();
        if (resultSet == null) {
            resultSet = databaseDriver.getHighestRatingBook();
        }
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
                res = book;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public ObservableList<BorrowTransaction> getBorrowTransaction() {
        return BorrowTransactions;
    }

    public ObservableList<Book> getAllBook() {
        return allBook;
    }

    public ObservableList<Book> getHighestRatedBook() {
        return HighestRatedBooks;
    }

    public Book findBookByISBN(String ISBN) {
        for (Book book : allBook) {
            if (book.getIsbn().equals(ISBN))
                return book;
        }
        return null;
    }

    public void evaluateAdminCred(String username) {
        ResultSet resultSet = databaseDriver.getAdminData(username);
        try {
            if (resultSet != null && resultSet.next()) {
                this.admin.setadmin_id(resultSet.getInt("admin_id"));
                this.admin.setUsername(resultSet.getString("username"));
                this.admin.setPassword_hash(resultSet.getString("password_hash"));
                this.admin.setEmail(resultSet.getString("email"));
                this.adminLoginSuccessFlag = true;
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
                this.client.setAvatarImagePath(resultSet.getString("avatar_image_path"));
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
        ResultSet resultSet = (viewFactory.getLoginAccountType().equals(AccountType.CLIENT))
                ? databaseDriver.getNotifications(this.client.getClientId(), "Client", limit)
                : databaseDriver.getNotifications(this.client.getClientId(), "Admin", limit);
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

    public void sendNotification(Notification notification) {
        databaseDriver.insertNotification(notification);
    }

    public void markAllNotificationsAsRead(int recipientId) {
        if (viewFactory.getLoginAccountType().equals(AccountType.CLIENT))
            databaseDriver.markAllNotificationsAsRead(recipientId, "Client");
        else
            databaseDriver.markAllNotificationsAsRead(recipientId, "Admin");
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

    public void setClientAvatar(String fileURI) {
        databaseDriver.setClientAvatar(Model.getInstance().getClient().getClientId(), fileURI);
    }

}
