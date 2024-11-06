package main.Models;

import java.sql.*;

public class DatabaseDriver {
    private Connection conn;

    public DatabaseDriver() {
        try {
            // Chuỗi kết nối MySQL
            String url = "jdbc:mysql://localhost:3306/library_management";
            String username = "root";
            String password = "T1enm0t711!@#$%";

            // Kết nối MySQL
            this.conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connect to database successfuly!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        return conn;
    }
}
