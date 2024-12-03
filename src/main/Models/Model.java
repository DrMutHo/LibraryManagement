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

/**
 * Represents the central model of the library system, managing the data and interactions 
 * between various components such as books, clients, transactions, and notifications.
 * The model acts as a singleton and facilitates communication between the client and 
 * admin components.
 */
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

    /**
     * Initializes a new instance of the Model class, setting up various fields including lists for books, 
     * transactions, notifications, and listeners.
     */
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

    /**
     * Interface for client-related listeners. Defines the method to be called 
     * when a borrow transaction is created for the client.
     */
    public interface ModelListenerClient {
        /**
         * This method is called when a borrow transaction is created for the client.
         */
        void onBorrowTransactionClientCreated();
    }

    /**
     * Adds a listener to observe client-related events.
     * 
     * @param listener The listener to be added.
     */
    public void addListener(ModelListenerClient listener) {
        listenersClient.add(listener);
    }

    /**
     * Removes a listener from observing client-related events.
     * 
     * @param listener The listener to be removed.
     */
    public void removeListener(ModelListenerClient listener) {
        listenersClient.remove(listener);
    }

    /**
     * Notifies all listeners that a borrow transaction has been created for the client.
     * This method is called whenever a new borrow transaction is created, and it triggers
     * the notification to each registered listener.
     */
    public void notifyBorrowTransactionClientCreated() {
        for (ModelListenerClient listener : listenersClient) {
            listener.onBorrowTransactionClientCreated();
        }
    }

    /**
     * Returns the singleton instance of the Model class, creating it if necessary.
     * This method ensures that only one instance of the Model class exists throughout
     * the application's lifecycle, providing a global point of access.
     * 
     * @return The singleton instance of the Model class.
     */
    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }


    /**
     * Returns the ViewFactory associated with this model instance.
     * 
     * @return The ViewFactory for the model.
     */
    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    /**
     * Returns whether the client login was successful.
     * 
     * @return A boolean indicating whether the client login was successful.
     */
    public boolean getClientLoginSuccessFlag() {
        return this.clientLoginSuccessFlag;
    }

    /**
     * Sets the client login success flag.
     * 
     * @param flag A boolean value to set the client login success flag.
     */
    public void setClientLoginSuccessFlag(boolean flag) {
        this.clientLoginSuccessFlag = flag;
    }

    /**
     * Returns whether the admin login was successful.
     * 
     * @return A boolean indicating whether the admin login was successful.
     */
    public boolean getAdminLoginSuccessFlag() {
        return this.clientLoginSuccessFlag;
    }

    /**
     * Sets the admin login success flag.
     * 
     * @param flag A boolean value to set the admin login success flag.
     */
    public void setAdminLoginSuccessFlag(boolean flag) {
        this.clientLoginSuccessFlag = flag;
    }

    /**
     * Returns the DatabaseDriver instance associated with this model.
     * 
     * @return The DatabaseDriver for the model.
     */
    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    /**
     * Returns the Client instance associated with this model.
     * 
     * @return The Client for the model.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sets the client controller associated with this model.
     * 
     * @param controller The ClientController to be set.
     */
    public void setClientController(ClientController controller) {
        this.clientController = controller;
    }

    /**
     * Returns the client controller associated with this model.
     * 
     * @return The ClientController for the model.
     */
    public ClientController getClientController() {
        return clientController;
    }

    /**
     * Returns the listener for book selection changes.
     * 
     * @return An ObjectProperty for the selected book.
     */
    public ObjectProperty<Book> getBookSelectionListener() {
        return selectedBook;
    }

    /**
     * Sets the currently selected book.
     * 
     * @param book The book to set as selected.
     */
    public void setSelectedBook(Book book) {
        selectedBook.set(book);
    }

    /**
     * Returns the Admin instance associated with this model.
     * 
     * @return The Admin for the model.
     */
    public Admin getAdmin() {
        return admin;
    }

    /**
     * Sets the list of all books available in the library.
     * This method retrieves the book data from the database and updates 
     * the list of all books. Each book's information is fetched from the 
     * result set and added to the `allBook` observable list. It also counts 
     * the number of available copies of each book.
     * 
     * @throws SQLException If there is an error accessing the database.
     */
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

                Book book = new Book(book_id, title, author, isbn, genre, language, description, publication_year, image_path, average_rating, review_count, quantity);
                allBook.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * Sets the list of highest rated books, optionally filtered by genre.
     * This method retrieves the highest rated books from the database, either 
     * for all genres or filtered by a specific genre. The resulting books are 
     * added to the `HighestRatedBooks` observable list. 
     * 
     * @param genre The genre to filter the books by. If the value is "ALL", 
     *              the method fetches the highest rated books across all genres.
     * @throws SQLException If there is an error accessing the database.
     */
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
                // Retrieve book details from the ResultSet
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

                // Create a Book object and add it to the list
                Book book = new Book(book_id, title, author, isbn, genreResult, language, description, publication_year,
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


    /**
     * Exports the borrow transactions of the currently logged-in client to an Excel file.
     * This method retrieves the borrow transaction history of the client from the database
     * and exports the data to an Excel file using the Apache POI library.
     * The generated Excel file contains the transaction details such as transaction ID,
     * client ID, copy ID, borrow date, return date, and transaction status.
     * 
     * @param filePath The path where the Excel file should be saved. This should include
     *                 the full file name and extension (e.g., "C:/path/to/file.xlsx").
     */
    public void exportClientBorrowTransactionsToExcel(String filePath) {
        try {
            // Retrieve borrow transaction history of the currently logged-in client
            ResultSet resultSet = databaseDriver
                    .getTransactionByClientID(Model.getInstance().getClient().getClientId());

            // Create a new Excel workbook and sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Transactions");

            // Create header row with column names
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Transaction ID");
            headerRow.createCell(1).setCellValue("Copy ID");
            headerRow.createCell(2).setCellValue("Borrow Date");
            headerRow.createCell(3).setCellValue("Return Date");
            headerRow.createCell(4).setCellValue("Status");

            // Iterate through the result set and populate rows with transaction data
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

            // Write the workbook to the file system
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            // Close workbook and result set
            workbook.close();
            resultSet.close();
            System.out.println("Excel file generated successfully: " + filePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Exports all borrow transactions to an Excel file.
     * This method retrieves all borrow transactions from the database and exports the data 
     * to an Excel file using the Apache POI library. The generated Excel file contains 
     * transaction details such as transaction ID, client ID, copy ID, borrow date, 
     * return date, and transaction status.
     * 
     * @param filePath The path where the Excel file should be saved. This should include
     *                 the full file name and extension (e.g., "C:/path/to/file.xlsx").
     */
    public void exportBorrowTransactionsToExcel(String filePath) {
        try {
            // Retrieve all borrow transactions from the database
            ResultSet resultSet = databaseDriver.getAllBorrowTransactions();

            // Create a new Excel workbook and sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Transactions");

            // Create header row with column names
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Transaction ID");
            headerRow.createCell(1).setCellValue("Client ID");
            headerRow.createCell(2).setCellValue("Copy ID");
            headerRow.createCell(3).setCellValue("Borrow Date");
            headerRow.createCell(4).setCellValue("Return Date");
            headerRow.createCell(5).setCellValue("Status");

            // Iterate through the result set and populate rows with transaction data
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

            // Write the workbook to the file system
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            // Close workbook and result set
            workbook.close();
            resultSet.close();
            System.out.println("Excel file generated successfully: " + filePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Sets the list of borrow transactions for the currently logged-in client.
     * This method retrieves the borrow transactions for the client from the database 
     * and adds them to the `BorrowTransactions` list. Each transaction includes 
     * details such as the transaction ID, client ID, book title, copy ID, borrow date, 
     * return date, and transaction status.
     * 
     * @see BorrowTransaction
     */
    public void setBorrowTransaction() {
        // Retrieve the borrow transactions for the current client
        ResultSet resultSet = databaseDriver.getTransactionByClientID(Model.getInstance().getClient().getClientId());
        
        try {
            // Loop through each result and create a BorrowTransaction object
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

                // Create a new BorrowTransaction object and add it to the list
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


    /**
     * Retrieves book data by the specified copy ID.
     * This method queries the database for book details based on the provided copy ID and returns the corresponding book data.
     * The book data includes information such as the book ID, title, author, ISBN, genre, language, description, 
     * publication year, image path, average rating, review count, and the quantity of available copies.
     *
     * @param copy_id The copy ID of the book whose details are to be retrieved.
     * @return A {@link Book} object containing the details of the book associated with the provided copy ID. 
     *         If no book is found, an empty Book object is returned.
     */
    public Book getBookDataByCopyID(int copy_id) {
        ResultSet resultSet = databaseDriver.getBookDataByCopyID(copy_id);
        Book res = new Book();
        
        try {
            // Retrieve book details from the result set based on the copy ID
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

                // Get the quantity of available copies for this book
                int quantity = databaseDriver.countBookCopies(book_id);

                // Create a Book object with the retrieved data
                Book book = new Book(book_id, title, author, isbn, genre, language, description, publication_year,
                        image_path, average_rating, review_count, quantity);
                res = book;  // Set the result as the current book
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Return the book data (or an empty Book object if not found)
        return res;
    }


    /**
     * Retrieves the book currently being read by the logged-in client.
     * If the client is not currently reading any book, it defaults to returning the highest-rated book in the database.
     * 
     * This method first tries to retrieve the book that the client is currently reading by querying the database.
     * If the client is not reading any book, it then queries the database for the highest-rated book.
     * 
     * @return A {@link Book} object representing the book that the client is currently reading, or the highest-rated
     *         book if the client isn't reading any book. If no book is found, an empty {@link Book} object is returned.
     */
    public Book getReadingBook() {
        // Attempt to retrieve the book currently being read by the client
        ResultSet resultSet = databaseDriver.get1BookDataByCopyID(Model.getInstance().getClient().getClientId());
        Book res = new Book();
        
        // If no book is being read, fallback to retrieving the highest-rated book
        if (resultSet == null) {
            resultSet = databaseDriver.getHighestRatingBook();
        }
        
        try {
            while (resultSet.next()) {
                // Extract book details from the result set
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

                // Count the available copies of the book
                int quantity = databaseDriver.countBookCopies(book_id);

                // Create a new Book object and assign it to the result
                Book book = new Book(book_id, title, author, isbn, genre, language, description, publication_year,
                        image_path, average_rating, review_count, quantity);
                res = book;  // Set the result book
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Return the retrieved book or an empty book if no result
        return res;
    }

    /**
     * Returns the list of borrow transactions for the currently logged-in client.
     * 
     * This method retrieves the list of borrow transactions that the client has made. It returns an 
     * observable list of {@link BorrowTransaction} objects.
     * 
     * @return An observable list of {@link BorrowTransaction} objects representing the borrow transactions.
     */
    public ObservableList<BorrowTransaction> getBorrowTransaction() {
        return BorrowTransactions;
    }

    /**
     * Returns the list of all books available in the library.
     * 
     * This method retrieves the complete list of books stored in the library system. It returns an 
     * observable list of {@link Book} objects representing the books in the library.
     * 
     * @return An observable list of {@link Book} objects representing all available books.
     */
    public ObservableList<Book> getAllBook() {
        return allBook;
    }

    /**
     * Returns the list of highest-rated books in the library.
     * 
     * This method retrieves the list of books that have the highest ratings, optionally filtered by genre.
     * It returns an observable list of {@link Book} objects representing the highest-rated books.
     * 
     * @return An observable list of {@link Book} objects representing the highest-rated books.
     */
    public ObservableList<Book> getHighestRatedBook() {
        return HighestRatedBooks;
    }

    /**
     * Finds a book by its ISBN from the list of all books.
     * 
     * This method searches through the list of all books and returns the first book that matches the given ISBN.
     * If no book is found with the specified ISBN, it returns {@code null}.
     * 
     * @param ISBN The ISBN of the book to search for.
     * @return The {@link Book} object that matches the provided ISBN, or {@code null} if no such book is found.
     */
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
