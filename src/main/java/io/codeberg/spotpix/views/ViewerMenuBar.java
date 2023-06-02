package io.codeberg.spotpix.views;

import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.text.html.ImageView;

import io.codeberg.spotpix.controllers.ImageFormat;
import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.images.Image;

public class ViewerMenuBar extends JMenuBar implements Action {
    private static final String FILE_STR = "File";
    private static final String OPEN_STR = "Open";
    private static final String EDIT_STR = "Edit";
    private static final String QUANTIZE_STR = "Quantize";
    private static final String SAVE_STR = "Save as";
    private static final String VIEW_STR = "View";
    private static final String ALL_CLR_HISTO_STR = "All Colors Histogram";
    private static final String RGB_HISTO_STR = "RGB Channel Histogram";
    private static final String PALLET_STRING = "Color Pallet";
    private static final String PNG_FMT = "png";
    private static final String FLT_FMT = "flt";

    private ViewerRoot viewerRoot;
    private ImageViewPanel imageViewPanel;
    private JMenuItem open, quantize, save, allColorHistogram, rgbHistogram, palletView;

    public ViewerMenuBar(ViewerRoot viewerRoot) {
        this.viewerRoot = viewerRoot;
        imageViewPanel = viewerRoot.getImageViewPanel();
        JMenu file = new JMenu(FILE_STR);
        JMenu edit = new JMenu(EDIT_STR);
        JMenu view = new JMenu(VIEW_STR);

        open = new JMenuItem(OPEN_STR);
        quantize = new JMenuItem(QUANTIZE_STR);
        save = new JMenuItem(SAVE_STR);
        allColorHistogram = new JMenuItem(ALL_CLR_HISTO_STR);
        rgbHistogram = new JMenuItem(RGB_HISTO_STR);
        palletView = new JMenuItem(PALLET_STRING);

        open.addActionListener(this);
        quantize.addActionListener(this);
        save.addActionListener(this);
        allColorHistogram.addActionListener(this);
        rgbHistogram.addActionListener(this);
        palletView.addActionListener(this);

        file.add(open);
        file.add(save);
        add(file);

        edit.add(quantize);
        add(edit);

        view.add(allColorHistogram);
        view.add(rgbHistogram);
        view.add(palletView);
        add(view);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == open) {
            openAction();
        }
        if (e.getSource() == quantize) {
            quantizeAction();
        }
        if (e.getSource() == save) {
            saveAction();
        }
        if(e.getSource()==allColorHistogram){
            viewAllColorsHistogram();
        }
        if(e.getSource()==rgbHistogram){
            viewRGBHistogram();
        }
        if(e.getSource()==palletView){
            viewColorPallet();
        }
    }

    private void viewColorPallet() {
        PalletView.createPallet(imageViewPanel.getColorMap());
    }

    private void viewRGBHistogram() {
        RGBHistogramPanel redPanel=RGBHistogramPanel.createRGBHistogramPanel("Red", imageViewPanel.getRedChannel());
        RGBHistogramPanel greenPanel=RGBHistogramPanel.createRGBHistogramPanel("Green", imageViewPanel.getGreenChannel());
        RGBHistogramPanel bluePanel=RGBHistogramPanel.createRGBHistogramPanel("Blue", imageViewPanel.getBlueChannel());
        RGBHistogramPanel.createRGBPanel(redPanel, greenPanel, bluePanel);
    }

    private void viewAllColorsHistogram() {
        int[] quantizationMap=imageViewPanel.getQuantizationMap();
        ArrayList<Color> colorMap=imageViewPanel.getColorMap();
        HistogramPanel.createHistogram(quantizationMap, colorMap);
    }

    @Override
    public Object getValue(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValue'");
    }

    @Override
    public void putValue(String key, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'putValue'");
    }

    private void openAction() {
        JFileChooser imageChooser = new ImageChooser();
        int response = imageChooser.showOpenDialog(this);
        if (response == ImageChooser.APPROVE_OPTION) {
            File file = imageChooser.getSelectedFile();
            imageViewPanel.openImage(file.getAbsolutePath());
        }
        viewerRoot.repaint();
    }

    private void quantizeAction() {
        if(!imageViewPanel.hasImage()){
            JOptionPane.showMessageDialog(viewerRoot, "No image is opened");
            return;
        }
        new QuantizationDialog(viewerRoot);
    }

    private void saveAction() {
        if(!imageViewPanel.hasImage()){
            JOptionPane.showMessageDialog(viewerRoot, "No image is opened");
            return;
        }
        JFileChooser fileChooser = new ImageSaver();
        int response = fileChooser.showSaveDialog(this);
        if (response == ImageChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            String ext = getExtension(path);
            if (ext.equals(PNG_FMT)) {
                imageViewPanel.saveImage(path, ImageFormat.PNG);
            } else if (ext.equals(FLT_FMT)) {
                imageViewPanel.saveImage(path, ImageFormat.FLT);
            }else{
                JOptionPane.showMessageDialog(viewerRoot, "Unsupported format, aborting...");
            }
        }
    }

    private String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension.toLowerCase();
    }
}
