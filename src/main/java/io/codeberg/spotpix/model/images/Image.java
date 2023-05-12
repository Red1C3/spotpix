package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.ColorSpace;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.Comparator;

public abstract class Image {
    protected int width, height;
    protected ColorSpace colorSpace;

    public abstract BufferedImage toBufferedImage();

    public abstract Pixel[] getNeighbouringPixels(Pixel pixel);

    public abstract Pixel getPixel(int x, int y);

    public abstract void setPixel(Pixel pixel);

    public abstract ByteImage toByteImage();

    public abstract IndexedImage toIndexedImage();

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

        return region.toArray(new Pixel[0]);
    }

    // colorOp can be null if updateStartColor was false
    public Pixel[] getRegion(Pixel seed, Comparator comparator, Color startColor,
            boolean updateStartColor, ColorOp colorOp) {
        boolean[][] visited = new boolean[width][height];
        ArrayList<Pixel> region = new ArrayList<>();
        region.add(new Pixel(startColor, -1, -1));

        ArrayDeque<Pixel> queue = new ArrayDeque<>();
        queue.addLast(seed);

        while (!queue.isEmpty()) {
            Pixel pixel = queue.poll();
            int x = pixel.getX();
            int y = pixel.getY();

            visited[x][y] = true;

            if (comparator.isEqual(region.get(0).getColor(), pixel.getColor())) {
                region.add(pixel);
                if (updateStartColor) { // Update the color that I'm comparing with
                    region.get(0).setColor(colorOp.op(region.get(0).getColor(), pixel.getColor()));
                }
                Pixel[] neighbours = getNeighbouringPixels(pixel);
                for (Pixel p : neighbours) {
                    if (!visited[p.getX()][p.getY()]) {
                        queue.addLast(p);
                    }
                }
            }
        }

        return region.toArray(new Pixel[0]);

    }
}
