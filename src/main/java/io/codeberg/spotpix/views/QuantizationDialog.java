package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import io.codeberg.spotpix.controllers.ColorSystem;
import io.codeberg.spotpix.model.comparators.ManRGBComparator;

public class QuantizationDialog extends JDialog {
    private final static String K_MEAN_STR = "K-Means";
    private final static String MEDIAN_CUT_STR = "Median Cut";
    private final static String OCTREE_STR = "Octree";
    private final static String AVG_STR = "Average Color";
    private final ImageViewPanel imageViewPanel;
    private static NumberFormatter formatter;

    private AvgPanel avgPanel;
    private KMeanPanel kMeanPanel;
    private MedianCutPanel medianCutPanel;
    private OctreePanel octreePanel;

    public QuantizationDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Quantization", ModalityType.APPLICATION_MODAL);
        imageViewPanel = viewerRoot.getImageViewPanel();
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        NumberFormat format = NumberFormat.getInstance();
        formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        setupPanels();

        tabbedPane.add(kMeanPanel, K_MEAN_STR);
        tabbedPane.add(medianCutPanel, MEDIAN_CUT_STR);
        tabbedPane.add(octreePanel, OCTREE_STR);
        tabbedPane.add(avgPanel, AVG_STR);

        add(tabbedPane, BorderLayout.CENTER);
        setSize(300, 100);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void setupPanels() {
        kMeanPanel = new KMeanPanel(this);
        medianCutPanel = new MedianCutPanel(this);
        avgPanel = new AvgPanel(this);
        octreePanel = new OctreePanel(this);
    }

    public ImageViewPanel getImageViewPanel() {
        return imageViewPanel;
    }

    public static NumberFormatter getFormatter() {
        return formatter;
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

        colorsCount = new JFormattedTextField(QuantizationDialog.getFormatter());

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
            int colorsCount = 0;
            try {
                colorsCount = (int) QuantizationDialog.getFormatter().stringToValue(this.colorsCount.getText());
            } catch (NumberFormatException | ParseException numberFormatException) {
                JOptionPane.showMessageDialog(quantizationDialog, "Please enter a number of colors");
                return;
            }
            if (colorsCount > quantizationDialog.getImageViewPanel().getColorMap().size() || colorsCount == 0) {
                JOptionPane.showMessageDialog(quantizationDialog, "Input colors is invalid");
                return;
            }
            if (rgbButton.isSelected()) {
                quantizationDialog.getImageViewPanel().kMeanQuantize(colorsCount, ColorSystem.RGB);
            } else if (labButton.isSelected()) {
                quantizationDialog.getImageViewPanel().kMeanQuantize(colorsCount, ColorSystem.LAB);
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

        colorsCount = new JFormattedTextField(QuantizationDialog.getFormatter());

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
            int colorsCount = 0;
            try {
                colorsCount = (int) QuantizationDialog.getFormatter().stringToValue(this.colorsCount.getText());
            } catch (NumberFormatException | ParseException numberFormatException) {
                JOptionPane.showMessageDialog(quantizationDialog, "Please enter a number of colors");
                return;
            }
            if (colorsCount > quantizationDialog.getImageViewPanel().getColorMap().size() || colorsCount == 0) {
                JOptionPane.showMessageDialog(quantizationDialog, "Input colors is invalid");
                return;
            }
            if (rgbButton.isSelected()) {
                quantizationDialog.getImageViewPanel().medianCutQuantize(colorsCount, ColorSystem.RGB);
            } else if (labButton.isSelected()) {
                quantizationDialog.getImageViewPanel().medianCutQuantize(colorsCount, ColorSystem.LAB);
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

        thresholdLabel = new JLabel(ENTER_THRESHOLD_STR + "30");
        add(thresholdLabel);

        thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 30); // 256*3
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
                threshold = (int) ((((float) threshold) / 100.0f) * 256.0f * 3.0f);
                quantizationDialog.getImageViewPanel().avgQuantize(ColorSystem.RGB, new ManRGBComparator(threshold),
                        null);
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

        colorsCount = new JFormattedTextField(QuantizationDialog.getFormatter());

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
            int colorsCount = 0;
            try {
                colorsCount = (int) QuantizationDialog.getFormatter().stringToValue(this.colorsCount.getText());
            } catch (NumberFormatException | ParseException numberFormatException) {
                JOptionPane.showMessageDialog(quantizationDialog, "Please enter a number of colors");
                return;
            }
            if (colorsCount > quantizationDialog.getImageViewPanel().getColorMap().size() || colorsCount == 0) {
                JOptionPane.showMessageDialog(quantizationDialog, "Input colors is invalid");
                return;
            }
            if (rgbButton.isSelected()) {
                quantizationDialog.getImageViewPanel().octreeQuantize(colorsCount, ColorSystem.RGB);
            } else if (labButton.isSelected()) {
                quantizationDialog.getImageViewPanel().octreeQuantize(colorsCount, ColorSystem.LAB);
            }

            quantizationDialog.dispose();
        }
    }
}