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
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
import javax.swing.text.NumberFormatter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.html.ImageView;

import io.codeberg.spotpix.App;
import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.search.SearchEngine;
import io.codeberg.spotpix.model.search.algorithms.DateSearch;
import io.codeberg.spotpix.model.search.algorithms.DimSearch;

public class SearchDialog extends JDialog implements ActionListener {
    private final static String COLOR_SRCH_STR = "Color Search";
    private final static String DATE_SRCH_STR = "Date Search";
    private final static String DIM_SRCH_STR = "Dimensions Search";
    private ColorPanel colorPanel;
    private DatePanel datePanel;
    private DimPanel dimPanel;
    private JTextField pathField;
    private JButton pathButton, searchButton;
    private ViewerRoot viewerRoot;
    private JTabbedPane tabbedPane;

    private static NumberFormatter numberFormatter;

    public SearchDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Search", ModalityType.APPLICATION_MODAL);
        this.viewerRoot = viewerRoot;

        NumberFormat format = NumberFormat.getInstance();
        numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(0);
        numberFormatter.setMaximum(Integer.MAX_VALUE);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);

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
        tabbedPane.add(dimPanel, DIM_SRCH_STR);
        add(tabbedPane, BorderLayout.CENTER);
        add(pathPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        setSize(300, 300);
        setVisible(true);
    }

    private void setupPanels() {
        colorPanel = new ColorPanel(viewerRoot.getImageViewPanel());
        datePanel = new DatePanel();
        if (viewerRoot.getImageViewPanel().hasImage()) {
            dimPanel = new DimPanel(viewerRoot.getImageViewPanel().getImgWidth(),
                    viewerRoot.getImageViewPanel().getImgHeight());
        } else {
            dimPanel = new DimPanel(0, 0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pathButton) {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setCurrentDirectory(Paths.get(System.getProperty("user.dir")).toFile());
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            directoryChooser.setApproveButtonText("Select Directory");
            int response = directoryChooser.showOpenDialog(this);
            if (response == JFileChooser.APPROVE_OPTION) {
                pathField.setText(directoryChooser.getSelectedFile().getAbsolutePath());
            }
        }
        if (e.getSource() == searchButton) {
            if (tabbedPane.getSelectedIndex() == 0) {
                Color[] searchColors = colorPanel.getSelectedColors();
                // Apply search algorithm
            } else if (tabbedPane.getSelectedIndex() == 1) {
                Date startDate = datePanel.getStartDate();
                Date endDate = datePanel.getEndDate();
                SearchEngine engine = new SearchEngine(Paths.get(pathField.getText()), null);
                ArrayList<Image> res = engine.search(new DateSearch(startDate, endDate));
                displayResults(res);
            } else if (tabbedPane.getSelectedIndex() == 2) {
                int width = dimPanel.getInputWidth();
                int height = dimPanel.getInputHeight();
                int widthLim = dimPanel.getInputWidthLim();
                int heightLim = dimPanel.getInputHeightLim();

                SearchEngine engine = new SearchEngine(Paths.get(pathField.getText()), null);
                ArrayList<Image> res = engine.search(new DimSearch(width, height, widthLim, heightLim));
                displayResults(res);
            }
            dispose();
        }
    }

    public void displayResults(ArrayList<Image> images) {
        for (Image image : images) {
            App.main(image);
        }
    }

    public static NumberFormatter getFormatter() {
        return numberFormatter;
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
    private final static String START_DATE = "Start Date:";
    private final static String END_DATE = "End Date:";
    private JComboBox<Integer> startYear, startMonth, startDay,
            endYear, endMonth, endDay;

    public DatePanel() {
        super();

        setLayout(new GridLayout(2, 4, 10, 100));
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

    public Date getStartDate() {
        int year = (int) startYear.getSelectedItem();
        int month = (int) startMonth.getSelectedItem();
        int day = (int) startDay.getSelectedItem();

        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date getEndDate() {
        int year = (int) endYear.getSelectedItem();
        int month = (int) endMonth.getSelectedItem();
        int day = (int) endDay.getSelectedItem();

        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

class DimPanel extends JPanel {
    private static final String WIDTH_LIMITS_STR = "Width Limits:";
    private static final String HEIGHT_LIMITS_STR = "Height Limits:";
    private static final String WIDTH_STR = "Width:";
    private static final String HEIGHT_STR = "Height:";

    private JFormattedTextField width, height, widthLim, heightLim;

    public DimPanel(int imgWidth, int imgHeight) {
        super();
        setLayout(new GridLayout(4, 2, 0, 20));

        add(new JLabel(WIDTH_STR));
        width = new JFormattedTextField(SearchDialog.getFormatter());
        width.setText(Integer.valueOf(imgWidth).toString());
        add(width);

        add(new JLabel(HEIGHT_STR));
        height = new JFormattedTextField(SearchDialog.getFormatter());
        height.setText(Integer.valueOf(imgHeight).toString());
        add(height);

        add(new JLabel(WIDTH_LIMITS_STR));
        widthLim = new JFormattedTextField(SearchDialog.getFormatter());
        widthLim.setText("0");
        add(widthLim);

        add(new JLabel(HEIGHT_LIMITS_STR));
        heightLim = new JFormattedTextField(SearchDialog.getFormatter());
        heightLim.setText("0");
        add(heightLim);
    }

    public int getInputWidth() {
        try {
            return (int) SearchDialog.getFormatter().stringToValue(width.getText());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public int getInputHeight() {
        try {
            return (int) SearchDialog.getFormatter().stringToValue(height.getText());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public int getInputWidthLim() {
        try {
            return (int) SearchDialog.getFormatter().stringToValue(widthLim.getText());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public int getInputHeightLim() {
        try {
            return (int) SearchDialog.getFormatter().stringToValue(heightLim.getText());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}