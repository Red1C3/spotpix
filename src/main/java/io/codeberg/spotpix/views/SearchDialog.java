package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import io.codeberg.spotpix.model.Color;

public class SearchDialog extends JDialog implements ActionListener {
    private final static String COLOR_SRCH_STR = "Color Search";

    private ColorPanel colorPanel;
    private JTextField pathField;
    private JButton pathButton, searchButton;

    public SearchDialog() {
        super(ViewerRoot.instance(), "Search", ModalityType.APPLICATION_MODAL);

        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        pathField = new JTextField();
        pathButton = new JButton("Navigate");
        pathButton.addActionListener(this);
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BorderLayout());
        pathPanel.add(pathField, BorderLayout.CENTER);
        pathPanel.add(pathButton, BorderLayout.EAST);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        JPanel southPanel = new JPanel();
        southPanel.add(searchButton);

        setupPanels();

        tabbedPane.add(colorPanel, COLOR_SRCH_STR);
        add(tabbedPane, BorderLayout.CENTER);
        add(pathPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        setSize(300, 300);
        setVisible(true);
    }

    private void setupPanels() {
        colorPanel = new ColorPanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

}

class ColorPanel extends JPanel {
    private JComboBox<Color> comboBox;

    public ColorPanel() {
        super();
        comboBox = new JComboBox<Color>(); // find out how to render labels in combobox
        comboBox.setRenderer(new ColorRenderer());
        Color red = new Color(255, 255, 0, 0);
        comboBox.addItem(red);
        add(comboBox);
    }
}

class ColorRenderer extends BasicComboBoxRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Color) {
            BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.createGraphics();
            Color color = (Color) value;
            g.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue()));
            g.fillRect(0, 0, 15, 15);
            g.dispose();
            setIcon(new ImageIcon(img));
        }
        return this;
    }
}