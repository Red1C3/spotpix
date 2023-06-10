package io.codeberg.spotpix.views;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.text.NumberFormatter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class ResampleDialog extends JDialog implements ActionListener {
    private static final String RESAMPLE_STR = "Resample";
    private static final String SCALE_STR = "Scale";
    private static final String CANCEL_STR = "Cancel";
    private ViewerRoot viewerRoot;
    private static NumberFormatter numberFormatter;
    private JFormattedTextField scaleField;
    private JRadioButton linearButton, nearestButton;
    private JButton scaleButton, cancelButton;

    public ResampleDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, RESAMPLE_STR, ModalityType.APPLICATION_MODAL);
        this.viewerRoot = viewerRoot;

        NumberFormat format = NumberFormat.getInstance();
        numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(0);
        numberFormatter.setMaximum(Integer.MAX_VALUE);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);

        setLayout(new GridLayout(3, 2, 5, 5));
        add(new JLabel(SCALE_STR));

        scaleField = new JFormattedTextField(numberFormatter);
        add(scaleField);

        linearButton = new JRadioButton("Linear Filter");
        nearestButton = new JRadioButton("Nearest Filter", true);
        scaleButton = new JButton(SCALE_STR);
        cancelButton = new JButton(CANCEL_STR);
        scaleButton.addActionListener(this);
        cancelButton.addActionListener(this);

        ButtonGroup filterGroup = new ButtonGroup();
        filterGroup.add(linearButton);
        filterGroup.add(nearestButton);

        add(linearButton);
        add(nearestButton);
        add(scaleButton);
        add(cancelButton);

        setSize(220, 100);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}
