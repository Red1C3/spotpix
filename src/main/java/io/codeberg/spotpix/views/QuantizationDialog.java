package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;



public class QuantizationDialog extends JDialog {
    private final static String K_MEAN_STR = "K-Means";
    private final static String MEDIAN_CUT_STR = "Median Cut";
    private final static String AVG_STR = "Average Color";

    private JPanel  medianCutPanel, avgPanel;
    private KMeanPanel kMeanPanel;

    public QuantizationDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Quantization", ModalityType.APPLICATION_MODAL);
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        setupPanels();

        tabbedPane.add(kMeanPanel, K_MEAN_STR);
        tabbedPane.add(medianCutPanel, MEDIAN_CUT_STR);
        tabbedPane.add(avgPanel, AVG_STR);

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setupPanels() {
        kMeanPanel = new KMeanPanel();

        medianCutPanel = new JPanel();
        avgPanel = new JPanel();
    }
}


class KMeanPanel extends JPanel{
    public KMeanPanel(){
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT));

        
    }   
}