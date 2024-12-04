package main.Controllers.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import main.Models.Client;
import main.Models.Model;
import main.Models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javafx.scene.text.Text;

/**
 * Controller class for managing the dashboard UI in the client view.
 * Handles user interaction with borrowed books, borrowing trends,
 * top books, and genre trends.
 */
public class DashboardController implements Initializable, Model.ModelListenerClient {

    @FXML
    private Label headerSubtitle;

    @FXML
    private Label borrowedBooks;

    @FXML
    private ProgressBar borrowedBooksProgress;

    @FXML
    private Label favBook;

    @FXML
    private Label favGenre;

    @FXML
    private Label fee;

    // @FXML
    // private ListView<String> recentActivityList;

    @FXML
    private LineChart<String, Number> borrowingTrendsLineChart;

    @FXML
    private BarChart<String, Number> borrowingTrendsBarChart;

    @FXML
    private HBox topBooksContainer;

    private Client client;

    /**
     * Initializes the dashboard by setting the client data,
     * loading the borrowed books information, trends, and top books.
     * 
     * @param location  the URL location of the FXML file
     * @param resources the resource bundle for localization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Model.getInstance().getClient();

        headerSubtitle.setText("Welcome, " + client.getName() + "!");

        // loadRecentActivities();

        refreshBorrowedBooksInfo();

        loadTopBooks();

        loadBorrowingTrends();

        loadGenreTrends();

        Model.getInstance().addListener(this);
    }

    /**
     * Callback method triggered when a new borrow transaction is created.
     * Updates the borrowed books information, trends, and top books.
     */
    @Override
    public void onBorrowTransactionClientCreated() {
        refreshBorrowedBooksInfo();
        loadBorrowingTrends();
        loadGenreTrends();
        loadTopBooks();
    }

    /**
     * Refreshes the information displayed for the borrowed books section.
     * Updates the count of borrowed books, progress bar, favorite book,
     * favorite genre, and outstanding fees for the client.
     */
    private void refreshBorrowedBooksInfo() {
        // Fetch the number of borrowed books for the client.
        int borrowedBooksCount = Model.getInstance().getDatabaseDriver().getNumberOfBorrowedBooks(client.getClientId());
        borrowedBooks.setText(String.valueOf(borrowedBooksCount));

        // Calculate and update the progress bar based on the borrowed books count.
        int MAX_BORROW_LIMIT = 50;
        double progress = (double) borrowedBooksCount / MAX_BORROW_LIMIT;
        borrowedBooksProgress.setProgress(progress);

        // Fetch and update the favorite book of the client.
        Book favouriteBook = Model.getInstance().getDatabaseDriver().getClientFavouriteBook(client.getClientId());
        favBook.setText(favouriteBook != null ? favouriteBook.getTitle() : "N/A");

        // Fetch and update the favorite genre of the client.
        String favouriteGenre = Model.getInstance().getDatabaseDriver().getClientFavouriteGenre(client.getClientId());
        favGenre.setText(favouriteGenre != null ? favouriteGenre : "N/A");

        // Display the client's outstanding fees in a formatted manner.
        fee.setText(String.format("$%.2f", client.getOutstandingFees()));
    }


    /**
     * Loads and displays the top books for the client on the dashboard.
     * Fetches the top 5 books from the database, parses the data, 
     * and creates a visual card for each book to be displayed.
     */
    private void loadTopBooks() {
        // Fetch the top books data as a string from the database.
        String topBooksStr = Model.getInstance().getDatabaseDriver().getTopBooksForClient(client.getClientId(), 5);

        // Split the fetched data into individual book records.
        String[] records = topBooksStr.split("\n");

        // Iterate through each book record and process it.
        for (String record : records) {
            // Skip empty or blank records.
            if (record.trim().isEmpty()) {
                continue;
            }

            // Split each record into its individual fields.
            String[] fields = record.split("\\|");

            // Parse the book's data from the fields.
            int bookId = Integer.parseInt(fields[0]); // Book ID
            String author = fields[1];                // Author's name
            String imagePath = fields[2];             // Path to the book's image
            double clientRating = Double.parseDouble(fields[3]); // Client's rating for the book

            // Create a card UI component for the book.
            VBox bookCard = createBookCard(author, imagePath, clientRating);

            // Add the created book card to the UI container.
            topBooksContainer.getChildren().add(bookCard);
        }
    }


