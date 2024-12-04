package main.Controllers.Client;

import main.Models.Book;
import main.Models.Model;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 * Controller for managing the main client view, which includes navigating
 * between different sections of the application such as Home, Profile, Browsing,
 * Notifications, Borrow Transactions, and Book Details.
 */
public class ClientController implements Initializable {

    @FXML
    public BorderPane client_parent;

    private boolean isInBookDetails = false;
    private Book selectedBook;

    /**
     * Initializes the client controller. Sets up listeners for client menu selection
     * and book selection events.
     * 
     * This method is called when the FXML view is loaded.
     * 
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the view.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setClientController(this);

        // Listener for menu item selection changes
        Model.getInstance().getViewFactory().getClientSelectedMenuItem()
                .addListener((observableValue, oldVal, newVal) -> {
                    switch (newVal) {
                        case HOME ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getHomeView());
                        case PROFILE ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                        case BROWSING -> {
                            if (isInBookDetails) {
                                client_parent.setCenter(
                                        Model.getInstance().getViewFactory().getBookDetailsView(selectedBook));
                            } else {
                                client_parent.setCenter(Model.getInstance().getViewFactory().getBrowsingView());
                            }
                        }
                        case NOTIFICATION ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getNotiView());
                        case BORROWTRANSACTION ->
                            client_parent.setCenter(Model.getInstance().getViewFactory().getBorrowTransactionView());
                        default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    }
                });

        // Listener for book selection event
        Model.getInstance().getBookSelectionListener().addListener((observable, oldBook, newBook) -> {
            if (newBook != null) {
                openBookDetailsView(newBook);
                System.out.println(newBook.getTitle());
            }
        });
    }

    /**
     * Opens the book details view for the selected book.
     * 
     * @param book The {@code Book} to display in the details view.
     */
    public void openBookDetailsView(Book book) {
        selectedBook = book;
        isInBookDetails = true;
        client_parent.setCenter(Model.getInstance().getViewFactory().getBookDetailsView(book));
    }

    /**
     * Navigates back to the browsing view.
     */
    public void goBackToBrowsing() {
        isInBookDetails = false;
        client_parent.setCenter(Model.getInstance().getViewFactory().getBrowsingView());
    }

    /**
     * Navigates back to the borrow transaction view.
     */
    public void goBackToTransaction() {
        isInBookDetails = false;
        client_parent.setCenter(Model.getInstance().getViewFactory().getBorrowTransactionView());
    }

    /**
     * Navigates back to the home view.
     */
    public void goBackToHome() {
        isInBookDetails = false;
        client_parent.setCenter(Model.getInstance().getViewFactory().getHomeView());
    }
}


// package main.Controllers.Admin;

// import main.Models.Model;
// import java.net.URL;
// import java.util.ResourceBundle;

// import javafx.fxml.Initializable;
// import javafx.scene.layout.BorderPane;

// public class AdminController implements Initializable {

//     public BorderPane admin_parent;

//     @Override
//     public void initialize(URL url, ResourceBundle resourceBundle) {

//         Model.getInstance().getViewFactory().getAdminSelectedMenuItem()
//                 .addListener((observableValue, oldVal, newVal) -> {
//                     switch (newVal) {
//                         case HOME ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getHomeView());
//                         case PROFILE ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
//                         case BROWSING ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getBrowsingView());
//                         case NOTIFICATION ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getNotiView());
//                         case BOOKTRANSACTION ->
//                             admin_parent.setCenter(Model.getInstance().getViewFactory().getBookTransactionView());
//                         default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
//                     }
//                 });
//     }
// }
