package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;

import io.codeberg.spotpix.model.Color;

public class ByteImage implements Image {
    private Color[][] pixels;
    private int height,width;
    //TODO pixels iterator
    public ByteImage(Color[][] pixels,int height,int width){
        this.pixels=pixels;
        this.height=height;
        this.width=width;
    }

    @Override
    public BufferedImage toBufferedImage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toBufferedImage'");
    }
    
}
