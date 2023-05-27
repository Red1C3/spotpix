package io.codeberg.spotpix.views;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ViewerRoot extends JFrame {
    private static ViewerRoot viewerRoot=new ViewerRoot();
    private final static int INITIAL_WIDTH = 640;
    private final static int INITAIL_HEIGHT = 480;
    private final static String TITLE = "Spotpix";

    public final JSplitPane splitPane;
    public final RGBHistogramPanel[] histogramPanels;

    private ViewerRoot() {
        super(TITLE);
        histogramPanels = new RGBHistogramPanel[3];

        JPanel histoPallet = new JPanel(new GridLayout(3, 1));
        // histoPallet.add(PalletView.instance());
        // histoPallet.add(HistogramPanel.instance());
        for (int i = 0; i < 3; i++) {
            histogramPanels[i] = new RGBHistogramPanel(null);
            histoPallet.add(histogramPanels[i]);
        }

        histoPallet.setPreferredSize(new Dimension(150, HEIGHT));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, histoPallet, ImageViewPanel.instance());
        add(splitPane);
        setJMenuBar(new ViewerMenuBar(this));

        setSize(INITIAL_WIDTH, INITAIL_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static ViewerRoot instance(){
        return viewerRoot;
    }
}
