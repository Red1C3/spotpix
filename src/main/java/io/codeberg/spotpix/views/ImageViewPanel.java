package io.codeberg.spotpix.views;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

import io.codeberg.spotpix.controllers.ImageCtrlr;

public class ImageViewPanel extends JPanel {
    private ImageCtrlr imageCtrlr;

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
}
