package main.Controllers.Admin;

import java.awt.Checkbox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.Models.BorrowTransaction;
import main.Models.Model;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import main.Views.BorrowTransactionCheckBoxValueFactory;

public class AminBorrowTransactionController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<BorrowTransaction> bookTable;

     // Checkbox column
    @FXML
    private TableColumn<BorrowTransaction, Integer> transactionIdColumn;
    @FXML
    private TableColumn<BorrowTransaction, String> titleColumn;
    @FXML
    private TableColumn<BorrowTransaction, Integer> copyIdColumn;
    @FXML
    private TableColumn<BorrowTransaction, LocalDate> borrowDateColumn;
    @FXML
    private TableColumn<BorrowTransaction, LocalDate> returnDateColumn;
    @FXML
    private TableColumn<BorrowTransaction, String> statusColumn;

    @FXML
    private Button returnButton;
    private Button returnButton2;
    private Button ExportButton;

    private FilteredList<BorrowTransaction> filteredData;
    private SortedList<BorrowTransaction> sortedData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load data and set up table columns
        Model.getInstance().setBorrowTransaction();

        // Table column setup
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        copyIdColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        TableColumn selectColumn = new TableColumn<>(" ");
        TableColumn conditonColumn = new TableColumn<>("Condition");


        bookTable.getColumns().add(selectColumn);

        selectColumn.setCellValueFactory(new PropertyValueFactory<BorrowTransaction, String>("selected"));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(transaction -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
            String lowerCaseFilter = newValue.toLowerCase();

            if (transaction.getTitle().toLowerCase().contains(lowerCaseFilter)
                || transaction.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
            });
        });

        filteredData = new FilteredList<>(Model.getInstance().getDatabaseDriver().getAllBorrowTransactions(), p -> true);

        // Enable sorting
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedData);
    }

    // This method is used to handle search action (if needed)
    @FXML
    private void onSearch() {
        // No changes required here, search logic already handled in textProperty listener
    }

    // Handle the return button click event
    @FXML
    private void onReturnButtonClick() {
        
        for (BorrowTransaction transaction : sortedData) {
            // Get the checkbox state from the model's selectedPropert

            if (transaction.getSelected().isSelected()) {
                // Update the status of selected rows to "Returned"
                transaction.setStatus("Done");
                Model.getInstance().getDatabaseDriver()
                        .ProcessReturnBook(transaction.getTransactionId());
            }
        }
        // Refresh the table to reflect the changes
        bookTable.refresh();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
    
    @FXML 
    private void ExporttoExcel(){

    }
}
