package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.ColorSpace;

public class ByteImage implements Image {
    private Color[][] pixels;
    private int height, width;
    private ColorSpace colorSpace;

    // TODO pixels iterator
    public ByteImage(Color[][] pixels, int height, int width, ColorSpace colorSpace) {
        this.pixels = pixels;
        this.height = height;
        this.width = width;
        this.colorSpace = colorSpace;
    }

    public ByteImage(Color[][] pixels, int height, int width) {
        this.pixels = pixels;
        this.height = height;
        this.width = width;
        this.colorSpace = ColorSpace.LINEAR;
    }

    @Override
    public BufferedImage toBufferedImage() {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                switch (colorSpace) {
                    case sRGB:
                        int argb = pixels[i][j].getARGB();
                        bufferedImage.setRGB(i, j, argb);
                        break;
                    case LINEAR:
                        // FIXME convert from sRGB to linear
                        System.out.println("Cannot create a buffered image from a linear space image");
                        break;
                }

            }
        }
        return bufferedImage;
    }
}
