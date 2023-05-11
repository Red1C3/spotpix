package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;

import io.codeberg.spotpix.model.ColorSpace;
import io.codeberg.spotpix.model.Pixel;

public abstract class Image {
    protected int width,height;
    protected ColorSpace colorSpace;
    public abstract BufferedImage toBufferedImage();
    public abstract Pixel[] getNeighbouringPixels(Pixel pixel);
    public abstract Pixel getPixel(int x,int y);
    public boolean isInside(int x,int y){
        return x > -1 && x < width && y > -1 && y < height;
    }
}
