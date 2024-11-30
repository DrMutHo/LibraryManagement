package main.Models;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Views.NotificationType;
import main.Views.RecipientType;

public class DatabaseDriver {
    private HikariDataSource dataSource;

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
            config.setMaximumPoolSize(10); // Số lượng kết nối tối đa trong pool
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

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
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

    public ResultSet getNotifications(int recipientId, int limit) {
        String query = "SELECT * FROM Notification WHERE recipient_id = ? ORDER BY is_read ASC, created_at DESC";
        if (limit > 0) {
            query += " LIMIT ?";
        }
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, recipientId);
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

    public int countUnreadNotifications(int recipientId) {
        String query = "SELECT COUNT(*) AS unread_count FROM Notification WHERE recipient_id = ? AND is_read = false;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, recipientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("unread_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void markAllNotificationsAsRead(int recipientId) {
        String query = "UPDATE notification SET is_read = 1 WHERE recipient_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, recipientId);
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

        return new Book(book_id, title, author, isbn, genre, language, description, publication_year, image_path,
                average_rating, review_count);
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

    public void submitReportBug() {

    }
}
