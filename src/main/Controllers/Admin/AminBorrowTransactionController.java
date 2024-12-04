package main.Controllers.Admin;

import java.io.File;

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
import javafx.stage.DirectoryChooser;
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

/**
 * Controller class for managing borrow transactions in the admin panel.
 * Handles displaying and searching, and processing the return of borrowed books.
 */
public class AminBorrowTransactionController implements Initializable {

    /** TextField for entering search queries */
    @FXML
    private TextField searchField;

    /** TableView to display borrow transactions */
    @FXML
    private TableView<BorrowTransaction> bookTable;

    /** TableColumn for transaction ID */
    @FXML
    private TableColumn<BorrowTransaction, Integer> transactionIdColumn;
    /** TableColumn for book title */
    @FXML
    private TableColumn<BorrowTransaction, String> titleColumn;
    /** TableColumn for copy ID */
    @FXML
    private TableColumn<BorrowTransaction, Integer> copyIdColumn;
    /** TableColumn for borrow date */
    @FXML
    private TableColumn<BorrowTransaction, LocalDate> borrowDateColumn;
    /** TableColumn for return date */
    @FXML
    private TableColumn<BorrowTransaction, LocalDate> returnDateColumn;
    /** TableColumn for status */
    @FXML
    private TableColumn<BorrowTransaction, String> statusColumn;

    /** Button to process the return of selected books */
    @FXML
    private Button returnButton;

    /** ChoiceBox for selecting actions */
    @FXML 
    private ChoiceBox actionChoiceBox;

    /** FilteredList for searching transactions */
    private FilteredList<BorrowTransaction> filteredData;
    /** SortedList for sorting transactions */
    private SortedList<BorrowTransaction> sortedData;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the table columns, loads data, and initializes search functionality.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
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

        bookTable.getColumns().add(selectColumn);

        selectColumn.setCellValueFactory(new PropertyValueFactory<BorrowTransaction, String>("selected"));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(transaction -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                String transactionid = String.valueOf(transaction.getTransactionId());
                String clientid = String.valueOf(transaction.getClientId());

                if (transaction.getTitle().toLowerCase().contains(lowerCaseFilter)
                    || transaction.getStatus().toLowerCase().contains(lowerCaseFilter)
                    || transactionid.contains(lowerCaseFilter)
                    || clientid.contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        filteredData = new FilteredList<>(Model.getInstance().getDatabaseDriver().getAllBorrowTransactionsList(),
                p -> true);

        // Enable sorting
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bookTable.comparatorProperty());
        bookTable.setItems(sortedData);
    }

    /**
     * Handles the search action (if needed).
     * Currently, search logic is handled by the textProperty listener on searchField.
     */
    @FXML
    private void onSearch() {
        // No changes required here, search logic already handled in textProperty
        // listener
    }

    /**
     * Handles the return button click event.
     * Processes the return of selected borrowed books.
     */
    @FXML
    private void onReturnButtonClick() {

        for (BorrowTransaction transaction : sortedData) {
            // Get the checkbox state from the model's selectedProperty

            if (transaction.getSelected().isSelected()) {
                // Update the status of selected rows to "Done"
                transaction.setStatus("Done");
                Model.getInstance().getDatabaseDriver()
                        .ProcessReturnBook(transaction.getTransactionId());
            }
        }
        // Refresh the table to reflect the changes
        bookTable.refresh();
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
    
    /**
     * Exports borrow transactions data to an Excel file.
     * Opens a directory chooser for the user to select the save location.
     */
    @FXML
    private void ExporttoExcel() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory to Save File");

        // Open the directory chooser and get the selected directory
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            try {
                // Create the file path (file name can be fixed or derived from data)
                String filePath = selectedDirectory.getAbsolutePath() + "/borrow_transactions.xlsx";

                // Call method to export data to the selected file
                Model.getInstance().getDatabaseDriver().exportAllBorrowTransactionsToExcel(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error exporting to Excel: " + e.getMessage());
            }
        } else {
            System.out.println("No directory selected.");
        }
    }
}
