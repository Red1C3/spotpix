package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

public class QuantizationDialog extends JDialog {
    private final static String K_MEAN_STR = "K-Means";
    private final static String MEDIAN_CUT_STR = "Median Cut";
    private final static String AVG_STR = "Average Color";

    private JPanel medianCutPanel, avgPanel;
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
        setSize(300,100);
        setVisible(true);
        
    }

    private void setupPanels() {
        kMeanPanel = new KMeanPanel(this);

        medianCutPanel = new JPanel();
        avgPanel = new JPanel();
    }
}

class KMeanPanel extends JPanel implements ActionListener {
    private final static String ENTER_COLOR_STR = "The number of colors:";
    private final JFormattedTextField colorsCount;
    private final ButtonGroup colorSystem;
    private final JRadioButton rgbButton,labButton;
    private final JButton quantizeButton,cancelButton;
    private final QuantizationDialog quantizationDialog;

    public KMeanPanel(QuantizationDialog quantizationDialog) {
        super();
        this.quantizationDialog=quantizationDialog;
        setLayout(new GridLayout(3, 2));

        add(new JLabel(ENTER_COLOR_STR));

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);
        colorsCount = new JFormattedTextField(formatter);

        add(colorsCount);

        colorSystem = new ButtonGroup();
        rgbButton = new JRadioButton("RGB",true);
        labButton = new JRadioButton("Lab");

        colorSystem.add(rgbButton);
        colorSystem.add(labButton);
        add(rgbButton);
        add(labButton);

        quantizeButton=new JButton("Quantize");
        cancelButton=new JButton("Cancel");
        quantizeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        add(quantizeButton);
        add(cancelButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==cancelButton){
            quantizationDialog.dispose();
        }
    }
}