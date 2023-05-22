package io.codeberg.spotpix.views;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
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

    public void createHistogram(int[] quantizationMap,final ArrayList<Color> colorMap) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < quantizationMap.length; i++) {
            dataset.addValue(quantizationMap[i], Integer.toString(i), "Color");
        }
        chart = ChartFactory.createBarChart("Color Histogram", "Color", "Occurances",
                dataset, PlotOrientation.HORIZONTAL, false, false, false);

        CategoryItemRenderer itemRenderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(final int row, final int column) {
                return new java.awt.Color(colorMap.get(row).getRed(), colorMap.get(row).getGreen(),
                        colorMap.get(row).getBlue(), colorMap.get(row).getAlpha());
            }
        };
        chart.getCategoryPlot().setRenderer(0, itemRenderer);
        setChart(chart);
        repaint();
    }
}
