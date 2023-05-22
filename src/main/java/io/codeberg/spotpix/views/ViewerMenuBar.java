package io.codeberg.spotpix.views;

import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import io.codeberg.spotpix.controllers.ImageFormat;
import io.codeberg.spotpix.model.images.Image;

public class ViewerMenuBar extends JMenuBar implements Action {
    private static final String FILE_STR = "File";
    private static final String OPEN_STR = "Open";
    private static final String EDIT_STR = "Edit";
    private static final String QUANTIZE_STR = "Quantize";
    private static final String SAVE_STR = "Save as";
    private static final String PNG_FMT="png";
    private static final String FLT_FMT="flt";
    private static final int IMAGE_VIEW_PANEL_INDEX = 1;

    private ViewerRoot viewerRoot;
    private ImageViewPanel imageViewPanel;
    private JMenuItem open, quantize, save;

    public ViewerMenuBar(ViewerRoot viewerRoot) {
        this.viewerRoot = viewerRoot;
        imageViewPanel = (ImageViewPanel) viewerRoot.splitPane.getComponent(IMAGE_VIEW_PANEL_INDEX);
        JMenu file = new JMenu(FILE_STR);
        JMenu edit = new JMenu(EDIT_STR);

        open = new JMenuItem(OPEN_STR);
        quantize = new JMenuItem(QUANTIZE_STR);
        save = new JMenuItem(SAVE_STR);

        open.addActionListener(this);
        quantize.addActionListener(this);
        save.addActionListener(this);

        file.add(open);
        file.add(save);
        add(file);

        edit.add(quantize);
        add(edit);
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
        QuantizationDialog quantizationDialog = new QuantizationDialog(viewerRoot);
    }

    private void saveAction() {
        JFileChooser fileChooser = new ImageSaver();
        int response = fileChooser.showSaveDialog(this);
        if(response==ImageChooser.APPROVE_OPTION){
            String path=fileChooser.getSelectedFile().getAbsolutePath();
            String ext=getExtension(path);
            if(ext.equals(PNG_FMT)){
                imageViewPanel.saveImage(path, ImageFormat.PNG);
            }else if(ext.equals(FLT_FMT)){
                imageViewPanel.saveImage(path, ImageFormat.FLT);
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
