package main.Controllers.Admin;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import main.Models.Client;
import main.Models.Model;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller class for managing clients in the admin panel.
 * Handles displaying, searching, and exporting client data.
 */
public class AdminClientController implements Initializable {

    /** TableView to display clients */
    @FXML
    private TableView<Client> clientTable;

    /** TableColumn for client ID */
    @FXML
    private TableColumn<Client, Integer> clientIdColumn;

    /** TableColumn for client name */
    @FXML
    private TableColumn<Client, String> nameColumn;

    /** TableColumn for library card number */
    @FXML
    private TableColumn<Client, String> libraryCardNumberColumn;

    /** TableColumn for email */
    @FXML
    private TableColumn<Client, String> emailColumn;

    /** TableColumn for phone number */
    @FXML
    private TableColumn<Client, String> phoneNumberColumn;

    /** TableColumn for address */
    @FXML
    private TableColumn<Client, String> addressColumn;

    /** TableColumn for registration date */
    @FXML
    private TableColumn<Client, LocalDate> registrationDateColumn;

    /** TableColumn for outstanding fees */
    @FXML
    private TableColumn<Client, Double> outstandingFeesColumn;

    /** TableColumn for username */
    @FXML
    private TableColumn<Client, String> usernameColumn;

    /** TextField for searching clients */
    @FXML
    private TextField searchField;

    /** ObservableList containing client data */
    private ObservableList<Client> clients;

    /** FilteredList for client search functionality */
    private FilteredList<Client> filteredData;
    private SortedList<Client> sortedData;

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the columns in the table
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        libraryCardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("libraryCardNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        outstandingFeesColumn.setCellValueFactory(new PropertyValueFactory<>("outstandingFees"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Load initial data into the table
        clients = Model.getInstance().getDatabaseDriver().getAllClients();
        filteredData = new FilteredList<>(clients, p -> true);
        sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator
        sortedData.comparatorProperty().bind(clientTable.comparatorProperty());

        // Set the data in the table
        clientTable.setItems(sortedData);

        // Add listener to search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(client -> {
                // If filter text is empty, display all clients
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare name, library card number, email, and username with filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if (client.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches client name
                } else if (client.getLibraryCardNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches library card number
                } else if (client.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches email
                } else if (client.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches username
                }
                return false; // Does not match
            });
        });
    }

    /**
     * Exports client data to an Excel file.
     * Opens a directory chooser for the user to select the save location.
     */
    @FXML
    private void Excell() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory to Save File");

        // Open the directory chooser and get the selected directory
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            try {
                // Create the file path (file name can be fixed or derived from data)
                String filePath = selectedDirectory.getAbsolutePath() + "/clients.xlsx";

                // Call method to export data to the selected file
                Model.getInstance().getDatabaseDriver().exportClientsToExcel(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error exporting to Excel: " + e.getMessage());
            }
        } else {
            System.out.println("No directory selected.");
        }
    }
    
    /**
     * Displays an alert dialog with the specified information.
     *
     * @param alertType The type of alert.
     * @param title     The title of the alert dialog.
     * @param header    The header text.
     * @param content   The content text.
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
