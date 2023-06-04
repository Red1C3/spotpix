package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.DefaultListSelectionModel;
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
import javax.swing.text.html.ImageView;

import io.codeberg.spotpix.model.Color;

public class SearchDialog extends JDialog implements ActionListener {
    private final static String COLOR_SRCH_STR = "Color Search";

    private ColorPanel colorPanel;
    private JTextField pathField;
    private JButton pathButton, searchButton;
    private ViewerRoot viewerRoot;

    public SearchDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Search", ModalityType.APPLICATION_MODAL);
        this.viewerRoot = viewerRoot;
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
        colorPanel = new ColorPanel(viewerRoot.getImageViewPanel());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

}

class ColorPanel extends JPanel implements ItemListener {
    private JComboBox<Color> comboBox;
    private JList<Color> colorsList;
    private HashSet<Color> selectedColors;
    private boolean firstSelection = true;

    public ColorPanel(ImageViewPanel imageViewPanel) {
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets=new Insets(10, 10, 10, 10);
        selectedColors = new HashSet<>();
        comboBox = new JComboBox<Color>();
        comboBox.addItemListener(this);
        comboBox.setRenderer(new ColorRenderer());
        ArrayList<Color> colorMap = imageViewPanel.getColorMap();
        for (int i = 0; i < colorMap.size(); i++) {
            comboBox.addItem(colorMap.get(i));
        }
        comboBox.setSelectedItem(null);
        add(comboBox, constraints);
        constraints.gridy = 1;

        colorsList = new JList<Color>();
        colorsList.setCellRenderer(new ColorRenderer());
        colorsList.setSelectionModel(new DisabledItemSelectionModel());
        add(colorsList, constraints);

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {

            if (firstSelection) {
                firstSelection = false;
                return;
            }

            Color color = (Color) e.getItem();
            selectedColors.add(color);
            colorsList.setListData(selectedColors.toArray(new Color[0]));
        }
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

class DisabledItemSelectionModel extends DefaultListSelectionModel {

    @Override
    public void setSelectionInterval(int index0, int index1) {
        super.setSelectionInterval(-1, -1);
    }
}