package main.Models;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseDriver {
    private Connection conn;

    public DatabaseDriver() {
        try {
            // Chuỗi kết nối MySQL
            Dotenv dotenv = Dotenv.load();
            String url = "jdbc:mysql://localhost:3306/library_management";
            String username = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            // Kết nối MySQL
            this.conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connect to database successfuly!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
