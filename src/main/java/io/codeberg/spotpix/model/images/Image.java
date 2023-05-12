package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

import io.codeberg.spotpix.model.ColorSpace;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.comparators.Comparator;

public abstract class Image {
    protected int width, height;
    protected ColorSpace colorSpace;

    public abstract BufferedImage toBufferedImage();

    public abstract Pixel[] getNeighbouringPixels(Pixel pixel);

    public abstract Pixel getPixel(int x, int y);

    public abstract void setPixel(Pixel pixel);

    public boolean isInside(int x, int y) {
        return x > -1 && x < width && y > -1 && y < height;
    }

    public Pixel[] getRegion(Pixel seed, Comparator comparator) {
        boolean[][] visited = new boolean[width][height]; // Booleans default value is false
        ArrayList<Pixel> region = new ArrayList<>();
        Stack<Pixel> stack = new Stack<>();
        stack.push(seed);
        
        while (!stack.isEmpty()) {
            Pixel pixel = stack.pop();
            int x = pixel.getX();
            int y = pixel.getY();

            visited[x][y] = true;

            if (comparator.isEqual(seed.getColor(), pixel.getColor())) {
                region.add(pixel);
                Pixel[] neighbours = getNeighbouringPixels(pixel);

                for (Pixel p : neighbours) {
                    if (!visited[p.getX()][p.getY()]) {
                        stack.push(p);
                    }
                }
            }
        }

        return (Pixel[]) region.toArray();
    }
}
