package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;

import io.codeberg.spotpix.model.Pixel;

public interface Image {
    BufferedImage toBufferedImage();
    Pixel[] getNeighbouringPixels(Pixel pixel);
    Pixel getPixel(int x,int y);
}
