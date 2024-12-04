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

public class AdminClientController implements Initializable {

    @FXML
    private TableView<Client> clientTable;

    @FXML
    private TableColumn<Client, Integer> clientIdColumn;

    @FXML
    private TableColumn<Client, String> nameColumn;

    @FXML
    private TableColumn<Client, String> libraryCardNumberColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, String> phoneNumberColumn;

    @FXML
    private TableColumn<Client, String> addressColumn;

    @FXML
    private TableColumn<Client, LocalDate> registrationDateColumn;

    @FXML
    private TableColumn<Client, Double> outstandingFeesColumn;

    @FXML
    private TableColumn<Client, String> usernameColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Client> clients;

    private FilteredList<Client> filteredData;
    private SortedList<Client> sortedData;

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


    @FXML
    private void Excell() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chọn Thư Mục Lưu Tệp");

        // Mở cửa sổ chọn thư mục và lấy thư mục người dùng chọn
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            try {
                // Tạo đường dẫn tệp (tên tệp có thể cố định hoặc lấy từ dữ liệu)
                String filePath = selectedDirectory.getAbsolutePath() + "/clients.xlsx";

                // Gọi hàm export dữ liệu vào file đã chọn
                Model.getInstance().getDatabaseDriver().exportClientsToExcel(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error exporting to Excel: " + e.getMessage());
            }
        } else {
            System.out.println("No directory selected.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
