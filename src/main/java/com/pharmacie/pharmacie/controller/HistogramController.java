package com.pharmacie.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class HistogramController {

    @FXML
    private BarChart<String, Number> histogramChart;

    public void setHistogramData(XYChart.Series<String, Number> data) {
        histogramChart.getData().clear();
        histogramChart.getData().add(data);
    }
}
