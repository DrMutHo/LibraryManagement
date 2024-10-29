package main.Controllers.Client;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class DashboardController implements Initializable {

    @FXML
    private PieChart pieChart;

    @FXML
    private Button btnBorrowReturn;

    @FXML
    private Button btnBorrowingBooks;

    @FXML
    private Button btnBorrowedBooks;

    @FXML
    private Button btnOverdueBooks;

    @FXML
    private Label timeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(formatter));
        }));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
        PieChart.Data borrowingBooks = new PieChart.Data("Borrowing Books", 5);
        PieChart.Data borrowedBooks = new PieChart.Data("Borrowed Books", 30);
        PieChart.Data overdueBooks = new PieChart.Data("Overdue Books", 2);

        pieChart.getData().addAll(borrowingBooks, borrowedBooks, overdueBooks);

        for (PieChart.Data data : pieChart.getData()) {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getName() + ": " + (int) data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);

            data.getNode().setOnMouseEntered(e -> {
                data.getNode().setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
            });
        }

        btnBorrowReturn.setOnAction(e -> System.out.println("Manage Borrow/Return clicked"));

    }
}