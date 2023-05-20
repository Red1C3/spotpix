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
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import javax.swing.text.html.ImageView;

import io.codeberg.spotpix.controllers.ColorSystem;
import io.codeberg.spotpix.model.comparators.ManRGBComparator;

public class QuantizationDialog extends JDialog {
    private final static String K_MEAN_STR = "K-Means";
    private final static String MEDIAN_CUT_STR = "Median Cut";
    private final static String OCTREE_STR = "Octree";
    private final static String AVG_STR = "Average Color";

    private AvgPanel avgPanel;
    private KMeanPanel kMeanPanel;
    private MedianCutPanel medianCutPanel;
    private OctreePanel octreePanel;

    public QuantizationDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Quantization", ModalityType.APPLICATION_MODAL);
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        setupPanels();

        tabbedPane.add(kMeanPanel, K_MEAN_STR);
        tabbedPane.add(medianCutPanel, MEDIAN_CUT_STR);
        tabbedPane.add(octreePanel, OCTREE_STR);
        tabbedPane.add(avgPanel, AVG_STR);

        add(tabbedPane, BorderLayout.CENTER);
        setSize(300, 100);
        setVisible(true);

    }

    private void setupPanels() {
        kMeanPanel = new KMeanPanel(this);
        medianCutPanel = new MedianCutPanel(this);
        avgPanel = new AvgPanel(this);
        octreePanel = new OctreePanel(this);
    }
}

class KMeanPanel extends JPanel implements ActionListener {
    private final static String ENTER_COLOR_STR = "The number of colors:";
    private final JFormattedTextField colorsCount;
    private final ButtonGroup colorSystem;
    private final JRadioButton rgbButton, labButton;
    private final JButton quantizeButton, cancelButton;
    private final QuantizationDialog quantizationDialog;

    public KMeanPanel(QuantizationDialog quantizationDialog) {
        super();
        this.quantizationDialog = quantizationDialog;
        setLayout(new GridLayout(3, 2));

        add(new JLabel(ENTER_COLOR_STR));

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        colorsCount = new JFormattedTextField(formatter);

        add(colorsCount);

        colorSystem = new ButtonGroup();
        rgbButton = new JRadioButton("RGB", true);
        labButton = new JRadioButton("Lab");

        colorSystem.add(rgbButton);
        colorSystem.add(labButton);
        add(rgbButton);
        add(labButton);

        quantizeButton = new JButton("Quantize");
        cancelButton = new JButton("Cancel");
        quantizeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        add(quantizeButton);
        add(cancelButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            quantizationDialog.dispose();
        } else if (e.getSource() == quantizeButton) {
            int colorsCount = Integer.parseInt(this.colorsCount.getText());
            if (rgbButton.isSelected()) {
                ImageViewPanel.instance().kMeanQuantize(colorsCount, ColorSystem.RGB);
            } else if (labButton.isSelected()) {
                ImageViewPanel.instance().kMeanQuantize(colorsCount, ColorSystem.LAB);
            }
            quantizationDialog.dispose();
        }
    }
}

class MedianCutPanel extends JPanel implements ActionListener {
    private final static String ENTER_COLOR_STR = "The number of colors:";
    private final JFormattedTextField colorsCount;
    private final ButtonGroup colorSystem;
    private final JRadioButton rgbButton, labButton;
    private final JButton quantizeButton, cancelButton;
    private final QuantizationDialog quantizationDialog;

