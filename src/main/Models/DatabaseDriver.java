package main.Models;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Properties;

import org.mindrot.jbcrypt.BCrypt;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.crypto.Data;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import main.Views.NotificationType;
import main.Views.RecipientType;

/**
 * The DatabaseDriver class handles the configuration and management of the
 * database connection pool using HikariCP. It provides utility methods for
 * retrieving and closing database connections.
 */
public class DatabaseDriver {

    /** DataSource instance managed by HikariCP */
    private HikariDataSource dataSource;

    /**
     * Gets the current DataSource instance.
     * 
     * @return the DataSource instance managed by HikariCP
     */
    public HikariDataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Sets the DataSource instance.
     * 
     * @param dataSource the HikariDataSource to set
     */
    public void setDataSource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Constructs a DatabaseDriver instance and initializes the HikariCP DataSource.
     * Loads database configuration from environment variables using Dotenv.
     */
    public DatabaseDriver() {
        try {
            // Load environment variables
            Dotenv dotenv = Dotenv.load();
            String url = "jdbc:mysql://localhost:3306/library_management";
            String username = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(3000); // Maximum connections in the pool
            config.setConnectionTimeout(60000); // Connection timeout (60 seconds)
            config.setIdleTimeout(600000); // Idle connection timeout (10 minutes)
            config.setMaxLifetime(1800000); // Maximum connection lifetime (30 minutes)

            // Create DataSource
            this.dataSource = new HikariDataSource(config);
            System.out.println("Connect to database successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a connection from the HikariCP DataSource.
     * 
     * @return a Connection object from the connection pool
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Closes the HikariCP DataSource and releases all resources.
     * Should be called during application shutdown to clean up resources.
     */
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    /**
     * 
     * @return
     */
    public ResultSet getAllAdminIDs() {
        ResultSet resultSet = null;
        String query = "SELECT admin_id FROM Admin";
        try {
            Connection connection = this.dataSource.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Retrieves book data based on the copy ID.
     * 
     * @param copy_id the ID of the book copy to retrieve data for
     * @return a ResultSet containing the book data
     */
    public ResultSet getBookDataByCopyID(int copy_id) {
        ResultSet resultSet = null;
        String query = "SELECT Book.* FROM Book " +
                "JOIN BookCopy ON Book.book_id = BookCopy.book_id " +
                "JOIN BorrowTransaction ON BookCopy.copy_id = BorrowTransaction.copy_id " +
                "WHERE BorrowTransaction.copy_id = ?";

        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, copy_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public String getBookTitleByCopyId(int copyId) {
        String title = null;
        String query = "SELECT b.title " +
                "FROM Book b " +
                "JOIN BookCopy bc ON b.book_id = bc.book_id " +
                "WHERE bc.copy_id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the copyId parameter
            pstmt.setInt(1, copyId);

            // Execute the query and get the result set
            ResultSet rs = pstmt.executeQuery();

            // Check if the result set contains a title
            if (rs.next()) {
                title = rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return title; // Will return null if no book found for the given copyId
    }

    /**
     * Retrieves book data based on the copy ID.
     *
     * @param copy_id the ID of the book copy used to fetch the corresponding book
     *                data.
     * @return a ResultSet containing the book data. If no matching record is found,
     *         the ResultSet will be empty.
     * @throws SQLException if a database access error occurs.
     */
    public ResultSet get1BookDataByCopyID(int copy_id) {
        ResultSet resultSet = null;
        String query = "SELECT Book.* FROM Book " +
                "JOIN BookCopy ON Book.book_id = BookCopy.book_id " +
                "JOIN BorrowTransaction ON BookCopy.copy_id = BorrowTransaction.copy_id " +
                "WHERE BorrowTransaction.copy_id = ? " +
                "LIMIT 1";

        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, copy_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Retrieves transaction details for a specific client based on their client ID.
     *
     * @param client_id the ID of the client whose transactions are to be retrieved.
     * @return a ResultSet containing transaction details, including transaction ID,
     *         book title,
     *         copy ID, borrow date, return date, and status. If no transactions are
     *         found, the ResultSet will be empty.
     * @throws SQLException if a database access error occurs during the query
     *                      execution.
     */
    public ResultSet getTransactionByClientID(int client_id) {
        ResultSet resultSet = null;
        String query = "SELECT " +
                "bt.transaction_id, " +
                "b.title, " +
                "bt.copy_id, " +
                "bt.borrow_date, " +
                "bt.return_date, " +
                "bt.status " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "WHERE bt.client_id = ?";
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, client_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Retrieves all borrow transactions from the database.
     *
     * @return a ResultSet containing details of all borrow transactions, including
     *         transaction ID, client ID,
     *         copy ID, borrow date, return date, and status. If no transactions are
     *         present, the ResultSet will be empty.
     * @throws SQLException if a database access error occurs during the query
     *                      execution.
     */
    public ResultSet getAllBorrowTransactions() {
        ResultSet resultSet = null;
        String query = "SELECT transaction_id, client_id, copy_id, borrow_date, return_date, status FROM BorrowTransaction";
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * Retrieves the list of books borrowed by a specific client.
     *
     * @param client_id the ID of the client whose borrowed books are to be
     *                  retrieved
     * @return a {@link ResultSet} containing details of the books borrowed by the
     *         client,
     *         or {@code null} if an error occurs
     */
    public ResultSet getBookByClientID(int client_id) {
        ResultSet resultSet = null;
        String query = "SELECT " +
                "b.* " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "WHERE bt.client_id = ?";
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, client_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public Book getBookByBookId(int bookId) {
        String query = "SELECT * FROM Book WHERE book_id = ?";

        try {
            // Establish connection with the database
            Connection connection = this.dataSource.getConnection();

            // Prepare the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the book_id parameter
            preparedStatement.setInt(1, bookId);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if resultSet has data
            if (resultSet.next()) {
                // Map the ResultSet to the Book object
                return new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getString("language"),
                        resultSet.getString("description"),
                        resultSet.getInt("publication_year"),
                        resultSet.getString("image_path"),
                        resultSet.getDouble("average_rating"),
                        resultSet.getInt("review_count"),
                        1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Book();
    }

    /**
     * Retrieves the wish list of a specific client.
     *
     * @param client_id the ID of the client whose wish list is to be retrieved
     * @return a {@link ResultSet} containing details of the books in the wish list,
     *         or {@code null} if an error occurs
     */
    public ResultSet getWishList(int client_id) {
        ResultSet resultSet = null;
        String query = "SELECT " +
                "b.* " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "WHERE bt.client_id = ? AND bt.status = 'Processing' " +
                "LIMIT 1";
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, client_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Retrieves the details of the book currently being read by a specific client.
     *
     * @param client_id the ID of the client whose currently reading book is to be
     *                  retrieved
     * @return a {@link ResultSet} containing details of the book currently being
     *         read,
     *         or {@code null} if an error occurs
     */
    public ResultSet getReadingBook(int client_id) {
        ResultSet resultSet = null;
        String query = "SELECT " +
                "b.* " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "WHERE bt.client_id = ? AND bt.status = 'Processing' " +
                "LIMIT 1";
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, client_id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Retrieves the data of all books available in the library.
     *
     * @return a {@link ResultSet} containing details of all books,
     *         or {@code null} if an error occurs
     */
    public ResultSet getAllBookData() {
        ResultSet resultSet = null;
        String query = "SELECT * FROM Book";

        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Retrieves the title of a book by its ID.
     *
     * @param bookId the ID of the book whose title is to be retrieved
     * @return the title of the book as a {@link String},
     *         or {@code null} if the book is not found or an error occurs
     */
    public String getBookTitleById(int bookId) {
        String query = "SELECT title FROM Book WHERE book_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the top-rated books based on their average rating.
     *
     * @return a {@link ResultSet} containing details of the highest-rated books,
     *         or {@code null} if an error occurs
     */
    public ResultSet getHighestRatingBooks() {
        ResultSet resultSet = null;
        String query = "SELECT * FROM Book " +
                "ORDER BY average_rating DESC " +
                "LIMIT 6";
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Retrieves the top-rated books based on their average rating.
     *
     * @return a {@link ResultSet} containing details of the highest-rated books,
     *         or {@code null} if an error occurs
     */
    public ResultSet getTop1HighestRatingBooks() {
        ResultSet resultSet = null;
        String query = "SELECT * FROM Book " +
                "ORDER BY average_rating DESC " +
                "LIMIT 1";
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Lấy danh sách 10 cuốn sách có đánh giá cao nhất theo thể loại.
     *
     * @param genre Thể loại của sách. Nếu là "TẤT CẢ" hoặc null, sẽ lấy sách từ mọi
     *              thể loại.
     * @return Một đối tượng ResultSet chứa danh sách sách được sắp xếp theo đánh
     *         giá từ cao đến thấp.
     */
    public ResultSet getHighestRatingBooksByGenre(String genre) {
        ResultSet resultSet = null;
        String query = "SELECT *, " +
                "IFNULL(average_rating, 0.0) AS normalized_rating " +
                "FROM Book ";

        if (genre != null && !genre.equalsIgnoreCase("TẤT CẢ")) {
            query += "WHERE genre = ? ";
        }

        query += "ORDER BY normalized_rating DESC LIMIT 10";

        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Gán tham số nếu có genre
            if (genre != null && !genre.equalsIgnoreCase("TẤT CẢ")) {
                preparedStatement.setString(1, genre);
            }

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Lấy dữ liệu khách hàng dựa trên tên người dùng.
     *
     * @param username Tên người dùng của khách hàng.
     * @return Một đối tượng ResultSet chứa thông tin của khách hàng tương ứng với
     *         tên người dùng.
     */
    public ResultSet getClientData(String username) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.dataSource.getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Client WHERE username='" + username + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * Lấy dữ liệu quản trị viên dựa trên tên người dùng.
     *
     * @param username Tên người dùng của quản trị viên.
     * @return Một đối tượng ResultSet chứa thông tin của quản trị viên tương ứng
     *         với tên người dùng.
     */
    public ResultSet getAdminData(String username) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.dataSource.getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM admin WHERE username='" + username + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * Tạo một khách hàng mới với các thông tin cung cấp.
     *
     * @param email         Địa chỉ email của khách hàng.
     * @param phone_number  Số điện thoại của khách hàng.
     * @param address       Địa chỉ của khách hàng.
     * @param username      Tên người dùng của khách hàng.
     * @param password_hash Mã hóa mật khẩu của khách hàng.
     * @param name          Tên của khách hàng.
     */
    public void createClient(String email, String phone_number, String address, String username, String password_hash,
            String name) {
        String newLibraryCardNum = null;
        int newClientId = 0;
        BigDecimal outstanding_fees = BigDecimal.ZERO;

        String query = "SELECT client_id, library_card_number FROM Client ORDER BY client_id DESC LIMIT 1";
        String insertQuery = "INSERT INTO Client "
                + "(Client_id, name, library_card_number, email, phone_number, address, registration_date, outstanding_fees, username, password_hash) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Lấy thời gian hiện tại để làm registration_date
        LocalDateTime now = LocalDateTime.now();
        String registration_date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String Id = rs.getString("client_id");
                String lastLibraryCardNum = rs.getString("library_card_number");
                int IdNum = Integer.parseInt(Id);
                int newLibraryCardLastNum = Integer.parseInt(lastLibraryCardNum.substring(3)) + 1;
                newLibraryCardNum = String.format("LIB%05d", newLibraryCardLastNum);
                newClientId = IdNum + 1;
            } else {

                newClientId = 1;
                newLibraryCardNum = "LIB00001";
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, newClientId);
                insertStmt.setString(2, name);
                insertStmt.setString(3, newLibraryCardNum);
                insertStmt.setString(4, email);
                insertStmt.setString(5, phone_number);
                insertStmt.setString(6, address);
                insertStmt.setString(7, registration_date);
                insertStmt.setBigDecimal(8, outstanding_fees);
                insertStmt.setString(9, username);
                insertStmt.setString(10, password_hash);
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy tên khách hàng dựa trên ID của khách hàng.
     *
     * @param clientId ID của khách hàng cần lấy tên.
     * @return Tên của khách hàng nếu tìm thấy, ngược lại trả về null.
     */
    public String getClientNameById(int clientId) {
        String query = "SELECT name FROM Client WHERE client_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, clientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách thông báo cho người nhận dựa trên ID, loại tài khoản và giới
     * hạn số lượng.
     *
     * @param recipientId ID của người nhận thông báo.
     * @param AccountType Loại tài khoản của người nhận (ví dụ: Client, Admin).
     * @param limit       Số lượng thông báo tối đa cần lấy. Nếu nhỏ hơn hoặc bằng
     *                    0, không giới hạn.
     * @return Một đối tượng ResultSet chứa danh sách thông báo theo tiêu chí đã
     *         cho, hoặc null nếu xảy ra lỗi.
     */
    public ResultSet getNotifications(int recipientId, String AccountType, int limit) {
        String query = "SELECT * FROM Notification WHERE recipient_id = ? And recipient_type = ? ORDER BY is_read ASC, created_at DESC";

        if (limit > 0) {
            query += " LIMIT ?";
        }
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, recipientId);
            pstmt.setString(2, AccountType);
            if (limit > 0) {
                pstmt.setInt(3, limit);
            }
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Xóa một thông báo dựa trên ID của thông báo đó.
     *
     * @param notificationId ID của thông báo cần xóa.
     */
    public void deleteNotification(int notificationId) {
        String query = "DELETE FROM Notification WHERE notification_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, notificationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật trạng thái đọc của một thông báo dựa trên ID của thông báo đó.
     *
     * @param notificationId ID của thông báo cần cập nhật.
     * @param isRead         Trạng thái đọc mới của thông báo. {@code true} nếu đã
     *                       đọc, {@code false} nếu chưa đọc.
     */
    public void updateNotification(int notificationId, boolean isRead) {
        String query = "UPDATE Notification SET is_read = ? WHERE notification_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBoolean(1, isRead);
            pstmt.setInt(2, notificationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Chèn một thông báo mới vào cơ sở dữ liệu.
     *
     * @param notification Đối tượng {@link Notification} chứa thông tin thông báo
     *                     cần chèn.
     * @return {@code true} nếu chèn thành công, {@code false} nếu có lỗi xảy ra.
     */
    public boolean insertNotification(Notification notification) {
        String query = "INSERT INTO Notification (recipient_id, recipient_type, notification_type, message, created_at, is_read) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, notification.getRecipientId());
            pstmt.setString(2, notification.getRecipientType().toString());
            pstmt.setString(3, notification.getNotificationType().toString());
            pstmt.setString(4, notification.getMessage());
            pstmt.setTimestamp(5, Timestamp.valueOf(notification.getCreatedAt()));
            pstmt.setBoolean(6, notification.isRead());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        notification.setNotificationId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy thông tin một thông báo dựa trên ID của thông báo đó.
     *
     * @param notificationId ID của thông báo cần lấy.
     * @return Đối tượng {@link Notification} chứa thông tin của thông báo nếu tìm
     *         thấy, ngược lại trả về {@code null}.
     */
    public Notification getNotificationById(int notificationId) {
        String query = "SELECT * FROM Notification WHERE notification_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, notificationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int recipientId = rs.getInt("recipient_id");
                RecipientType recipientType = RecipientType.valueOf(rs.getString("recipient_type"));
                NotificationType notificationType = NotificationType.valueOf(rs.getString("notification_type"));
                String message = rs.getString("message");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                boolean isRead = rs.getBoolean("is_read");
                return new Notification(notificationId, recipientId, recipientType, notificationType, message,
                        createdAt, isRead);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Đếm số lượng thông báo chưa đọc cho một người nhận cụ thể.
     *
     * @param recipientId ID của người nhận thông báo.
     * @param AccountType Loại tài khoản của người nhận (ví dụ: Client, Admin).
     * @return Số lượng thông báo chưa đọc.
     */
    public int countUnreadNotifications(int recipientId, String AccountType) {
        String query = "SELECT COUNT(*) AS unread_count FROM Notification WHERE recipient_id = ? AND recipient_type = ? AND is_read = false;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, recipientId);
            pstmt.setString(2, AccountType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("unread_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Đánh dấu tất cả các thông báo của một người nhận cụ thể là đã đọc.
     *
     * @param recipientId ID của người nhận thông báo.
     * @param AccountType Loại tài khoản của người nhận (ví dụ: Client, Admin).
     */
    public void markAllNotificationsAsRead(int recipientId, String AccountType) {
        String query = "UPDATE notification SET is_read = 1 WHERE recipient_id = ? AND recipient_type = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recipientId);
            stmt.setString(2, AccountType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Chèn một đánh giá sách mới vào cơ sở dữ liệu.
     *
     * @param bookId   ID của cuốn sách được đánh giá.
     * @param clientId ID của khách hàng đánh giá.
     * @param rating   Đánh giá số sao của sách. Có thể là {@code null} nếu không có
     *                 đánh giá số.
     * @param comment  Bình luận về sách. Có thể là {@code null} hoặc rỗng nếu không
     *                 có bình luận.
     * @return {@code true} nếu chèn thành công, {@code false} nếu có lỗi xảy ra.
     */
    public boolean insertBookReview(int bookId, int clientId, Double rating, String comment) {
        String query = "INSERT INTO BookReview (book_id, client_id, rating, comment) VALUES (?, ?, ?, ?);";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, clientId);
            if (rating != null) {
                pstmt.setDouble(3, rating);
            } else {
                pstmt.setNull(3, Types.DECIMAL);
            }
            if (comment != null && !comment.isEmpty()) {
                pstmt.setString(4, comment);
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                updateBookAverageRating(bookId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trung bình đánh giá và số lượng đánh giá của một cuốn sách dựa trên
     * các đánh giá hiện có.
     *
     * @param bookId ID của cuốn sách cần cập nhật đánh giá trung bình.
     */
    private void updateBookAverageRating(int bookId) {
        String avgQuery = "SELECT AVG(rating) AS avg_rating, COUNT(*) AS review_count FROM BookReview WHERE book_id = ? AND rating IS NOT NULL;";
        String updateBookQuery = "UPDATE Book SET average_rating = ?, review_count = ? WHERE book_id = ?;";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement avgStmt = conn.prepareStatement(avgQuery)) {
            avgStmt.setInt(1, bookId);
            ResultSet rs = avgStmt.executeQuery();

            if (rs.next()) {
                double avgRating = rs.getDouble("avg_rating");
                int reviewCount = rs.getInt("review_count");

                try (PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery)) {
                    updateBookStmt.setDouble(1, avgRating);
                    updateBookStmt.setInt(2, reviewCount);
                    updateBookStmt.setInt(3, bookId);
                    updateBookStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách tất cả các đánh giá cho một cuốn sách cụ thể.
     *
     * @param bookId ID của cuốn sách cần lấy đánh giá.
     * @return Một đối tượng {@link ObservableList} chứa danh sách các đánh giá cho
     *         cuốn sách, sắp xếp theo ngày đánh giá giảm dần.
     */
    public ObservableList<BookReview> getAllReviewsForBook(int bookId) {
        ObservableList<BookReview> reviews = FXCollections.observableArrayList();
        String query = "SELECT review_id, book_id, client_id, rating, comment, review_date " +
                "FROM BookReview WHERE book_id = ? ORDER BY review_date DESC;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int reviewId = rs.getInt("review_id");
                double rating = rs.getDouble("rating");
                String comment = rs.getString("comment");
                Timestamp reviewDate = rs.getTimestamp("review_date");

                BookReview review = new BookReview(reviewId, bookId, rs.getInt("client_id"), rating, comment,
                        reviewDate.toLocalDateTime());
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public ResultSet getAllBorrowTransactions2() {
        String query = "SELECT transaction_id, client_id, copy_id, borrow_date, return_date, status " +
                "FROM BorrowTransaction ORDER BY borrow_date DESC;";

        try {
            // Establish the database connection
            Connection conn = dataSource.getConnection();

            // Prepare the statement
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Execute the query and return the ResultSet
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return null if an exception occurs
        return null;
    }

    public ObservableList<BorrowTransaction> getAllBorrowTransactionsList() {
        ObservableList<BorrowTransaction> borrowTransactions = FXCollections.observableArrayList();
        String query = "SELECT transaction_id, client_id, copy_id, borrow_date, return_date, status " +
                "FROM BorrowTransaction ORDER BY borrow_date DESC;";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Execute the query and get the result set
            ResultSet rs = pstmt.executeQuery();

            // Iterate over the result set and create BorrowTransaction objects
            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                int clientId = rs.getInt("client_id");
                int copyId = rs.getInt("copy_id");

                LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
                LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate()
                        : null;
                String status = rs.getString("status");
                String title = Model.getInstance().getDatabaseDriver().getBookTitleByCopyId(copyId);

                // Create a new BorrowTransaction object
                BorrowTransaction transaction = new BorrowTransaction(transactionId,
                        clientId, title, copyId, borrowDate,
                        returnDate, status);
                borrowTransactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowTransactions;
    }

    public void exportAllBorrowTransactionsToExcel(String filePath) {
        try {
            ResultSet resultSet = Model.getInstance().getDatabaseDriver().getAllBorrowTransactions2();

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

    public ObservableList<Client> getAllClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String query = "SELECT client_id, name, library_card_number, email, phone_number, address, " +
                "registration_date, outstanding_fees, username, password_hash, avatar_image_path " +
                "FROM Client ORDER BY name ASC;";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Execute the query and get the result set
            ResultSet rs = pstmt.executeQuery();

            // Iterate over the result set and create Client objects
            while (rs.next()) {
                int clientId = rs.getInt("client_id");
                String name = rs.getString("name");
                String libraryCardNumber = rs.getString("library_card_number");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");
                Date registrationDate = rs.getDate("registration_date");
                double outstandingFees = rs.getDouble("outstanding_fees");
                String username = rs.getString("username");
                String passwordHash = rs.getString("password_hash");
                String avatarImagePath = rs.getString("avatar_image_path");

                // Create a new Client object
                Client client = new Client(clientId, name, libraryCardNumber, email, phoneNumber, address,
                        registrationDate, outstandingFees, username, passwordHash, avatarImagePath);

                // Add the client object to the list
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    public ResultSet getAllClients2() {
        String query = "SELECT client_id, name, library_card_number, email, phone_number, address, " +
                "registration_date, outstanding_fees, username, password_hash, avatar_image_path " +
                "FROM Client ORDER BY name ASC;";

        try {
            // Establish the database connection
            Connection conn = dataSource.getConnection();

            // Prepare the statement
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Execute the query and return the ResultSet
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return null if an exception occurs
        return null;
    }

    /**
     * Lấy đánh giá của một người dùng cụ thể cho một cuốn sách.
     *
     * @param bookId   ID của cuốn sách.
     * @param clientId ID của khách hàng.
     * @return Đối tượng {@link BookReview} chứa thông tin đánh giá nếu tìm thấy,
     *         ngược lại trả về {@code null}.
     */
    public BookReview getUserReview(int bookId, int clientId) {
        String query = "SELECT review_id, book_id, client_id, rating, comment, review_date FROM BookReview WHERE book_id = ? AND client_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, clientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int reviewId = rs.getInt("review_id");
                double rating = rs.getDouble("rating");
                String comment = rs.getString("comment");
                Timestamp reviewDate = rs.getTimestamp("review_date");

                return new BookReview(reviewId, bookId, clientId, rating, comment, reviewDate.toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Chèn một đánh giá sách mới hoặc cập nhật đánh giá hiện có của một khách hàng
     * cho một cuốn sách.
     *
     * @param bookId   ID của cuốn sách được đánh giá.
     * @param clientId ID của khách hàng đánh giá.
     * @param rating   Đánh giá số sao của sách. Có thể là {@code null} nếu không có
     *                 đánh giá số.
     * @param comment  Bình luận về sách. Có thể là {@code null} hoặc rỗng nếu không
     *                 có bình luận.
     * @return {@code true} nếu chèn hoặc cập nhật thành công, {@code false} nếu có
     *         lỗi xảy ra.
     */
    public boolean upsertBookReview(int bookId, int clientId, Double rating, String comment) {
        BookReview existingReview = getUserReview(bookId, clientId);

        if (existingReview == null) {
            return insertBookReview(bookId, clientId, rating, comment);
        } else {
            return updateBookReview(existingReview.getReviewId(), rating, comment);
        }
    }

    /**
     * Cập nhật một đánh giá sách hiện có.
     *
     * @param reviewId ID của đánh giá cần cập nhật.
     * @param rating   Đánh giá số sao mới. Có thể là {@code null} nếu không có đánh
     *                 giá số mới.
     * @param comment  Bình luận mới về sách. Có thể là {@code null} hoặc rỗng nếu
     *                 không có bình luận mới.
     * @return {@code true} nếu cập nhật thành công, {@code false} nếu có lỗi xảy
     *         ra.
     */
    public boolean updateBookReview(int reviewId, Double rating, String comment) {
        String query = "UPDATE BookReview SET rating = ?, comment = ?, review_date = ? WHERE review_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (rating != null) {
                pstmt.setDouble(1, rating);
            } else {
                pstmt.setNull(1, Types.DECIMAL);
            }
            if (comment != null && !comment.isEmpty()) {
                pstmt.setString(2, comment);
            } else {
                pstmt.setNull(2, Types.VARCHAR);
            }
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(4, reviewId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                String getBookIdQuery = "SELECT book_id FROM BookReview WHERE review_id = ?;";
                try (PreparedStatement getBookIdStmt = conn.prepareStatement(getBookIdQuery)) {
                    getBookIdStmt.setInt(1, reviewId);
                    ResultSet rs = getBookIdStmt.executeQuery();
                    if (rs.next()) {
                        int bookId = rs.getInt("book_id");
                        updateBookAverageRating(bookId);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy số lượng đánh giá cho một cuốn sách cụ thể.
     *
     * @param bookId ID của cuốn sách cần đếm số đánh giá.
     * @return Số lượng đánh giá của cuốn sách.
     */
    public int getReviewCount(int bookId) {
        String query = "SELECT COUNT(*) AS count FROM BookReview WHERE book_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy tổng số điểm đánh giá của một cuốn sách cụ thể.
     *
     * @param bookId ID của cuốn sách cần tính tổng điểm đánh giá.
     * @return Tổng số điểm đánh giá của cuốn sách nếu tìm thấy, ngược lại trả về
     *         0.0.
     */
    public double getSumRatings(int bookId) {
        String query = "SELECT SUM(rating) AS sum FROM BookReview WHERE book_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("sum");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Lấy số lượng sách đã mượn của một khách hàng cụ thể.
     *
     * @param clientId ID của khách hàng cần đếm số lượng sách đã mượn.
     * @return Số lượng sách đã mượn nếu tìm thấy, ngược lại trả về 0.
     */
    public int getNumberOfBorrowedBooks(int clientId) {
        String query = "SELECT COUNT(*) AS count FROM BorrowTransaction WHERE client_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy cuốn sách yêu thích của một khách hàng dựa trên số lần mượn.
     *
     * @param clientId ID của khách hàng cần lấy cuốn sách yêu thích.
     * @return Đối tượng {@link Book} là cuốn sách yêu thích nếu tìm thấy, ngược lại
     *         trả về {@code null}.
     */
    public Book getClientFavouriteBook(int clientId) {
        String query = "SELECT b.*, COUNT(bt.copy_id) AS borrow_count " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "WHERE bt.client_id = ? " +
                "GROUP BY bc.book_id " +
                "ORDER BY borrow_count DESC " +
                "LIMIT 1";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy thể loại sách yêu thích của một khách hàng dựa trên số lần mượn.
     *
     * @param clientId ID của khách hàng cần lấy thể loại yêu thích.
     * @return Thể loại yêu thích nếu tìm thấy, ngược lại trả về {@code null}.
     */
    public String getClientFavouriteGenre(int clientId) {
        String query = "SELECT b.genre, COUNT(b.genre) AS genre_count " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "WHERE bt.client_id = ? " +
                "GROUP BY b.genre " +
                "ORDER BY genre_count DESC " +
                "LIMIT 1";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("genre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách các hoạt động gần đây của một khách hàng, bao gồm ngày mượn và
     * tiêu đề sách.
     *
     * @param clientId ID của khách hàng cần lấy hoạt động.
     * @param limit    Số lượng hoạt động gần đây cần lấy.
     * @return Một danh sách {@link List} chứa các chuỗi mô tả hoạt động gần đây.
     */
    public List<String> getClientRecentActivities(int clientId, int limit) {
        String query = "SELECT bt.borrow_date, b.title " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "WHERE bt.client_id = ? " +
                "ORDER BY bt.borrow_date DESC " +
                "LIMIT ?";
        List<String> activities = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Date borrowDate = rs.getDate("borrow_date");
                String title = rs.getString("title");
                activities.add("Đã mượn '" + title + "' vào ngày " + borrowDate.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    /**
     * Lấy danh sách các cuốn sách hàng đầu mà khách hàng đã đánh giá cao nhất.
     *
     * @param clientId ID của khách hàng cần lấy danh sách sách.
     * @param limit    Số lượng sách hàng đầu cần lấy.
     * @return Chuỗi chứa thông tin các cuốn sách hàng đầu, mỗi cuốn sách được phân
     *         tách bằng ký tự ngăn cách dòng.
     */
    public String getTopBooksForClient(int clientId, int limit) {
        String query = "SELECT b.book_id, b.author, b.image_path, br.rating AS client_rating " +
                "FROM BookReview br " +
                "JOIN Book b ON br.book_id = b.book_id " +
                "WHERE br.client_id = ? " +
                "ORDER BY br.rating DESC " +
                "LIMIT ?";
        StringBuilder result = new StringBuilder();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String author = rs.getString("author");
                    String imagePath = rs.getString("image_path");
                    double clientRating = rs.getDouble("client_rating");

                    result.append(bookId).append("|")
                            .append(author).append("|")
                            .append(imagePath).append("|")
                            .append(clientRating).append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * Lấy xu hướng mượn sách hàng tháng của một khách hàng cụ thể.
     *
     * @param clientId ID của khách hàng cần lấy xu hướng mượn sách.
     * @return Một {@link Map} chứa khóa là tháng (định dạng "YYYY-MM") và giá trị
     *         là số lượng sách đã mượn trong tháng đó.
     */
    public Map<String, Integer> getMonthlyBorrowingTrends(int clientId) {
        String query = "SELECT DATE_FORMAT(borrow_date, '%Y-%m') AS month, COUNT(*) AS borrow_count " +
                "FROM BorrowTransaction " +
                "WHERE client_id = ? " +
                "GROUP BY month " +
                "ORDER BY month ASC";
        Map<String, Integer> trends = new LinkedHashMap<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String month = rs.getString("month");
                int count = rs.getInt("borrow_count");
                trends.put(month, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trends;
    }

    /**
     * Lấy xu hướng mượn sách theo thể loại của một khách hàng cụ thể.
     *
     * @param clientId ID của khách hàng cần lấy xu hướng mượn sách theo thể loại.
     * @return Một {@link Map} chứa khóa là thể loại sách và giá trị là số lượng
     *         sách đã mượn thuộc thể loại đó.
     */
    public Map<String, Integer> getBorrowingTrendsByCategory(int clientId) {
        String query = "SELECT b.genre, COUNT(*) AS borrow_count " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "WHERE bt.client_id = ? " +
                "GROUP BY b.genre";
        Map<String, Integer> trends = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String genre = rs.getString("genre");
                int count = rs.getInt("borrow_count");
                trends.put(genre, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trends;
    }

    /**
     * Trích xuất thông tin của một cuốn sách từ đối tượng {@link ResultSet}.
     *
     * @param rs Đối tượng {@link ResultSet} chứa dữ liệu của cuốn sách.
     * @return Đối tượng {@link Book} được tạo từ dữ liệu trong {@link ResultSet}.
     * @throws SQLException Nếu có lỗi xảy ra khi truy xuất dữ liệu từ
     *                      {@link ResultSet}.
     */
    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        int book_id = rs.getInt("book_id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String isbn = rs.getString("isbn");
        String genre = rs.getString("genre");
        String language = rs.getString("language");
        String description = rs.getString("description");
        int publication_year = rs.getInt("publication_year");
        String image_path = rs.getString("image_path");
        double average_rating = rs.getDouble("average_rating");
        int review_count = rs.getInt("review_count");

        int quantity = countBookCopies(book_id);

        return new Book(book_id, title, author, isbn, genre, language, description, publication_year, image_path,
                average_rating, review_count, quantity);
    }

    /**
     * Lấy một bản sao sách khả dụng của một cuốn sách cụ thể.
     *
     * @param bookId ID của cuốn sách cần lấy bản sao khả dụng.
     * @return Đối tượng {@link BookCopy} là bản sao sách khả dụng nếu tìm thấy,
     *         ngược lại trả về {@code null}.
     */
    public BookCopy getAvailableBookCopy(int bookId) {
        String query = "SELECT * FROM BookCopy WHERE book_id = ? AND is_available = TRUE LIMIT 1";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new BookCopy(
                        rs.getInt("copy_id"),
                        rs.getInt("book_id"),
                        rs.getBoolean("is_available"),
                        rs.getString("book_condition"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy ID của cuốn sách dựa trên ID của bản sao sách.
     *
     * @param copyId ID của bản sao sách cần lấy ID cuốn sách.
     * @return ID của cuốn sách nếu tìm thấy, ngược lại trả về -1.
     */
    public int getBookIdByCopyId(int copyId) {
        int bookId = -1;

        String query = "SELECT book_id FROM BookCopy WHERE copy_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, copyId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bookId = rs.getInt("book_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookId;
    }

    /**
     * Đếm số lượng bản sao sách khả dụng của một cuốn sách cụ thể.
     *
     * @param book_id ID của cuốn sách cần đếm bản sao.
     * @return Số lượng bản sao sách khả dụng nếu tìm thấy, ngược lại trả về 0.
     */
    public int countBookCopies(int book_id) {
        int count = 0;
        String query = "SELECT COUNT(*) AS count FROM BookCopy WHERE book_id = ? AND is_available = true";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, book_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void exportClientBorrowTransactionsToExcel(String filePath) {
        try {
            ResultSet resultSet = Model.getInstance().getDatabaseDriver().getAllClients2();

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

    public void exportClientsToExcel(String filePath) {
        try {
            // Retrieve the ResultSet for all clients
            ResultSet resultSet = Model.getInstance().getDatabaseDriver().getAllClients2();

            // Create a new workbook and sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Clients");

            // Create the header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Client ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Library Card Number");
            headerRow.createCell(3).setCellValue("Email");
            headerRow.createCell(4).setCellValue("Phone Number");
            headerRow.createCell(5).setCellValue("Address");
            headerRow.createCell(6).setCellValue("Registration Date");
            headerRow.createCell(7).setCellValue("Outstanding Fees");
            headerRow.createCell(8).setCellValue("Username");

            // Write client data into the sheet
            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(resultSet.getInt("client_id"));
                row.createCell(1).setCellValue(resultSet.getString("name"));
                row.createCell(2).setCellValue(resultSet.getString("library_card_number"));
                row.createCell(3).setCellValue(resultSet.getString("email"));
                row.createCell(4).setCellValue(resultSet.getString("phone_number"));
                row.createCell(5).setCellValue(resultSet.getString("address"));
                row.createCell(6).setCellValue(resultSet.getDate("registration_date").toString());
                row.createCell(7).setCellValue(resultSet.getDouble("outstanding_fees"));
                row.createCell(8).setCellValue(resultSet.getString("username"));
            }

            // Save the workbook to the specified file path
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            // Close resources
            workbook.close();
            resultSet.close();

            System.out.println("Excel file generated successfully: " + filePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public int adjustBookCopies(int book_id, int quantity) {
        int currentCopies = countBookCopies(book_id); // Get the current number of copies
        int processedCopies = currentCopies; // Track the updated number of copies

        String findQuery = "SELECT copy_id FROM BookCopy WHERE book_id = ? LIMIT 1";
        String deleteQuery = "DELETE FROM BookCopy WHERE copy_id = ?";
        String deleteTransactionQuery = "DELETE FROM BorrowTransaction WHERE copy_id = ?";
        String checkTransactionStatusQuery = "SELECT status FROM BorrowTransaction WHERE copy_id = ?";
        String insertQuery = "INSERT INTO BookCopy (book_id) VALUES (?)";
        String deleteBookQuery = "DELETE FROM Book WHERE book_id = ?";
        String deleteNotificationsQuery = "DELETE FROM NotificationRequest WHERE book_id = ?";
        String deleteReviewsQuery = "DELETE FROM BookReview WHERE book_id = ?";

        if (quantity == 0) {
            // Delete the book and all its copies
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement deleteBookStmt = conn.prepareStatement(deleteBookQuery);
                    PreparedStatement findStmt = conn.prepareStatement(findQuery);
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                    PreparedStatement deleteTransStmt = conn.prepareStatement(deleteTransactionQuery);
                    PreparedStatement checkStatusStmt = conn.prepareStatement(checkTransactionStatusQuery);
                    PreparedStatement deleteNotificationsStmt = conn.prepareStatement(deleteNotificationsQuery);
                    PreparedStatement deleteReviewsStmt = conn.prepareStatement(deleteReviewsQuery)) {

                conn.setAutoCommit(false); // Start transaction

                // Delete all book copies and associated borrow transactions
                while (true) {
                    findStmt.setInt(1, book_id);
                    try (ResultSet rs = findStmt.executeQuery()) {
                        if (rs.next()) {
                            int copyId = rs.getInt("copy_id");

                            // Check the status of the associated borrow transaction
                            checkStatusStmt.setInt(1, copyId);
                            try (ResultSet statusRs = checkStatusStmt.executeQuery()) {
                                if (statusRs.next()) {
                                    String status = statusRs.getString("status");
                                    if ("processing".equalsIgnoreCase(status)) {
                                        System.out.println("Cannot delete copy ID " + copyId
                                                + ": Transaction is still processing.");
                                        continue; // Skip deleting this copy
                                    }
                                }
                            }

                            // Delete associated borrow transaction (if any)
                            deleteTransStmt.setInt(1, copyId);
                            deleteTransStmt.executeUpdate();

                            // Delete the book copy
                            deleteStmt.setInt(1, copyId);
                            deleteStmt.executeUpdate();
                            processedCopies--; // Decrement the count of remaining copies
                        } else {
                            break; // No more copies to delete
                        }
                    }
                }

                // Delete associated notifications
                deleteNotificationsStmt.setInt(1, book_id);
                deleteNotificationsStmt.executeUpdate();

                // Delete associated reviews
                deleteReviewsStmt.setInt(1, book_id);
                deleteReviewsStmt.executeUpdate();

                // Delete the book itself if no copies are left
                if (processedCopies == 0) {
                    deleteBookStmt.setInt(1, book_id);
                    deleteBookStmt.executeUpdate();
                }

                conn.commit(); // Commit the transaction
                System.out.println("Book and all associated records deleted successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error adjusting book copies.");
            }
        } else if (quantity > currentCopies) {
            // Add more copies
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

                for (int i = 0; i < quantity - currentCopies; i++) {
                    insertStmt.setInt(1, book_id);
                    insertStmt.executeUpdate();
                    processedCopies++; // Increment the count of copies
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error adding book copies.");
            }
        } else if (quantity < currentCopies) {
            // Remove copies
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement findStmt = conn.prepareStatement(findQuery);
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                    PreparedStatement deleteTransStmt = conn.prepareStatement(deleteTransactionQuery);
                    PreparedStatement checkStatusStmt = conn.prepareStatement(checkTransactionStatusQuery)) {

                conn.setAutoCommit(false); // Start transaction

                for (int i = 0; i < currentCopies - quantity; i++) {
                    findStmt.setInt(1, book_id);
                    try (ResultSet rs = findStmt.executeQuery()) {
                        if (rs.next()) {
                            int copyId = rs.getInt("copy_id");

                            // Check the status of the associated borrow transaction
                            checkStatusStmt.setInt(1, copyId);
                            try (ResultSet statusRs = checkStatusStmt.executeQuery()) {
                                if (statusRs.next()) {
                                    String status = statusRs.getString("status");
                                    if ("processing".equalsIgnoreCase(status)) {
                                        System.out.println("Cannot delete copy ID " + copyId
                                                + ": Transaction is still processing.");
                                        continue; // Skip deleting this copy
                                    }
                                }
                            }

                            // Delete associated borrow transaction (if any)
                            deleteTransStmt.setInt(1, copyId);
                            deleteTransStmt.executeUpdate();

                            // Delete the book copy
                            deleteStmt.setInt(1, copyId);
                            deleteStmt.executeUpdate();
                            processedCopies--; // Decrement the count of remaining copies
                        } else {
                            break; // No more copies to delete
                        }
                    }
                }

                conn.commit(); // Commit the transaction
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error removing book copies.");
            }
        }

        if (processedCopies < 0)
            return 0;
        return processedCopies; // Return the updated number of book copies
    }

    /**
     * Tạo một giao dịch mượn sách mới cho một khách hàng cụ thể.
     *
     * @param clientId ID của khách hàng mượn sách.
     * @param copyId   ID của bản sao sách được mượn.
     * @return {@code true} nếu tạo giao dịch thành công, {@code false} nếu có lỗi
     *         xảy ra.
     */
    public boolean createBorrowTransaction(int clientId, int copyId) {
        String query = "INSERT INTO BorrowTransaction (client_id, copy_id, borrow_date, status) VALUES (?, ?, ?, 'Processing')";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, copyId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái khả dụng của một bản sao sách cụ thể.
     *
     * @param copyId      ID của bản sao sách cần cập nhật.
     * @param isAvailable Trạng thái khả dụng mới của bản sao sách. {@code true} nếu
     *                    bản sao đang có sẵn, {@code false} nếu không.
     * @return {@code true} nếu cập nhật thành công, {@code false} nếu có lỗi xảy
     *         ra.
     */
    public boolean updateBookCopyAvailability(int copyId, boolean isAvailable) {
        String query = "UPDATE BookCopy SET is_available = ? WHERE copy_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, copyId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách các giao dịch mượn sách đang hoạt động (trạng thái
     * 'Processing') của một khách hàng cụ thể.
     *
     * @param clientId ID của khách hàng cần lấy danh sách giao dịch.
     * @return Một {@link List} chứa các đối tượng {@link BorrowTransaction} đang
     *         hoạt động, hoặc danh sách rỗng nếu không có giao dịch nào.
     */
    public List<BorrowTransaction> getActiveBorrowTransactions(int clientId) {
        List<BorrowTransaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM BorrowTransaction WHERE client_id = ? AND status = 'Processing'";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BorrowTransaction transaction = new BorrowTransaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setClientId(rs.getInt("client_id"));
                transaction.setCopyId(rs.getInt("copy_id"));
                transaction.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                transaction.setReturnDate(null);
                transaction.setStatus(rs.getString("status"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    /**
     * Lấy danh sách tất cả các giao dịch mượn sách đang hoạt động (trạng thái
     * 'Processing').
     *
     * @return Một {@link List} chứa các đối tượng {@link BorrowTransaction} đang
     *         hoạt động, hoặc danh sách rỗng nếu không có giao dịch nào.
     */
    public List<BorrowTransaction> getActiveBorrowTransactions() {
        String query = "SELECT * FROM BorrowTransaction WHERE status = 'Processing'";
        List<BorrowTransaction> activeTransactions = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BorrowTransaction transaction = new BorrowTransaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setClientId(rs.getInt("client_id"));
                transaction.setCopyId(rs.getInt("copy_id"));
                transaction.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                transaction.setReturnDate(null);
                transaction.setStatus(rs.getString("status"));
                activeTransactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeTransactions;
    }

    /**
     * Tạo một yêu cầu thông báo mới cho một khách hàng cụ thể về một cuốn sách.
     *
     * @param clientId ID của khách hàng tạo yêu cầu.
     * @param bookId   ID của cuốn sách mà khách hàng yêu cầu thông báo khi có sẵn.
     * @return {@code true} nếu tạo yêu cầu thành công, {@code false} nếu có lỗi xảy
     *         ra.
     */
    public boolean createNotificationRequest(int clientId, int bookId) {
        String query = "INSERT INTO NotificationRequest (client_id, book_id, request_date) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, bookId);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra xem một khách hàng có bất kỳ giao dịch mượn sách đang hoạt động nào
     * cho một cuốn sách cụ thể hay không.
     *
     * @param clientId ID của khách hàng cần kiểm tra.
     * @param bookId   ID của cuốn sách cần kiểm tra.
     * @return {@code true} nếu khách hàng có ít nhất một giao dịch mượn đang hoạt
     *         động cho cuốn sách đó, {@code false} ngược lại.
     */
    public boolean hasActiveBorrowForBook(int clientId, int bookId) {
        String query = "SELECT COUNT(*) AS count FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "WHERE bt.client_id = ? AND bc.book_id = ? AND bt.status = 'Processing'";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy yêu cầu thông báo của một khách hàng cụ thể cho một cuốn sách.
     *
     * @param clientId ID của khách hàng.
     * @param bookId   ID của cuốn sách.
     * @return Đối tượng {@link NotificationRequest} nếu tìm thấy, ngược lại trả về
     *         {@code null}.
     */
    public NotificationRequest getNotificationRequest(int clientId, int bookId) {
        String query = "SELECT * FROM NotificationRequest WHERE client_id = ? AND book_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new NotificationRequest(
                        rs.getInt("request_id"),
                        rs.getInt("client_id"),
                        rs.getInt("book_id"),
                        rs.getTimestamp("request_date").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả các yêu cầu thông báo cho một cuốn sách cụ thể.
     *
     * @param bookId ID của cuốn sách cần lấy danh sách yêu cầu.
     * @return Một {@link List} chứa các đối tượng {@link NotificationRequest} nếu
     *         có, ngược lại trả về danh sách rỗng.
     */
    public List<NotificationRequest> getNotificationRequestsForBook(int bookId) {
        List<NotificationRequest> requests = new ArrayList<>();
        String query = "SELECT * FROM NotificationRequest WHERE book_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                NotificationRequest request = new NotificationRequest(
                        rs.getInt("request_id"),
                        rs.getInt("client_id"),
                        rs.getInt("book_id"),
                        rs.getTimestamp("request_date").toLocalDateTime());
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * Xóa một yêu cầu thông báo dựa trên ID của yêu cầu đó.
     *
     * @param requestId ID của yêu cầu thông báo cần xóa.
     * @return {@code true} nếu xóa thành công, {@code false} nếu có lỗi xảy ra.
     */
    public boolean deleteNotificationRequest(int requestId) {
        String query = "DELETE FROM NotificationRequest WHERE request_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, requestId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xử lý việc trả sách cho một giao dịch mượn cụ thể.
     *
     * <p>
     * Phương thức này sẽ cập nhật trạng thái giao dịch thành 'Returned', cập nhật
     * lại trạng thái khả dụng của bản sao sách,
     * và gửi thông báo cho các yêu cầu thông báo liên quan đến cuốn sách đó.
     * </p>
     *
     * @param transactionId ID của giao dịch mượn cần xử lý.
     * @return {@code true} nếu xử lý thành công, {@code false} nếu có lỗi xảy ra.
     */
    public boolean processBookReturn(int transactionId) {
        String updateTransaction = "UPDATE BorrowTransaction SET status = 'Done', return_date = ? WHERE transaction_id = ?";
        String getCopyIdQuery = "SELECT copy_id FROM BorrowTransaction WHERE transaction_id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            int copyId = 0;
            int bookId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(getCopyIdQuery)) {
                stmt.setInt(1, transactionId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    copyId = rs.getInt("copy_id");
                } else {
                    conn.rollback();
                    return false;
                }
            }
            bookId = getBookIdByCopyId(copyId);

            try (PreparedStatement stmt = conn.prepareStatement(updateTransaction)) {
                stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setInt(2, transactionId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            boolean copyUpdated = updateBookCopyAvailability(copyId, true);
            if (!copyUpdated) {
                conn.rollback();
                return false;
            }
            Book currentBook = getBookByBookId(bookId);
            currentBook
                    .setQuantity(Model.getInstance().getDatabaseDriver().countBookCopies(currentBook.getBook_id()));

            List<NotificationRequest> requests = getNotificationRequestsForBook(bookId);
            String bookTitle = getBookTitleById(bookId);
            for (NotificationRequest request : requests) {
                int recipientId = request.getClientId();
                RecipientType recipientType = RecipientType.Client;
                NotificationType notificationType = NotificationType.BookAvailable;
                String message = "Một bản sao của cuốn sách '" + bookTitle + "' bạn đã đăng ký đang có sẵn để mượn.";

                Notification notification = new Notification(recipientId, recipientType, notificationType, message);

                boolean notificationCreated = insertNotification(notification);
                if (!notificationCreated) {
                    System.err.println("Không thể tạo thông báo cho client_id: " + request.getClientId());
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra xem một giao dịch mượn có được gửi nhắc nhở trả sách hay không dựa
     * trên số ngày đã mượn.
     *
     * @param transactionId ID của giao dịch mượn cần kiểm tra.
     * @param dayBorrowed   Số ngày đã mượn.
     * @return {@code true} nếu đã gửi nhắc nhở trả sách trong ngày mượn tương ứng,
     *         {@code false} ngược lại.
     */
    public boolean hasReturnReminder(int transactionId, long dayBorrowed) {
        String query = "";
        if (dayBorrowed == 5) {
            query = "SELECT COUNT(*) FROM Notification WHERE recipient_id = (SELECT client_id FROM BorrowTransaction WHERE transaction_id = ?) AND notification_type = 'ReturnReminder' AND DATE(created_at) = ?";
        } else if (dayBorrowed == 6) {
            query = "SELECT COUNT(*) FROM Notification WHERE recipient_id = (SELECT client_id FROM BorrowTransaction WHERE transaction_id = ?) AND notification_type = 'ReturnReminder' AND DATE(created_at) = ?";
        } else {
            return false;
        }

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            LocalDate reminderDate = LocalDate.now();
            stmt.setDate(2, Date.valueOf(reminderDate));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Đặt hình đại diện (avatar) cho một khách hàng cụ thể.
     *
     * @param clientId ID của khách hàng cần đặt hình đại diện.
     * @param fileURI  Đường dẫn URI đến tệp hình ảnh avatar.
     */
    public void setClientAvatar(int clientId, String fileURI) {
        String query = "UPDATE Client SET avatar_image_path = ? WHERE client_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fileURI);
            stmt.setInt(2, clientId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalNumberOfEmployees() {
        int totalEmployees = 0;
        String query = "SELECT COUNT(*) AS total FROM Admin";

        try (Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                totalEmployees = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalEmployees;
    }

    public int getTotalNumberOfBooks() {
        int totalClients = 0;
        String query = "SELECT COUNT(*) AS total FROM Book";

        try (Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                totalClients = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalClients;
    }

    public int getTotalNumberOfClients() {
        int totalClients = 0;
        String query = "SELECT COUNT(*) AS total FROM Client";

        try (Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                totalClients = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalClients;
    }

    public int getTotalNumberOfCurrentlyBorrowedBooks() {
        int totalCurrentlyBorrowedBooks = 0;
        String query = "SELECT COUNT(*) AS total FROM BorrowTransaction WHERE status = 'Processing'";

        try (Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                totalCurrentlyBorrowedBooks = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCurrentlyBorrowedBooks;
    }

    public Map<String, Integer> getMonthlyBorrowingTrendsForAllUsers() {
        Map<String, Integer> trends = new LinkedHashMap<>();

        // Query to get monthly borrowing trends across all users
        String query = "SELECT MONTH(borrow_date) AS month, COUNT(*) AS borrow_count " +
                "FROM BorrowTransaction " +
                "WHERE YEAR(borrow_date) = YEAR(CURRENT_DATE) " + // Filter by the current year
                "GROUP BY MONTH(borrow_date)";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String month = resultSet.getString("month");
                int count = resultSet.getInt("borrow_count");
                trends.put(month, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trends;
    }

    /**
     * Gửi yêu cầu API để xác minh địa chỉ email.
     *
     * @param email Địa chỉ email cần xác minh.
     * @return Phản hồi API dưới dạng chuỗi JSON, hoặc {@code null} nếu xảy ra lỗi.
     */
    public String getEmailValidationApiResponse(String email) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(
                    "https://emailvalidation.abstractapi.com/v1/?api_key=fe97d39becd94b14a4a19b97ebcc29a1&email="
                            + email);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                System.out.println("GET request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Truy vấn số lượng email khớp trong cơ sở dữ liệu.
     *
     * @param email Địa chỉ email cần kiểm tra.
     * @return Số lượng email khớp, hoặc {@code -1} nếu xảy ra lỗi.
     */
    public int getEmailCountFromDatabase(String email) {
        String query = "SELECT COUNT(*) FROM Client WHERE email = ?";
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return -1;
            }

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Map<String, Integer> getBorrowingTrendsByCategoryForAllUsers() {
        Map<String, Integer> trends = new LinkedHashMap<>();

        // Query to get the number of books borrowed per genre for all users
        String query = "SELECT genre, COUNT(*) AS borrow_count " +
                "FROM BorrowTransaction bt " +
                "JOIN BookCopy bc ON bt.copy_id = bc.copy_id " +
                "JOIN Book b ON bc.book_id = b.book_id " +
                "GROUP BY genre";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String genre = resultSet.getString("genre");
                int count = resultSet.getInt("borrow_count");
                trends.put(genre, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trends;
    }

    public Map<String, Double> getTotalFeesOverTimeForAllClients() {
        Map<String, Double> feesOverTime = new HashMap<>();

        String query = "SELECT DATE_FORMAT(registration_date, '%Y-%m') AS month, " +
                "SUM(outstanding_fees) AS total_fees FROM Client GROUP BY month ORDER BY month";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month");
                double totalFees = rs.getDouble("total_fees");
                feesOverTime.put(month, totalFees);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feesOverTime;
    }

    public void updateStatus(int copyID) {
        String query = "UPDATE BorrowTransaction SET status = 'Done' WHERE copy_id = ? AND status = 'Processing'";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, copyID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String query = "UPDATE Book SET title = ?, author = ?, isbn = ?, genre = ?, language = ?, description = ?, publication_year = ?, image_path = ? WHERE book_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Only set the parameter if the field is not null
            if (book.getTitle() != null) {
                pstmt.setString(1, book.getTitle());
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }

            if (book.getAuthor() != null) {
                pstmt.setString(2, book.getAuthor());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }

            if (book.getIsbn() != null) {
                pstmt.setString(3, book.getIsbn());
            } else {
                pstmt.setNull(3, java.sql.Types.VARCHAR);
            }

            if (book.getGenre() != null) {
                pstmt.setString(4, book.getGenre());
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }

            if (book.getLanguage() != null) {
                pstmt.setString(5, book.getLanguage());
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }

            if (book.getDescription() != null) {
                pstmt.setString(6, book.getDescription());
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }

            if (book.getPublication_year() != 0) {
                pstmt.setInt(7, book.getPublication_year());
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }

            if (book.getImagePath() != null) {
                pstmt.setString(8, book.getImagePath());
            } else {
                pstmt.setNull(8, java.sql.Types.VARCHAR);
            }

            // Set book_id at the end
            pstmt.setInt(9, book.getBook_id());

            // Execute the update query and check the number of affected rows
            int rowsUpdated = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gửi mật khẩu mới đến địa chỉ email của người nhận.
     *
     * @param recipientEmail Địa chỉ email của người nhận.
     * @param newPassword    Mật khẩu mới cần gửi.
     * @throws MessagingException Nếu có lỗi xảy ra trong quá trình gửi email.
     */
    public void sendNewPassword(String recipientEmail, String newPassword) throws MessagingException {
        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        String senderEmail = "thuha25121976@gmail.com";
        String senderPassword = "bbjh xcbp oxtj qozz";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Your New Password");
        message.setText("Dear customer,\n\nYour new password is: " + newPassword
                + "\n\nPlease log in and change your password as soon as possible.\n\nThank you.");
        Transport.send(message);
        System.out.println("Email sent successfully to " + recipientEmail);
    }

    /**
     * Cập nhật mật khẩu của người dùng trong cơ sở dữ liệu.
     *
     * @param username    Tên người dùng cần cập nhật mật khẩu.
     * @param newPassword Mật khẩu mới cần cập nhật.
     * @return Số dòng bị ảnh hưởng nếu cập nhật thành công, hoặc {@code 0} nếu
     *         không thành công.
     */
    public int updatePassword(String username, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        int row = 0;
        String query = "UPDATE client SET password_hash = ? WHERE username = ?";
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, username);
            row = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * Xóa tài khoản của người dùng dựa trên tên người dùng.
     *
     * <p>
     * Phương thức này sẽ xóa tất cả các giao dịch mượn sách liên quan, yêu cầu
     * thông báo và tài khoản của người dùng.
     * </p>
     *
     * @param username Tên người dùng của tài khoản cần xóa.
     */
    public void deleteAccount(String username) {
        String deleteTransactionsQuery = "DELETE FROM borrowtransaction WHERE client_id = (SELECT client_id FROM client WHERE username = ?)";
        String deleteNotificationRequestsQuery = "DELETE FROM notificationrequest WHERE client_id = (SELECT client_id FROM client WHERE username = ?)";
        String deleteAccountQuery = "DELETE FROM client WHERE username = ?";

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement deleteTransactionsStatement = connection.prepareStatement(deleteTransactionsQuery);
                PreparedStatement deleteNotificationRequestsStatement = connection
                        .prepareStatement(deleteNotificationRequestsQuery);
                PreparedStatement deleteAccountStatement = connection.prepareStatement(deleteAccountQuery)) {

            // Check connection validity
            if (connection == null || connection.isClosed()) {
                System.err.println("Invalid database connection!");
                return;
            }

            // Step 1: Delete related borrow transactions
            deleteTransactionsStatement.setString(1, username);
            int rowsAffectedInTransactions = deleteTransactionsStatement.executeUpdate();
            if (rowsAffectedInTransactions > 0) {
                System.out.println("Related borrow transactions deleted successfully.");
            }

            // Step 2: Delete related notification requests
            deleteNotificationRequestsStatement.setString(1, username);
            int rowsAffectedInNotificationRequests = deleteNotificationRequestsStatement.executeUpdate();
            if (rowsAffectedInNotificationRequests > 0) {
                System.out.println("Related notification requests deleted successfully.");
            }

            // Step 3: Delete the account from the client table
            deleteAccountStatement.setString(1, username);
            int rowsAffectedInAccount = deleteAccountStatement.executeUpdate();
            if (rowsAffectedInAccount > 0) {
                System.out.println("Account deleted successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int addBook2(Book book) {
        // Câu truy vấn kiểm tra xem sách đã tồn tại với ISBN chưa
        String checkQuery = "SELECT book_id FROM Book WHERE isbn = ?";
        String query = "INSERT INTO Book (title, author, isbn, genre, language, description, publication_year, image_path, average_rating, review_count) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Kiểm tra nếu sách với cùng ISBN đã tồn tại
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt2 = conn.prepareStatement(checkQuery)) {

            // Đặt giá trị của ISBN vào tham số
            stmt2.setString(1, book.getIsbn());

            try (ResultSet rs = stmt2.executeQuery()) {
                if (rs.next()) {
                    // Nếu sách đã tồn tại, trả về book_id
                    int existingBookId = rs.getInt("book_id");
                    return existingBookId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Chèn sách mới vào cơ sở dữ liệu nếu không tìm thấy sách với ISBN này
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Đặt các giá trị cho các tham số của câu lệnh INSERT
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, book.getLanguage());
            stmt.setString(6, book.getDescription());
            stmt.setInt(7, book.getPublication_year());
            stmt.setString(8, book.getImagePath());
            stmt.setBigDecimal(9, BigDecimal.ZERO); // Sử dụng BigDecimal cho rating
            stmt.setInt(10, 0); // Đặt review count là 0

            // Thực thi câu lệnh INSERT
            int rowsAffected = stmt.executeUpdate();

            // Nếu câu lệnh chèn thành công, lấy book_id từ các khóa tự động sinh
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int bookId = generatedKeys.getInt(1);
                        return bookId;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Trả về -1 nếu chèn sách thất bại
    }

    public int checkBookExist(String title) {
        String query = "SELECT book_id FROM Book WHERE title = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameter
            stmt.setString(1, title);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("book_id"); // Trả về book_id nếu tìm thấy
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Nếu không tìm thấy, trả về 0
    }

    public void addBookCopy(int bookId, boolean isAvailable, String condition) {
        String query = "INSERT INTO BookCopy (book_id, is_available, book_condition) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bookId); // Set the book_id
            stmt.setBoolean(2, isAvailable); // Set the availability status
            stmt.setString(3, condition); // Set the condition (New, Good, Fair, Poor)

            // Execute the INSERT statement
            int rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void ProcessReturnBook(int transaction_id) {
        String query = "UPDATE BorrowTransaction SET status = 'Done' WHERE transaction_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) { // Close the parenthesis here

            stmt.setInt(1, transaction_id); // Set the transaction_id
            stmt.executeUpdate(); // Execute the update

        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace if an error occurs
        }
    }

    public Map<Integer, Map<Integer, Double>> loadDataFromDatabase() {
        String query = "SELECT client_id, book_id, rating FROM BookReview;";
        Map<Integer, Map<Integer, Double>> resultMap = new HashMap<>();

        try (Connection conn = dataSource.getConnection(); // Replace dataSource with your actual data source
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int clientId = rs.getInt("client_id");
                int bookId = rs.getInt("book_id");
                double rating = rs.getDouble("rating");

                // Check if the clientId already exists in the resultMap
                resultMap.computeIfAbsent(clientId, k -> new HashMap<>())
                        .put(bookId, rating);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    // CREATE TABLE IF NOT EXISTS BookCopy (
    // copy_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique ID for each physical book
    // copy
    // book_id INT, -- Foreign key referencing the book this copy belongs to
    // is_available BOOLEAN DEFAULT TRUE, -- Availability status of the book copy
    // book_condition ENUM('New', 'Good', 'Fair', 'Poor') DEFAULT 'Good', --
    // Physical condition of the book copy
    // FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE CASCADE
    // );

    // public void toCSV() {
    // String query = "SELECT client_id, book_id, rating FROM BookReview;";
    // String output =
    // getClass().getResource("/resources/Database/Data.csv").getFile();

    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement pstmt = conn.prepareStatement(query);
    // ResultSet rs = pstmt.executeQuery();
    // BufferedWriter writer = Files.newBufferedWriter(Paths.get(output))) {

    // writer.write("client_id,book_id,rating\n");

    // // Write data rows
    // while (rs.next()) {
    // int userId = rs.getInt("client_id");
    // int bookId = rs.getInt("book_id");
    // double rating = rs.getDouble("rating");

    // try {
    // writer.write(userId + "," + bookId + "," + rating + "\n");
    // } catch (IOException e) {
    // e.printStackTrace(); // Handle the write exception
    // }
    // }

    // } catch (SQLException | IOException e) {
    // e.printStackTrace(); // Handle SQL and IOException for the whole block
    // }
    // }
    /**
     * Cập nhật địa chỉ của người dùng trong cơ sở dữ liệu.
     *
     * @param newAddress Địa chỉ mới cần cập nhật.
     * @param username   Tên người dùng cần cập nhật địa chỉ.
     * @return Số dòng bị ảnh hưởng nếu cập nhật thành công, hoặc {@code 0} nếu
     *         không thành công.
     */
    public int updateAddress(String newAddress, String username) {
        // Câu truy vấn để cập nhật địa chỉ
        String updateQuery = "UPDATE client SET address = ? WHERE username = ?";
        int row = 0;

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return 0;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, newAddress); // Đặt địa chỉ mới
            preparedStatement.setString(2, username); // Đặt tên người dùng

            // Thực thi câu lệnh update
            row = preparedStatement.executeUpdate();

            // Nếu có ít nhất một dòng bị ảnh hưởng, tức là địa chỉ đã được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * Cập nhật số điện thoại của người dùng trong cơ sở dữ liệu.
     *
     * @param newPhoneNumber Số điện thoại mới cần cập nhật.
     * @param username       Tên người dùng cần cập nhật số điện thoại.
     * @return Số dòng bị ảnh hưởng nếu cập nhật thành công, hoặc {@code 0} nếu
     *         không thành công.
     */
    public int updatePhoneNumber(String newPhoneNumber, String username) {
        // Câu truy vấn để cập nhật số điện thoại
        String updateQuery = "UPDATE client SET phone_number = ? WHERE username = ?";
        int row = 0;

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return 0;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, newPhoneNumber); // Đặt số điện thoại mới
            preparedStatement.setString(2, username); // Đặt tên người dùng

            // Thực thi câu lệnh update
            row = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * Cập nhật email của người dùng trong cơ sở dữ liệu.
     *
     * @param newEmail Địa chỉ email mới cần cập nhật.
     * @param username Tên người dùng cần cập nhật email.
     * @return Số dòng bị ảnh hưởng nếu cập nhật thành công, hoặc {@code 0} nếu
     *         không thành công.
     */
    public int updateEmail(String newEmail, String username) {
        // Câu truy vấn để cập nhật email
        String updateQuery = "UPDATE client SET email = ? WHERE username = ?";
        int row = 0;

        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Kiểm tra kết nối
            if (connection == null || connection.isClosed()) {
                System.err.println("Kết nối cơ sở dữ liệu không hợp lệ!");
                return 0;
            }

            // Cài đặt tham số vào câu truy vấn
            preparedStatement.setString(1, newEmail); // Đặt email mới
            preparedStatement.setString(2, username); // Đặt tên người dùng

            // Thực thi câu lệnh update
            row = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * Đếm số lượng tên người dùng khớp trong cơ sở dữ liệu.
     *
     * @param username Tên người dùng cần đếm.
     * @return Số lượng tên người dùng khớp, hoặc {@code 0} nếu không có.
     */
    public int getUsernameCount(String username) {
        String query = "SELECT COUNT(*) FROM Client WHERE username = ?";
        int count = 0;
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1); // Lấy số lượng tên người dùng
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
        return count; // Trả về số lần xuất hiện tên người dùng trong cơ sở dữ liệu
    }

}

// public boolean insertNotification(Notification notification) {
// String query = "
