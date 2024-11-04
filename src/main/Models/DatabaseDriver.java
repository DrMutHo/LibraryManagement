package main.Models;
import java.sql.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

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
}