    public MedianCutPanel(QuantizationDialog quantizationDialog) {
        super();
        this.quantizationDialog = quantizationDialog;
        setLayout(new GridLayout(3, 2));

        add(new JLabel(ENTER_COLOR_STR));

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        colorsCount = new JFormattedTextField(formatter);

        add(colorsCount);

        colorSystem = new ButtonGroup();
        rgbButton = new JRadioButton("RGB", true);
        labButton = new JRadioButton("Lab");

        colorSystem.add(rgbButton);
        colorSystem.add(labButton);
        add(rgbButton);
        add(labButton);

        quantizeButton = new JButton("Quantize");
        cancelButton = new JButton("Cancel");
        quantizeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        add(quantizeButton);
        add(cancelButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            quantizationDialog.dispose();
        } else if (e.getSource() == quantizeButton) {
            int colorsCount = Integer.parseInt(this.colorsCount.getText());
            if (rgbButton.isSelected()) {
                ImageViewPanel.instance().medianCutQuantize(colorsCount, ColorSystem.RGB);
            } else if (labButton.isSelected()) {
                ImageViewPanel.instance().medianCutQuantize(colorsCount, ColorSystem.LAB);
            }
            quantizationDialog.dispose();
        }
    }
}

class AvgPanel extends JPanel implements ActionListener, ChangeListener {
    private final static String ENTER_THRESHOLD_STR = "Threshold:";
    private final JLabel thresholdLabel;
    private final JSlider thresholdSlider;
    private final ButtonGroup colorSystem;
    private final JRadioButton rgbButton, labButton;
    private final JButton quantizeButton, cancelButton;
    private final QuantizationDialog quantizationDialog;

    public AvgPanel(QuantizationDialog quantizationDialog) {
        super();
        this.quantizationDialog = quantizationDialog;
        setLayout(new GridLayout(3, 2));

        thresholdLabel = new JLabel(ENTER_THRESHOLD_STR + "70");
        add(thresholdLabel);

        thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 256 * 3, 70);
        thresholdSlider.addChangeListener(this);

        add(thresholdSlider);

        colorSystem = new ButtonGroup();
        rgbButton = new JRadioButton("RGB", true);
        labButton = new JRadioButton("Lab");

        colorSystem.add(rgbButton);
        colorSystem.add(labButton);
        // add(rgbButton);
        // add(labButton); not supported YET

        quantizeButton = new JButton("Quantize");
        cancelButton = new JButton("Cancel");
        quantizeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        add(quantizeButton);
        add(cancelButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            quantizationDialog.dispose();
        } else if (e.getSource() == quantizeButton) {
            int threshold = thresholdSlider.getValue();
            if (rgbButton.isSelected()) {
                ImageViewPanel.instance().avgQuantize(ColorSystem.RGB, new ManRGBComparator(threshold), null);
            }
            quantizationDialog.dispose();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int val = thresholdSlider.getValue();
        thresholdLabel.setText(ENTER_THRESHOLD_STR + val);
    }
}

class OctreePanel extends JPanel implements ActionListener {
    private final static String ENTER_COLOR_STR = "The number of colors:";
    private final JFormattedTextField colorsCount;
    private final ButtonGroup colorSystem;
    private final JRadioButton rgbButton, labButton;
    private final JButton quantizeButton, cancelButton;
    private final QuantizationDialog quantizationDialog;

    public OctreePanel(QuantizationDialog quantizationDialog) {
        super();
        this.quantizationDialog = quantizationDialog;
        setLayout(new GridLayout(3, 2));

        add(new JLabel(ENTER_COLOR_STR));

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        colorsCount = new JFormattedTextField(formatter);

        add(colorsCount);

        colorSystem = new ButtonGroup();
        rgbButton = new JRadioButton("RGB", true);
        labButton = new JRadioButton("Lab");

        colorSystem.add(rgbButton);
        colorSystem.add(labButton);
        add(rgbButton);
        add(labButton);

        quantizeButton = new JButton("Quantize");
        cancelButton = new JButton("Cancel");
        quantizeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        add(quantizeButton);
        add(cancelButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            quantizationDialog.dispose();
        } else if (e.getSource() == quantizeButton) {
            int colorsCount = Integer.parseInt(this.colorsCount.getText());
            if (rgbButton.isSelected()) {
                ImageViewPanel.instance().octreeQuantize(colorsCount, ColorSystem.RGB);
            } else if (labButton.isSelected()) {
                ImageViewPanel.instance().octreeQuantize(colorsCount, ColorSystem.LAB);
            }
            quantizationDialog.dispose();
        }
    }
}