package io.codeberg.spotpix.views;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class RGBHistogramPanel extends ChartPanel {
    public RGBHistogramPanel(JFreeChart chart){
        super(chart);
    }

    public void setRGBHistogramPanel(String channelName,int[] channel){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < channel.length; i++) {
            dataset.addValue(channel[i], "", Integer.toString(i));
        }
        JFreeChart chart=ChartFactory.createLineChart(channelName, channelName, "Occurances", dataset,PlotOrientation.VERTICAL,false,false,false);
        CategoryPlot plot=(CategoryPlot) chart.getPlot();
        CategoryAxis axis=plot.getDomainAxis();
        axis.setTickLabelsVisible(false);

        setChart(chart);
    }
    
}
