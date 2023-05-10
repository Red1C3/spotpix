package io.codeberg.spotpix.controllers;

import java.awt.image.BufferedImage;  

public class DummyCtrlr {
    public BufferedImage getImage(){
        BufferedImage img=new BufferedImage(100,100,BufferedImage.TYPE_BYTE_BINARY);
        
        return img;
    }   
}
