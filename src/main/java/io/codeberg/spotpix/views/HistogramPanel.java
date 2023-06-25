package io.codeberg.spotpix.views;

import java.util.ArrayList;

import javax.swing.JFrame;

import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import io.codeberg.spotpix.model.Color;

public class HistogramPanel extends ChartPanel {

    

    private HistogramPanel(JFreeChart chart) {
        super(chart);
    }

    public static void createHistogram(int[] quantizationMap, final ArrayList<Color> colorMap) {
        JFrame histogramFrame=new JFrame("Color Histogram");
        histogramFrame.setSize(500, 500);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < quantizationMap.length; i++) {
            dataset.addValue(quantizationMap[i], Integer.toString(i), "");
        }
        JFreeChart chart = ChartFactory.createBarChart("Color Histogram", "Color", "Occurances",
                dataset, PlotOrientation.HORIZONTAL, false, false, false);

        CategoryItemRenderer itemRenderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(final int row, final int column) {
                return new java.awt.Color(colorMap.get(row).getRed(), colorMap.get(row).getGreen(),
                        colorMap.get(row).getBlue(), colorMap.get(row).getAlpha());
            }
        };
        chart.getCategoryPlot().setRenderer(0, itemRenderer);
        HistogramPanel histogramPanel=new HistogramPanel(chart);
        histogramFrame.add(histogramPanel);
        histogramFrame.setLocationRelativeTo(null);
        histogramFrame.setVisible(true);
    }

    public void reset() {
        setChart(null);
    }
}
