package io.codeberg.spotpix.views;

import java.util.ArrayList;
import java.awt.Graphics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

import io.codeberg.spotpix.model.Color;

public class HistogramPanel extends ChartPanel {
    private static HistogramPanel histogramPanel;
    private JFreeChart chart;

    public static HistogramPanel instance() {
        if (histogramPanel == null)
            histogramPanel = new HistogramPanel(null);
        return histogramPanel;
    }

    private HistogramPanel(JFreeChart chart) {
        super(chart);
    }

    public void createHistogram(int[] quantizationMap, ArrayList<Color> colorMap) {
        HistogramDataset dataset = new HistogramDataset();
        int size=0;
        for(int i=0;i<quantizationMap.length;i++){
            size+=quantizationMap[i];
        }
        double[] series=new double[size];

        //dataset.addSeries("key", series, series.length,0,4);

        chart = ChartFactory.createHistogram("Color Histogram", "Colors", "Occurances", dataset);
        setChart(chart);
        repaint();
    }
}
