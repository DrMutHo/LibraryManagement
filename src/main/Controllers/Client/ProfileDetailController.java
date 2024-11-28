package main.Controllers.Client;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import main.Models.Model;

public class ProfileDetailController implements Initializable {
    @FXML
    private TextField Email;

    @FXML
    private TextField libraryCardNumber;

    @FXML
    private TextField outstandingFees;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField registrationDate;

    @FXML
    private TextField userName;

    @FXML
    private TextField address;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        libraryCardNumber
                .setText(Model.getInstance().getClient().getLibraryCardNumber());
        userName.setText(Model.getInstance().getClient().getUsername());
        Email.setText(Model.getInstance().getClient().getEmail());
        phoneNumber.setText(Model.getInstance().getClient().getPhoneNumber());
        address.setText(Model.getInstance().getClient().getAddress());
        outstandingFees
                .setText(String.valueOf(Model.getInstance().getClient().getOutstandingFees()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(Model.getInstance().getClient().getRegistrationDate());
        registrationDate.setText(formattedDate);
    }
}
