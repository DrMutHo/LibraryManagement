// package main.Controllers.Admin;
// import java.awt.print.Book;
// import java.net.URL;
// import java.sql.*;
// import java.util.ResourceBundle;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.fxml.FXML;
// import javafx.fxml.Initializable;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.cell.PropertyValueFactory;

// import javax.swing.table.TableColumn;
// import javax.swing.text.TableView;
// import main.Models.DatabaseDriver;
// import main.Models.booksearch;

// public class BooksController implements Initializable{
// @FXML
// private TableView<booksearch> tableview;
// @FXML
// private TableColumn<Book, Integer> bookid;
// @FXML
// private TableColumn<Book, String> title;
// @FXML
// private TableColumn<Book, String> author;
// @FXML
// private TableColumn<Book, String> isbn;
// @FXML
// private TableColumn<Book, String> genre;
// @FXML
// private TableColumn<Book, String> language;
// @FXML
// private TableColumn<Book, String> description;
// @FXML
// private TableColumn<Book, Integer> publication_year;
// @FXML
// private TableColumn<Book, String> image_url;

// ObservableList<booksearch> booklists = FXCollections.observableArrayList();

// @Override
// public void initialize(URL url, ResourceBundle resource){
// DatabaseDriver connects = new DatabaseDriver();
// Connection connectdb = connects.getConnection();
// String viewquery = "SELECT book_id, title, author, isbn, genre, language,
// description, publication_year, image_url FROM book";

// try {
// Statement statement = connectdb.createStatement();
// ResultSet queryout = statement.executeQuery(viewquery);
// while(queryout.next()){
// Integer queryid = queryout.getInt("book_id");
// String querytitle = queryout.getString("title");
// String queyauthor = queryout.getString("author");
// String queryisbn = queryout.getString("isbn");
// String querygenre = queryout.getString("genre");
// String querylan = queryout.getString("language");
// String querydes = queryout.getString("description");
// Integer queryyear = queryout.getInt("publication_year");
// String queryurl = queryout.getString("image_url");
// booklists.add(new booksearch(queryid, querytitle, queyauthor, queryisbn,
// querygenre, querylan, querydes, queryyear, queryurl));
// bookid.setCellValueFactory(new PropertyValueFactory<>("book_id"));
// title.setCellValueFactory(new PropertyValueFactory<>("title"));
// author.setCellValueFactory(new PropertyValueFactory<>("author"));
// isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
// genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
// language.setCellValueFactory(new PropertyValueFactory<>("language"));
// description.setCellValueFactory(new PropertyValueFactory<>("description"));
// publication_year.setCellValueFactory(new
// PropertyValueFactory<>("publication_year"));
// image_url.setCellValueFactory(new PropertyValueFactory<>("image_url"));

// tableview.setItems(booklists);
// }
// } catch (SQLException e) {
// e.printStackTrace();
// }

// }

// }
