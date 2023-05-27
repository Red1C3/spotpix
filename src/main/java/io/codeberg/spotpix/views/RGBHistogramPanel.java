package io.codeberg.spotpix.views;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class RGBHistogramPanel extends ChartPanel {
    private RGBHistogramPanel(JFreeChart chart){
        super(chart);
    }

    public static RGBHistogramPanel creatRGBHistogramPanel(String channelName,int[] channel){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < channel.length; i++) {
            dataset.addValue(channel[i], Integer.toString(i), "");
        }
        JFreeChart chart=ChartFactory.createLineChart(channelName, channelName, "Occurances", dataset);

        return new RGBHistogramPanel(chart);
    }
    
}
