package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.html.ImageView;

import io.codeberg.spotpix.model.Color;

public class SearchDialog extends JDialog implements ActionListener {
    private final static String COLOR_SRCH_STR = "Color Search";
    private final static String DATE_SRCH_STR = "Date Search";
    private ColorPanel colorPanel;
    private DatePanel datePanel;
    private JTextField pathField;
    private JButton pathButton, searchButton;
    private ViewerRoot viewerRoot;
    private JTabbedPane tabbedPane;

    public SearchDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Search", ModalityType.APPLICATION_MODAL);
        this.viewerRoot = viewerRoot;
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

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
        tabbedPane.add(datePanel, DATE_SRCH_STR);
        add(tabbedPane, BorderLayout.CENTER);
        add(pathPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        setSize(300, 300);
        setVisible(true);
    }

    private void setupPanels() {
        colorPanel = new ColorPanel(viewerRoot.getImageViewPanel());
        datePanel = new DatePanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            if (tabbedPane.getSelectedIndex() == 0) {
                Color[] searchColors = colorPanel.getSelectedColors();
                // Apply search algorithm
            }
        }
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
        constraints.insets = new Insets(10, 10, 10, 10);
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

    public Color[] getSelectedColors() {
        return selectedColors.toArray(new Color[0]);
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

class DatePanel extends JPanel {
    private static String START_DATE = "Start Date:";
    private static String END_DATE = "End Date:";
    private JComboBox<Integer> startYear, startMonth, startDay,
            endYear, endMonth, endDay;

    public DatePanel() {
        super();

        setLayout(new GridLayout(2, 4,10,100));
        add(new JLabel(START_DATE));
        startYear = new JComboBox<>();
        endYear = new JComboBox<>();
        for (int i = LocalDate.now().getYear(); i > 1969; i--) {
            endYear.addItem(i);
            startYear.addItem(i);
        }
        add(startYear);

        startMonth = new JComboBox<>();
        endMonth = new JComboBox<>();
        for (int i = 1; i < 13; i++) {
            startMonth.addItem(i);
            endMonth.addItem(i);
        }

        add(startMonth);

        startDay = new JComboBox<>();
        endDay = new JComboBox<>();
        for (int i = 1; i < 32; i++) {
            startDay.addItem(i);
            endDay.addItem(i);
        }
        add(startDay);

        add(new JLabel(END_DATE));

        add(endYear);
        add(endMonth);
        add(endDay);

    }
}