package main.Controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class DashboardController {
    @FXML
    private LineChart<String, Number> borrowingTrendsLineChart;

    @FXML
    private BarChart<String, Number> borrowingTrendsBarChart;

    public void initialize() {
        populateCharts();
    }

    private void populateCharts() {
        // Sample data
        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        lineSeries.setName("Borrowing Trends");

        lineSeries.getData().add(new XYChart.Data<>("Jan", 20));
        lineSeries.getData().add(new XYChart.Data<>("Feb", 35));
        lineSeries.getData().add(new XYChart.Data<>("Mar", 50));
        lineSeries.getData().add(new XYChart.Data<>("Apr", 40));
        lineSeries.getData().add(new XYChart.Data<>("May", 45));

        borrowingTrendsLineChart.getData().add(lineSeries);

        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        barSeries.setName("Borrowing Trends");

        barSeries.getData().add(new XYChart.Data<>("Jan", 20));
        barSeries.getData().add(new XYChart.Data<>("Feb", 35));
        barSeries.getData().add(new XYChart.Data<>("Mar", 50));
        barSeries.getData().add(new XYChart.Data<>("Apr", 40));
        barSeries.getData().add(new XYChart.Data<>("May", 45));

        borrowingTrendsBarChart.getData().add(barSeries);
    }
}