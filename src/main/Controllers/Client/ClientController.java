package main.Controllers.Client;

import main.Models.Book;
import main.Models.Model;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class ClientController implements Initializable {

    public BorderPane client_parent;

    private boolean isInBookDetails = false;
    private Book selectedBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setClientController(this);

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

        Model.getInstance().getBookSelectionListener().addListener((observable, oldBook, newBook) -> {
            if (newBook != null) {
                openBookDetailsView(newBook);
                System.out.println(newBook.getTitle());

            }
        });
    }

    public void openBookDetailsView(Book book) {
        selectedBook = book;
        isInBookDetails = true;
        client_parent.setCenter(Model.getInstance().getViewFactory().getBookDetailsView(book));
    }

    public void goBackToBrowsing() {
        isInBookDetails = false;
        client_parent.setCenter(Model.getInstance().getViewFactory().getBrowsingView());
    }

    public void goBackToTransaction() {
        isInBookDetails = false;
        client_parent.setCenter(Model.getInstance().getViewFactory().getBorrowTransactionView());
    }

    public void goBackToHome() {
        isInBookDetails = false;
        client_parent.setCenter(Model.getInstance().getViewFactory().getHomeView());
    }
}
