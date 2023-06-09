package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;
import java.nio.file.attribute.FileTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;

public abstract class Image {
    protected int width, height;
    protected FileTime fileTime;
    protected int fileSize;

    public abstract BufferedImage toBufferedImage();

    public abstract Pixel[] getNeighbouringPixels(Pixel pixel);

    public abstract Pixel getPixel(int x, int y);

    public abstract void setPixel(Pixel pixel);

    public abstract ByteImage toByteImage();

    public abstract IndexedImage toIndexedImage();

    public Image(int width, int height, int fileSize, FileTime fileTime) {
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
        this.fileTime = fileTime;
    }

    public Pixel[][] getPixels() {
        Pixel[][] pixels = new Pixel[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = getPixel(i, j);
            }
        }
        return pixels;
    }

    public Pixel[] getFlattenPixels() {
        Pixel[] pixels = new Pixel[width * height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i + width * j] = getPixel(i, j);
            }
        }
        return pixels;
    }

    public boolean isInside(int x, int y) {
        return x > -1 && x < width && y > -1 && y < height;
    }

    // visited should have the same dims as the pic
    public Pixel[] getRegion(Pixel seed, EqComparator comparator, boolean[][] visited) {
        if (visited == null) {
            visited = new boolean[width][height];
        }
        ArrayList<Pixel> region = new ArrayList<>();
        Stack<Pixel> stack = new Stack<>();
        stack.push(seed);

        while (!stack.isEmpty()) {
            Pixel pixel = stack.pop();
            int x = pixel.getX();
            int y = pixel.getY();
            if (visited[x][y])
                continue;

            visited[x][y] = true;

            region.add(pixel);
            Pixel[] neighbours = getNeighbouringPixels(pixel);

            for (Pixel p : neighbours) {
                if (!visited[p.getX()][p.getY()] && comparator.isEqual(seed.getColor(), p.getColor())) {
                    stack.push(p);
                }
            }
        }

        return region.toArray(new Pixel[0]);
    }

    // colorOp can be null if updateStartColor was false
    // First Pixel is reserved, it's in (-1,-1) so it won't cause a problem if
    // setPixel was called on it
    // visited should have the same dims as the pic
    public Pixel[] getRegion(Pixel seed, EqComparator comparator, Color startColor,
            ColorOp colorOp, boolean[][] visited) {
        if (visited == null) {
            visited = new boolean[width][height];
        }
        ArrayList<Pixel> region = new ArrayList<>();
        region.add(new Pixel(startColor, -1, -1));

        ArrayDeque<Pixel> queue = new ArrayDeque<>();
        queue.addLast(seed);

        while (!queue.isEmpty()) {
            Pixel pixel = queue.poll();
            int x = pixel.getX();
            int y = pixel.getY();
            if (visited[x][y])
                continue;

            visited[x][y] = true;

            region.add(pixel);
            if (colorOp != null) { // Update the color that I'm comparing with
                region.get(0).setColor(colorOp.op(region.get(0).getColor(), pixel.getColor()));
            }
            Pixel[] neighbours = getNeighbouringPixels(pixel);
            for (Pixel p : neighbours) {
                if (!visited[p.getX()][p.getY()]
                        && comparator.isEqual(region.get(0).getColor(), p.getColor())) {
                    queue.addLast(p);
                }
            }
        }

        return region.toArray(new Pixel[0]);
    }

    public Image crop(int[] min, int[] max) {
        int width = max[0] - min[0] + 1;
        int height = max[1] - min[1] + 1;

        Color[][] pixels = new Color[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = getPixel(min[0] + i, min[1] + j).getColor();
            }
        }

        return new ByteImage(pixels, height, width, fileSize, fileTime); //TODO test me
    }

    public void setRegion(Pixel[] region) {
        for (Pixel pixel : region) {
            setPixel(pixel);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getFileSize() {
        return fileSize;
    }

    public FileTime getFileTime() {
        return fileTime;
    }
}
