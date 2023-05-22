package io.codeberg.spotpix.model.quantizers.MedianCut;

import java.util.ArrayList;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;
import io.codeberg.spotpix.model.KColor;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;
import io.codeberg.spotpix.model.quantizers.Quantizer;

public abstract class MedianCutQuantizer implements Quantizer {
    int K;
    public ArrayList<Color> colorMap = new ArrayList<>();
    ArrayList<KColor> allColors = new ArrayList<>();

    public MedianCutQuantizer(int k) {
        K = (int) Math.ceil(Math.log10(k) / Math.log10(2));
    }

    @Override
    public Image quantize(Image image, EqComparator comparator, ColorOp colorOp) {
        int height = image.getHeight();
        int width = image.getWidth();

        Pixel[] pixels = image.getFlattenPixels();
        for (Pixel pixel : pixels) {
            if (allColors.contains(pixel.getColor()) || pixel.getX() < 0 || pixel.getY() < 0)
                continue;
            else
                allColors.add(new KColor(pixel.getColor()));
        }

        splitIntoBuckets(allColors, K);

        int[][] indices = new int[width][height];
        int[] quantizationMap = new int[colorMap.size()];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Pixel pixel = image.getPixel(i, j);
                int x = pixel.getX();
                int y = pixel.getY();

                int index = Distances.getClosestColorId(pixel, colorMap, this);
                indices[x][y] = index;
                quantizationMap[index] += 1;
            }
        }

        IndexedImage indexedImage = new IndexedImage(colorMap, indices, height, width, quantizationMap);
        return indexedImage;
    }

    protected void medianCutQuantizer(ArrayList<KColor> colors) {
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        int sumA = 0;

        for (KColor color : colors) {
            sumR += color.getRed();
            sumG += color.getGreen();
            sumB += color.getBlue();
            sumA += color.getAlpha();
            color.clusterIndex = colorMap.size();
        }

        sumR /= colors.size();
        sumG /= colors.size();
        sumB /= colors.size();
        sumA /= colors.size();

        colorMap.add(new Color(sumA, sumR, sumG, sumB));
    }

    protected abstract void splitIntoBuckets(ArrayList<KColor> colors, int depth);

    protected abstract int getRange(ArrayList<KColor> colors);
}
