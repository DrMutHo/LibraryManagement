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

public class DashboardController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Model.getInstance().getClient();

        headerSubtitle.setText("Welcome, " + client.getName() + "!");

        int borrowedBooksCount = Model.getInstance().getDatabaseDriver().getNumberOfBorrowedBooks(client.getClientId());
        borrowedBooks.setText(String.valueOf(borrowedBooksCount));

        int MAX_BORROW_LIMIT = 50;
        double progress = (double) borrowedBooksCount / MAX_BORROW_LIMIT;
        borrowedBooksProgress.setProgress(progress);

        Book favouriteBook = Model.getInstance().getDatabaseDriver().getClientFavouriteBook(client.getClientId());
        favBook.setText(favouriteBook != null ? favouriteBook.getTitle() : "N/A");

        String favouriteGenre = Model.getInstance().getDatabaseDriver().getClientFavouriteGenre(client.getClientId());
        favGenre.setText(favouriteGenre != null ? favouriteGenre : "N/A");

        fee.setText(String.format("$%.2f", client.getOutstandingFees()));

        // loadRecentActivities();

        loadTopBooks();

        loadBorrowingTrends();

        loadGenreTrends();
    }

    private void loadTopBooks() {
        String topBooksStr = Model.getInstance().getDatabaseDriver().getTopBooksForClient(client.getClientId(), 5);

        String[] records = topBooksStr.split("\n");

        for (String record : records) {
            if (record.trim().isEmpty()) {
                continue;
            }

            String[] fields = record.split("\\|");

            int bookId = Integer.parseInt(fields[0]);
            String author = fields[1];
            String imagePath = fields[2];
            double clientRating = Double.parseDouble(fields[3]);

            VBox bookCard = createBookCard(author, imagePath, clientRating);
            topBooksContainer.getChildren().add(bookCard);
        }
    }

    private VBox createBookCard(String author, String imagePath, double clientRating) {
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getStyleClass().add("book-card");
        vbox.setPrefWidth(120);
        vbox.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();
        Image image;
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                image = new Image(new FileInputStream(file));
            } else {
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is != null) {
                    image = new Image(is);
                } else {
                    image = new Image(getClass().getResourceAsStream("/resources/Images/default.png"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            image = new Image(getClass().getResourceAsStream("/resources/Images/default.png"));
        }
        imageView.setImage(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label authorLabel = new Label("Author: " + author);
        authorLabel.getStyleClass().add("book-info");

        HBox ratingBar = new HBox();
        ratingBar.getStyleClass().add("book-rating");
        ratingBar.setAlignment(Pos.CENTER);
        ratingBar.setSpacing(5);

        TextFlow ratingTextFlow = new TextFlow();

        Text ratingText = new Text(clientRating > 0 ? String.format("%.1f ", clientRating) : "No Rating ");
        ratingText.setStyle("-fx-fill: white;");

        Text starText = new Text("★");
        starText.setStyle("-fx-fill: gold; -fx-font-weight: bold; -fx-font-size: 14;");

        ratingTextFlow.getChildren().addAll(ratingText, starText);

        ratingBar.getChildren().add(ratingTextFlow);

        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("image-container");
        imageContainer.getChildren().addAll(imageView, ratingBar);

        StackPane.setAlignment(ratingBar, Pos.BOTTOM_CENTER);

        imageContainer.setOnMouseEntered(event -> ratingBar.setVisible(true));
        imageContainer.setOnMouseExited(event -> ratingBar.setVisible(false));
        ratingBar.setVisible(false);

        vbox.getChildren().addAll(imageContainer, authorLabel);

        return vbox;
    }

    private void loadBorrowingTrends() {
        Map<String, Integer> monthlyTrends = Model.getInstance().getDatabaseDriver()
                .getMonthlyBorrowingTrends(client.getClientId());

        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        lineSeries.setName("Books Borrowed");

        for (Map.Entry<String, Integer> entry : monthlyTrends.entrySet()) {
            lineSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        borrowingTrendsLineChart.getData().add(lineSeries);
    }

    private void loadGenreTrends() {
        Map<String, Integer> genreTrends = Model.getInstance().getDatabaseDriver()
                .getBorrowingTrendsByCategory(client.getClientId());

        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        barSeries.setName("Books Borrowed");

        for (Map.Entry<String, Integer> entry : genreTrends.entrySet()) {
            barSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

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
