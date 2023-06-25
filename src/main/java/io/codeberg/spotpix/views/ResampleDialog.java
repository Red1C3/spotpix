package io.codeberg.spotpix.views;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.text.MaskFormatter;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class ResampleDialog extends JDialog implements ActionListener {
    private static final String RESAMPLE_STR = "Resample";
    private static final String SCALE_STR = "Scale";
    private static final String CANCEL_STR = "Cancel";
    private ViewerRoot viewerRoot;
    private JFormattedTextField scaleField;
    private JRadioButton linearButton, nearestButton;
    private JButton scaleButton, cancelButton;

    public ResampleDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, RESAMPLE_STR, ModalityType.APPLICATION_MODAL);
        this.viewerRoot = viewerRoot;

        setLayout(new GridLayout(3, 2, 5, 5));
        add(new JLabel(SCALE_STR));

        scaleField = new JFormattedTextField(getMaskFormatter("##.##"));
        scaleField.setText("01.00");
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

        setLocationRelativeTo(null);
        setSize(220, 100);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == scaleButton) {
            float ratio = 1.0f;
            try {
                ratio = Float.parseFloat(getMaskFormatter("##.##").stringToValue(scaleField.getText()).toString());
            } catch (ParseException e1) {
                e1.printStackTrace(); // Probably would never trigger
            }
            if (ratio == 0) {
                JOptionPane.showMessageDialog(this, "Scale ratio must be larger than 0");
                return;
            }
            if (linearButton.isSelected()) {
                viewerRoot.getImageViewPanel().linearScale(ratio);
            } else if (nearestButton.isSelected()) {
                viewerRoot.getImageViewPanel().nearestScale(ratio);
            }
            viewerRoot.repaint();
            dispose();
        }
        if (e.getSource() == cancelButton) {
            dispose();
        }
    }

    private MaskFormatter getMaskFormatter(String format) {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter(format);
            mask.setPlaceholderCharacter('0');
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return mask;
    }
}
