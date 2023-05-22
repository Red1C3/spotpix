package io.codeberg.spotpix.views;

import java.util.ArrayList;
import java.awt.Graphics;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.SimpleHistogramDataset;

import io.codeberg.spotpix.model.Color;

public class HistogramPanel extends ChartPanel {
    private static HistogramPanel histogramPanel;
    private JFreeChart chart;
    public static HistogramPanel instance(){
        if(histogramPanel==null) histogramPanel=new HistogramPanel(null);
        return histogramPanel;
    }
    private HistogramPanel(JFreeChart chart){
        super(chart);
    }
    public void createHistogram(int[] quantizationMap,ArrayList<Color> colorMap){
        SimpleHistogramDataset dataset=new SimpleHistogramDataset(null);

        for (int quantization : quantizationMap) {
            dataset.addObservation(quantization);
        }
        chart=ChartFactory.createHistogram("Color Histogram", "Colors", "Occurances", dataset);
        repaint();
    }
}
