package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.ColorSpace;
import io.codeberg.spotpix.model.Pixel;

public class IndexedImage extends Image {
    private Color[] colorMap;
    private int[][] indices;

    public IndexedImage(Color[] colorMap, int[][] indices, int height, int width, ColorSpace colorSpace) {
        this.colorMap = colorMap;
        this.indices = indices;
        this.height = height;
        this.width = width;
        this.colorSpace = colorSpace;
    }

    public IndexedImage(Color[] colorMap, int[][] indices, int height, int width) {
        this.colorMap = colorMap;
        this.indices = indices;
        this.height = height;
        this.width = width;
        this.colorSpace = ColorSpace.LINEAR;
    }

    public ByteImage toByteImage() {
        Color[][] colors = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = colorMap[indices[i][j]];
            }
        }
        return new ByteImage(colors, height, width, colorSpace);
    }

    @Override
    public BufferedImage toBufferedImage() {
        return toByteImage().toBufferedImage();
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
                    neighbours.add(new Pixel(colorMap[indices[i][j]], i, j));
                }

            }
        }

        return (Pixel[]) neighbours.toArray();
    }
    

    @Override
    public Pixel getPixel(int x, int y) {
        if(!isInside(x, y)) return null;
        Color color=colorMap[indices[x][y]];
        return new Pixel(color, x, y);
    }

    @Override
    public void setPixel(Pixel pixel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPixel'");
    }
}
