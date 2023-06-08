package io.codeberg.spotpix.views;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.html.ImageView;

import io.codeberg.spotpix.controllers.ColorSystem;
import io.codeberg.spotpix.controllers.ImageCtrlr;
import io.codeberg.spotpix.controllers.ImageFormat;
import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.images.Image;

public class ImageViewPanel extends JPanel {
    private ImageCtrlr imageCtrlr;

    public ImageViewPanel() {
        super();
    }

    @Override
    public void paint(Graphics g) {
        int height = getSize().height;
        int width = getSize().width;
        if (imageCtrlr != null) {
            int imgHeight = imageCtrlr.getHeight();
            int imgWidth = imageCtrlr.getWidth();
            float aspectRatio = (float) imgWidth / (float) imgHeight;
            int x = 0, y = 0;

            imgWidth = width;
            imgHeight = (int) ((float) width / aspectRatio);

            if (imgHeight > height) {
                imgHeight = height;
                imgWidth = (int) ((float) height * aspectRatio);
            }
            y = (int) ((float) height / 2.0f - (float) imgHeight / 2.0f);
            x = (int) ((float) width / 2.0f - (float) imgWidth / 2.0f);

            g.drawImage(imageCtrlr.getBufferedImage(), x, y, imgWidth, imgHeight, null);
        }
    }

    public void openImage(String path) {
        try {
            imageCtrlr = new ImageCtrlr(path);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        repaint();
    }

    public void openImage(Image image) {
        imageCtrlr = new ImageCtrlr(image);
        repaint();
    }

    public void kMeanQuantize(int colorsCount, ColorSystem colorSystem) {
        imageCtrlr.kMeanQuantize(colorsCount, colorSystem);
        repaint();
    }

    public void medianCutQuantize(int colorsCount, ColorSystem colorSystem) {
        imageCtrlr.medianCutQuantize(colorsCount, colorSystem);
        repaint();
    }

    public void avgQuantize(ColorSystem colorSystem, EqComparator comparator, ColorOp colorOp) {
        imageCtrlr.avgQuantize(colorSystem, comparator, colorOp);
        repaint();
    }

    public void octreeQuantize(int colorsCount, ColorSystem colorSystem) {
        imageCtrlr.octreeQuantize(colorsCount, colorSystem);
        repaint();
    }

    public void saveImage(String path, ImageFormat format) {
        imageCtrlr.saveImage(path, format);
    }

    public ArrayList<Color> getColorMap() {
        if (imageCtrlr == null)
            return null;
        return imageCtrlr.getColorMap();
    }

    public int[] getQuantizationMap() {
        if (imageCtrlr == null)
            return null;
        return imageCtrlr.getQuantizationMap();
    }

    public int[] getRedChannel() {
        if (imageCtrlr == null)
            return null;
        return imageCtrlr.getRedChannel();
    }

    public int[] getGreenChannel() {
        if (imageCtrlr == null)
            return null;
        return imageCtrlr.getGreenChannel();
    }

    public int[] getBlueChannel() {
        if (imageCtrlr == null)
            return null;
        return imageCtrlr.getBlueChannel();
    }

    public boolean hasImage() {
        return imageCtrlr != null;
    }
    public int getImgHeight(){
        return imageCtrlr.getHeight();
    }
    public int getImgWidth(){
        return imageCtrlr.getWidth();
    }
    public int getFileSize(){
        return imageCtrlr.getFileSize();
    }
}
