package io.codeberg.spotpix.views;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.text.html.ImageView;

import io.codeberg.spotpix.controllers.ColorSystem;
import io.codeberg.spotpix.controllers.ImageCtrlr;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;

public class ImageViewPanel extends JPanel {
    private static ImageViewPanel imageViewPanel;

    private ImageCtrlr imageCtrlr;
    private ImageViewPanel(){super();}
    public static ImageViewPanel instance(){
        if(imageViewPanel==null) imageViewPanel=new ImageViewPanel();
        return imageViewPanel;
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
            if (imgHeight > imgWidth) {
                imgHeight = height;
                imgWidth = (int) (height * aspectRatio);
                x = width / 2 - imgWidth / 2;
            } else {
                imgWidth = width;
                imgHeight = (int) (width / aspectRatio);
                y = height / 2 - imgHeight / 2;
            }

            g.drawImage(imageCtrlr.getBufferedImage(), x, y, imgWidth, imgHeight, null);
        }
    }

    public void openImage(String path) {
        imageCtrlr = new ImageCtrlr(path);
        repaint();
    }
    public void kMeanQuantize(int colorsCount,ColorSystem colorSystem){
        imageCtrlr.kMeanQuantize(colorsCount, colorSystem);
        repaint();
    }
    public void medianCutQuantize(int colorsCount,ColorSystem colorSystem){
        imageCtrlr.medianCutQuantize(colorsCount, colorSystem);
        repaint();
    }
    public void avgQuantize(ColorSystem colorSystem,EqComparator comparator,ColorOp colorOp){
        imageCtrlr.avgQuantize(colorSystem, comparator, colorOp);
        repaint();
    }
    public void octreeQuantize(int colorsCount,ColorSystem colorSystem){
        imageCtrlr.octreeQuantize(colorsCount, colorSystem);
        repaint();
    }
}