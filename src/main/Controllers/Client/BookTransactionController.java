package main.Controllers.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Models.Book;
import main.Models.BookTransaction;
import main.Models.Model;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookTransactionController {

    @FXML
    private TableView<BookTransaction> transactionTable;

    @FXML
    private TableColumn<BookTransaction, Integer> transactionIdColumn;
    @FXML
    private TableColumn<BookTransaction, String> titleColumn;
    @FXML
    private TableColumn<BookTransaction, Integer> copyIdColumn;
    @FXML
    private TableColumn<BookTransaction, LocalDate> borrowDateColumn;
    @FXML
    private TableColumn<BookTransaction, LocalDate> returnDateColumn;
    @FXML
    private TableColumn<BookTransaction, String> statusColumn;
    @FXML
    private ImageView bookImageView;
    @FXML
    private Label labelTitle, labelAuthor, labelISBN, labelGenre, labelLanguage, labelPublicationYear,
            labelAverageRating, labelReviewCount;
    @FXML
    private TextArea textDescription;
    private ObservableList<BookTransaction> transactionData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        Model.getInstance().setBookTransaction();
        // Khởi tạo các cột của bảng với thuộc tính của lớp BookTransaction
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        copyIdColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Nạp dữ liệu mẫu vào bảng
        loadDataFromDatabase();
        transactionTable.setItems(transactionData);
    }

    private void loadDataFromDatabase() {
        try {
            transactionData = Model.getInstance().getBookTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
