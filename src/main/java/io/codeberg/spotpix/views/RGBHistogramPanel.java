package io.codeberg.spotpix.views;

import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class RGBHistogramPanel extends ChartPanel {
    public RGBHistogramPanel(JFreeChart chart) {
        super(chart);
    }

    public void setRGBHistogramPanel(final String channelName, int[] channel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < channel.length; i++) {
            dataset.addValue(channel[i], "", Integer.toString(i));
        }
        JFreeChart chart = ChartFactory.createLineChart(channelName, channelName, "Occurances", dataset,
                PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis axis = plot.getDomainAxis();
        axis.setTickLabelsVisible(false);
        CategoryItemRenderer renderer = new LineAndShapeRenderer() {
            @Override
            public Paint getItemPaint(final int row, final int column) {
                switch (channelName.toLowerCase()) {
                    case "red":
                        return java.awt.Color.RED;
                    case "green":
                        return java.awt.Color.GREEN;
                    case "blue":
                        return java.awt.Color.BLUE;
                }
                return java.awt.Color.BLACK;
            }
        };
        chart.getCategoryPlot().setRenderer(0, renderer);

        setChart(chart);
    }

}
