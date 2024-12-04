package main.Controllers.Client;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import main.Models.Model;

/**
 * Controller for handling the profile details view in the client interface.
 * This controller is responsible for displaying the user's profile details such as 
 * username, email, phone number, address, registration date, library card number, 
 * and outstanding fees.
 */
public class ProfileDetailController implements Initializable {
    
    @FXML
    private TextField Email;  // TextField to display the user's email address

    @FXML
    private TextField libraryCardNumber;  // TextField to display the user's library card number

    @FXML
    private TextField outstandingFees;  // TextField to display the user's outstanding fees

    @FXML
    private TextField phoneNumber;  // TextField to display the user's phone number

    @FXML
    private TextField registrationDate;  // TextField to display the user's registration date

    @FXML
    private TextField userName;  // TextField to display the user's username

    @FXML
    private TextField address;  // TextField to display the user's address

    /**
     * Initializes the ProfileDetailController by loading the client's profile data 
     * into the corresponding TextFields.
     * 
     * @param url the location used to resolve relative paths for resources.
     * @param resourceBundle the resources used for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load client information from the Model and set it to the corresponding TextFields
        libraryCardNumber.setText(Model.getInstance().getClient().getLibraryCardNumber());
        userName.setText(Model.getInstance().getClient().getUsername());
        Email.setText(Model.getInstance().getClient().getEmail());
        phoneNumber.setText(Model.getInstance().getClient().getPhoneNumber());
        address.setText(Model.getInstance().getClient().getAddress());
        outstandingFees.setText(String.valueOf(Model.getInstance().getClient().getOutstandingFees()));
        
        // Format the registration date to a specific pattern and set it to the registrationDate field
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(Model.getInstance().getClient().getRegistrationDate());
        registrationDate.setText(formattedDate);
    }
}
