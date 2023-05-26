package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Pixel;

public class IndexedImage extends Image {
    private ArrayList<Color> colorMap;
    private int[][] indices;
    private int[] quantizationMap;

    public IndexedImage(ArrayList<Color> colorMap, int[][] indices, int height, int width, int fileSize,
            FileTime fileTime) {
        super(width, height, fileSize, fileTime);
        this.colorMap = colorMap;
        this.indices = indices;
    }

    public IndexedImage(ArrayList<Color> colorMap, int[][] indices, int height, int width, int[] quantizationMap,
            int fileSize, FileTime fileTime) {
        super(width, height, fileSize, fileTime);
        this.colorMap = colorMap;
        this.indices = indices;
        this.height = height;
        this.width = width;
        this.quantizationMap = quantizationMap;
    }

    @Override
    public ByteImage toByteImage() {
        Color[][] colors = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = colorMap.get(indices[i][j]);
            }
        }
        return new ByteImage(colors, height, width, fileSize, fileTime);
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
                    neighbours.add(new Pixel(colorMap.get(indices[i][j]), i, j));
                }

            }
        }

        return neighbours.toArray(new Pixel[0]);
    }

    @Override
    public Pixel getPixel(int x, int y) {
        if (!isInside(x, y))
            return null;
        Color color = colorMap.get(indices[x][y]);
        return new Pixel(color, x, y);
    }

    @Override
    public void setPixel(Pixel pixel) {
        int x = pixel.getX();
        int y = pixel.getY();

        if (!isInside(x, y))
            return;

        int i = -1;
        for (int it = 0; it < colorMap.size(); it++) {
            if (colorMap.get(it).equals(pixel.getColor())) {
                i = it;
                break;
            }
        }
        if (i != -1) {
            indices[x][y] = i;
        } else {
            colorMap.add(pixel.getColor());
            indices[x][y] = colorMap.size() - 1;
        }

    }

    @Override
    public IndexedImage toIndexedImage() {
        return this;
    }

    public boolean isQuantized() {
        if (quantizationMap != null)
            return true;
        return false;
    }

    public int[] getQuantizedMap() {
        return quantizationMap;
    }

    public ArrayList<Color> getColorMap() {
        return colorMap;
    }

    public int[][] getIndices() {
        return indices;
    }

    public void calculateQuantizationMap() {
        quantizationMap = new int[colorMap.size()];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int index = indices[i][j];
                quantizationMap[index] += 1;
            }
        }
    }
}
