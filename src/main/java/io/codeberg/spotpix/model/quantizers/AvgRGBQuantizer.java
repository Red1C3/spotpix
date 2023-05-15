package io.codeberg.spotpix.model.quantizers;

import java.util.ArrayList;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;

public class AvgRGBQuantizer implements Quantizer {

    @Override
    public Image quantize(Image image, EqComparator comparator, ColorOp colorOp) {
        ArrayList<Pixel[]> regions = new ArrayList<>();
        int height = image.getHeight();
        int width = image.getWidth();

        boolean[][] visited = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Pixel[] region = image.getRegion(image.getPixel(i, j), comparator, image.getPixel(i, j).getColor(),
                        colorOp,
                        visited);
                if (region.length > 1)
                    regions.add(region);
            }
        }

        ArrayList<Color> colorMap = new ArrayList<>(regions.size());
        int[] quantizationMap=new int[regions.size()];
        int[][] indices = new int[width][height];

        for (int i = 0; i < regions.size(); i++) {
            colorMap.add(avgRGB(regions.get(i)));
            for (Pixel pixel : regions.get(i)) {
                int x = pixel.getX();
                int y = pixel.getY();
                if (x == -1 && y == -1)
                    continue;

                indices[x][y] = i;
                quantizationMap[i]++;
            }
        }

        IndexedImage indexedImage = new IndexedImage(colorMap, indices, height, width,quantizationMap);
        return indexedImage;
    }

    private Color avgRGB(Pixel[] region) {
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;

        for (Pixel pixel : region) {
        sumR += pixel.getColor().getRed();
        sumG += pixel.getColor().getGreen();
        sumB += pixel.getColor().getBlue();
        }

        sumR /= region.length;
        sumG /= region.length;
        sumB /= region.length;

        return new Color(255, sumR, sumG, sumB);
    }

    @Override
    public double calcDistance(Color c1, Color c2) {
        throw new UnsupportedOperationException("Unimplemented method 'calcDistance'");
    }

}
