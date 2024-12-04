package main.Controllers.Admin;

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
import main.Models.Admin;
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

public class AdminDashboardController implements Initializable {

    @FXML
    private Label headerSubtitle;

    @FXML
    private Label Books;

    @FXML
    private Label Clients;

    @FXML
    private Label BorrowedBooks;

    @FXML
    private Label Employee;

    // @FXML
    // private ListView<String> recentActivityList;

    @FXML
    private LineChart<String, Number> borrowingTrendsLineChart, totalFeesLineChart;

    // @FXML
    // private BarChart<String, Number> borrowingTrendsBarChart;

    private Admin admin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        admin = Model.getInstance().getAdmin();

        headerSubtitle.setText("Welcome, " + admin.getUsername() + "!");

        int books = Model.getInstance().getDatabaseDriver().getTotalNumberOfBooks();
        Books.setText(String.valueOf(books));

        int clients = Model.getInstance().getDatabaseDriver().getTotalNumberOfClients();
        Clients.setText(String.valueOf(clients));

        int employee = Model.getInstance().getDatabaseDriver().getTotalNumberOfEmployees();
        Employee.setText(String.valueOf(employee));

        int borrowedbooks = Model.getInstance().getDatabaseDriver().getTotalNumberOfCurrentlyBorrowedBooks();
        BorrowedBooks.setText(String.valueOf(borrowedbooks));

        loadBorrowingTrends();

        loadTotalFeesOverTime();
    }

    private void loadBorrowingTrends() {
        Map<String, Integer> monthlyTrends = Model.getInstance().getDatabaseDriver()
                .getMonthlyBorrowingTrendsForAllUsers();

        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        lineSeries.setName("Books Borrowed");

        for (Map.Entry<String, Integer> entry : monthlyTrends.entrySet()) {
            lineSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        borrowingTrendsLineChart.getData().add(lineSeries);
    }

    private void loadTotalFeesOverTime() {
        Map<String, Double> totalFeesOverTime = Model.getInstance().getDatabaseDriver()
                .getTotalFeesOverTimeForAllClients();

        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        lineSeries.setName("Total Fees");

        for (Map.Entry<String, Double> entry : totalFeesOverTime.entrySet()) {
            lineSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        totalFeesLineChart.getData().add(lineSeries);
    }

    // private void loadGenreTrends() {
    // Map<String, Integer> genreTrends = Model.getInstance().getDatabaseDriver()
    // .getBorrowingTrendsByCategoryForAllUsers();

    // XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
    // barSeries.setName("Books Borrowed");

    // for (Map.Entry<String, Integer> entry : genreTrends.entrySet()) {
    // barSeries.getData().add(new XYChart.Data<>(entry.getKey(),
    // entry.getValue()));
    // }

    // borrowingTrendsBarChart.getData().add(barSeries);
    // }

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
