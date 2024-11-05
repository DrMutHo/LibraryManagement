package main.Models;
import java.sql.*;
import java.time.LocalDate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

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

    public ResultSet getBookData(String ISBN) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.dataSource.getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Book WHERE ISBN='" + ISBN + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getAllBookData() {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.dataSource.getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Book;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getClientnData(String username) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.dataSource.getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Client WHERE username='" + username + "';");
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createClient(int Client_id, String library_card_number, String email,
    String phone_number, String address, String registration_date, double outstandingfees,
    String username, String password_hash) {
        Statement statement;
        PreparedStatement stmt;
        int newClientId;
        String newLibraryCardNum;
        try {
            String query = "SELECT clent_id, library_card_number FROM users ORDER BY client_id DESC LIMIT 1";
            stmt = this.dataSource.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("client_id");
                String lastLibraryCardNum = rs.getString("library_card_number");
                int lastIdNum = Integer.parseInt(lastId);
                int newlibraryCardLastNum = Integer.parseInt(lastLibraryCardNum.substring(3)) + 1;
                newLibraryCardNum = String.format("LIB%05d", newlibraryCardLastNum);
                newClientId = lastIdNum + 1;
            }

            statement = this.dataSource.getConnection().createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "Client (Client_id, name, library_card_number, email, phone_number, address," + 
                    "resgistration_date, outstandingfees, username, password_hash)" +
                    "VALUES ('"+Client_id+"', '"+library_card_number+"', '"+email+"', '"+
                    phone_number+"', '"+address+"', '" +registration_date+"', '"+
                    outstandingfees+"', '"+username+"', '"+password_hash+"';");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}