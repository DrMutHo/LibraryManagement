package main.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Views.ViewFactory;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private boolean signupSuccessFlag;
    private final DatabaseDriver databaseDriver;
    private final ObservableList<Book> allBook;
    private final ObservableList<BookTransaction> bookTransactions;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.signupSuccessFlag = false;
        this.databaseDriver = new DatabaseDriver();
        this.allBook = FXCollections.observableArrayList();
        this.bookTransactions = FXCollections.observableArrayList();
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

    public boolean getSignupSuccessFlag() {
        return this.signupSuccessFlag;
    }

    public void setSignupSuccessFlag(boolean flag) {
        this.signupSuccessFlag = flag;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
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

                // Thêm Book vào ObservableList
                allBook.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBookTransaction() {
        ResultSet resultSet = databaseDriver.getTransactionByClientID(1);
        try {
            while (resultSet.next()) {
                int transactionId = resultSet.getInt("transaction_id");
                String title = resultSet.getString("title");
                int copyId = resultSet.getInt("copy_id");
                LocalDate borrowDate = resultSet.getDate("borrow_date").toLocalDate();
                LocalDate returnDate = resultSet.getDate("return_date") != null
                        ? resultSet.getDate("return_date").toLocalDate()
                        : null;
                String status = resultSet.getString("status");

                // Tạo đối tượng BookTransaction và thêm vào danh sách
                BookTransaction transaction = new BookTransaction(transactionId, title, copyId, borrowDate, returnDate,
                        status);
                bookTransactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<BookTransaction> getBookTransaction() {
        return bookTransactions;
    }

    public ObservableList<Book> getAllBook() {
        return allBook;
    }

}
