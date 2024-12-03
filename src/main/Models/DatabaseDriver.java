package main.Models;

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
import main.Views.NotificationType;
import main.Views.RecipientType;

public class DatabaseDriver {
    private HikariDataSource dataSource;

    public HikariDataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }
    

    public DatabaseDriver() {
        try {
            // Tải biến môi trường
            Dotenv dotenv = Dotenv.load();
            String url = "jdbc:mysql://localhost:3306/library_management";
            String username = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            // Cấu hình HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(151); // Số lượng kết nối tối đa trong pool
            config.setConnectionTimeout(30000); // Thời gian chờ kết nối (30 giây)
            config.setIdleTimeout(600000); // Thời gian chờ kết nối không sử dụng (10 phút)
            config.setMaxLifetime(1800000); // Thời gian sống tối đa của kết nối (30 phút)

            // Tạo DataSource
            this.dataSource = new HikariDataSource(config);
            System.out.println("Connect to database successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    /**
     * 
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

    public ResultSet getHighestRatingBook() {
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
                pstmt.setInt(2, limit);
            }
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public boolean upsertBookReview(int bookId, int clientId, Double rating, String comment) {
        BookReview existingReview = getUserReview(bookId, clientId);

        if (existingReview == null) {
            return insertBookReview(bookId, clientId, rating, comment);
        } else {
            return updateBookReview(existingReview.getReviewId(), rating, comment);
        }
    }

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
                activities.add("Borrowed '" + title + "' on " + borrowDate.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

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

    public boolean processBookReturn(int transactionId) {
        String updateTransaction = "UPDATE BorrowTransaction SET status = 'Returned', return_date = ? WHERE transaction_id = ?";
        String getCopyIdQuery = "SELECT copy_id, book_id FROM BorrowTransaction WHERE transaction_id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            int copyId = 0;
            int bookId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(getCopyIdQuery)) {
                stmt.setInt(1, transactionId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    copyId = rs.getInt("copy_id");
                    bookId = rs.getInt("book_id");
                } else {
                    conn.rollback();
                    return false;
                }
            }

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

            List<NotificationRequest> requests = getNotificationRequestsForBook(bookId);
            String bookTitle = getBookTitleById(bookId);
            for (NotificationRequest request : requests) {
                int recipientId = request.getClientId();
                RecipientType recipientType = RecipientType.Client;
                NotificationType notificationType = NotificationType.BookAvailable;
                String message = "Một bản sao của cuốn sách" + bookTitle + "bạn đã đăng ký đang có sẵn để mượn.";

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

    /**
     * Gửi yêu cầu API để xác minh địa chỉ email.
     *
     * @param email Địa chỉ email cần xác minh.
     * @return Phản hồi API dưới dạng chuỗi JSON, hoặc null nếu xảy ra lỗi.
     */
    public String getEmailValidationApiResponse(String email) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("https://emailvalidation.abstractapi.com/v1/?api_key=fe97d39becd94b14a4a19b97ebcc29a1&email=" + email);
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
     * @return Số lượng email khớp.
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

    public String getEmailByUsername(String username) {
        String query = "SELECT email FROM client WHERE username = ?";
    
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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
        message.setText("Dear customer,\n\nYour new password is: " + newPassword + "\n\nPlease log in and change your password as soon as possible.\n\nThank you.");
        Transport.send(message);
        System.out.println("Email sent successfully to " + recipientEmail);
    }

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

    public int getUsernameCount(String username) {
        String query = "SELECT COUNT(*) FROM Client WHERE username = ?";
        int count = 0;
        try (Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);  // Lấy số lượng tên người dùng
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // In ra lỗi nếu có
        }
        return count;  // Trả về số lần xuất hiện tên người dùng trong cơ sở dữ liệu
    }
}