    /**
     * Creates a UI card for displaying book information, including the author's name, 
     * book image, and client rating.
     * The card is displayed as a `VBox` containing an image, author label, and rating.
     * 
     * @param author     the author of the book
     * @param imagePath  the path to the book's image
     * @param clientRating the rating provided by the client for the book
     * @return a `VBox` containing the book card UI components
     */
    private VBox createBookCard(String author, String imagePath, double clientRating) {
        // Create a VBox container for the book card UI
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getStyleClass().add("book-card");
        vbox.setPrefWidth(120);
        vbox.setAlignment(Pos.CENTER);

        // Create an ImageView for the book image
        ImageView imageView = new ImageView();
        Image image;
        try {
            // Try loading the image from the given path
            File file = new File(imagePath);
            if (file.exists()) {
                image = new Image(new FileInputStream(file));
            } else {
                // If the file doesn't exist, try loading from the resources
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is != null) {
                    image = new Image(is);
                } else {
                    // If no image is found, use a default image
                    image = new Image(getClass().getResourceAsStream("/resources/Images/default.png"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // In case of error, use a default image
            image = new Image(getClass().getResourceAsStream("/resources/Images/default.png"));
        }
        imageView.setImage(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        // Create a label for the author name
        Label authorLabel = new Label("Author: " + author);
        authorLabel.getStyleClass().add("book-info");

        // Create a horizontal box for the rating display
        HBox ratingBar = new HBox();
        ratingBar.getStyleClass().add("book-rating");
        ratingBar.setAlignment(Pos.CENTER);
        ratingBar.setSpacing(5);

        // Create a TextFlow to show the rating and star symbol
        TextFlow ratingTextFlow = new TextFlow();

        // Display the rating or "No Rating" if the rating is zero
        Text ratingText = new Text(clientRating > 0 ? String.format("%.1f ", clientRating) : "No Rating ");
        ratingText.setStyle("-fx-fill: white;");

        // Display the star symbol for the rating
        Text starText = new Text("★");
        starText.setStyle("-fx-fill: gold; -fx-font-weight: bold; -fx-font-size: 14;");

        // Add the rating and star text to the TextFlow
        ratingTextFlow.getChildren().addAll(ratingText, starText);

        // Add the rating flow to the rating bar
        ratingBar.getChildren().add(ratingTextFlow);

        // Create a StackPane to hold the image and rating bar
        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("image-container");
        imageContainer.getChildren().addAll(imageView, ratingBar);

        // Position the rating bar at the bottom of the image
        StackPane.setAlignment(ratingBar, Pos.BOTTOM_CENTER);

        // Add mouse events to show/hide the rating bar
        imageContainer.setOnMouseEntered(event -> ratingBar.setVisible(true));
        imageContainer.setOnMouseExited(event -> ratingBar.setVisible(false));
        ratingBar.setVisible(false);

        // Add the image container and author label to the VBox
        vbox.getChildren().addAll(imageContainer, authorLabel);

        return vbox;
    }


    /**
     * Loads and displays the borrowing trends for the client on the line chart.
     * Fetches monthly borrowing data for the client and populates the line chart
     * with the data to visually represent the trends.
     */
    private void loadBorrowingTrends() {
        // Fetch the monthly borrowing trends data for the client from the database.
        Map<String, Integer> monthlyTrends = Model.getInstance().getDatabaseDriver()
                .getMonthlyBorrowingTrends(client.getClientId());

        // Create a new series for the line chart.
        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        lineSeries.setName("Books Borrowed");

        // Add the fetched data to the series.
        for (Map.Entry<String, Integer> entry : monthlyTrends.entrySet()) {
            lineSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the series to the line chart to display the trends.
        borrowingTrendsLineChart.getData().add(lineSeries);
    }


    /**
     * Loads and displays the borrowing trends by genre for the client on the bar chart.
     * Fetches borrowing data categorized by genre from the database and populates 
     * the bar chart to visually represent the trends for each genre.
     */
    private void loadGenreTrends() {
        // Fetch the borrowing trends by genre for the client from the database.
        Map<String, Integer> genreTrends = Model.getInstance().getDatabaseDriver()
                .getBorrowingTrendsByCategory(client.getClientId());

        // Create a new series for the bar chart.
        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        barSeries.setName("Books Borrowed");

        // Add the fetched data to the series.
        for (Map.Entry<String, Integer> entry : genreTrends.entrySet()) {
            barSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the series to the bar chart to display the genre trends.
        borrowingTrendsBarChart.getData().add(barSeries);
    }


    // private void loadRecentActivities() {
    // List<String> recentActivities = Model.getInstance().getDatabaseDriver()
    // .getClientRecentActivities(client.getClientId(), 10);

    // System.out.println("Recent Activities: " + recentActivities);
    // System.out.println("Is recentActivityList null? " + (recentActivityList ==
    // null));

    // if (recentActivityList != null) {
    // if (recentActivities != null && !recentActivities.isEmpty()) {
    // ObservableList<String> activities =
    // FXCollections.observableArrayList(recentActivities);
    // recentActivityList.setItems(activities);
    // } else {
    // recentActivityList.setItems(FXCollections.observableArrayList("No recent
    // activities."));
    // }
    // } else {
    // System.out.println("Error: recentActivityList is null.");
    // }
    // }
}
