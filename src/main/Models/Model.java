package main.Models;

import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Views.ViewFactory;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private boolean signupSuccessFlag; 
    private final DatabaseDriver databaseDriver;
    private final ObservableList<Book> allBook;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.signupSuccessFlag = false;
        this.databaseDriver = new DatabaseDriver();
        this.allBook = FXCollections.observableArrayList();
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
                int publication_year = resultSet.getInt("publication_year");
                String description = resultSet.getString("description");
                String image_url = resultSet.getString("image_url");

                Book book = new Book(book_id, title, author, isbn, genre, language, publication_year, description,
                        image_url);

                // Thêm Book vào ObservableList
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
}
