package main.Models;
import main.Models.Client;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Views.ViewFactory;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private boolean clientLoginSuccessFlag;
    private boolean adminLoginSuccessFlag;
    private final DatabaseDriver databaseDriver;
    private final ObservableList<Book> allBook;
    private final Client client;
    private final Admin admin;
    
    
        private Model() {
            this.viewFactory = new ViewFactory();
            this.clientLoginSuccessFlag = false;
            this.adminLoginSuccessFlag = false;
            this.databaseDriver = new DatabaseDriver();
            this.allBook = FXCollections.observableArrayList();
            this.client = new Client(0, "", "", "", "", "", null, 0, "", "");
            this.admin = new Admin(0, "", "", "");
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
    
        public void setAllBook() {
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
    
                    Book book = new Book(book_id, title, author, isbn, genre, language, description, publication_year,
                            image_path, average_rating, review_count);
    
                    allBook.add(book);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        public ObservableList<Book> getAllBook() {
            return allBook;
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

}
