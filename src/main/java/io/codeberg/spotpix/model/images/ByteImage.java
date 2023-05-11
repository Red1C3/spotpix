package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.ColorSpace;
import io.codeberg.spotpix.model.Pixel;

public class ByteImage extends Image {
    private Color[][] pixels;

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

    @Override
    public Pixel[] getNeighbouringPixels(Pixel pixel) {
        ArrayList<Pixel> neighbours = new ArrayList<>();
        int x = pixel.getX();
        int y = pixel.getY();

        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {

                if (i == x && j == y)
                    continue;

                if (isInside(i, j)) {
                    neighbours.add(new Pixel(pixels[i][j], i, j));
                }

            }
        }

        return (Pixel[]) neighbours.toArray();
    }

    @Override
    public Pixel getPixel(int x, int y) {
        if(!isInside(x, y))
            return null;
        return new Pixel(pixels[x][y], x, y);

    }

    
}
