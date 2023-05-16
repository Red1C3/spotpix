package io.codeberg.spotpix.views;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

import io.codeberg.spotpix.controllers.ImageCtrlr;

public class ImageViewPanel extends JPanel {
    private ImageCtrlr imageCtrlr;
    @Override
    public void paint(Graphics g){
        int height=getSize().height;
        int width=getSize().width;
        //g.drawImage(img, 0, 0,width,height, null);
    }
    public void openImage(String path){
        imageCtrlr=new ImageCtrlr(path);
    }
}
