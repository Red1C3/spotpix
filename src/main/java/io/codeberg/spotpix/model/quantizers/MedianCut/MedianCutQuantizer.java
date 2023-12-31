package io.codeberg.spotpix.model.quantizers.MedianCut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    int K,tureK;
    public ArrayList<Color> colorMap = new ArrayList<>();
    ArrayList<KColor> fakeColorMap = new ArrayList<>();
    ArrayList<KColor> allColors = new ArrayList<>();

    public MedianCutQuantizer(int k) {
        K = (int) Math.ceil(Math.log10(k) / Math.log10(2))+1;
        tureK=k;
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
        calColorMap();

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

        IndexedImage indexedImage = new IndexedImage(colorMap, indices, height, width, quantizationMap,image.getFileSize(),image.getFileTime());
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
        }

        sumR /= colors.size();
        sumG /= colors.size();
        sumB /= colors.size();
        sumA /= colors.size();

        KColor temp = new KColor(sumA, sumR, sumG, sumB);
        temp.clusterIndex=colors.size();

        fakeColorMap.add(temp);
    }

    private void calColorMap(){
        Collections.sort(fakeColorMap, new colorMapComparator());
        for (int i = 0; i < tureK; i++) {
            KColor c = fakeColorMap.get(i);
            Color curr = new Color(c.getARGB());
            if (!colorMap.contains(curr))
                colorMap.add(curr);
        }
    }

    private static class colorMapComparator implements Comparator<KColor> {
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.clusterIndex > c2.clusterIndex) ? -1 : (c1.clusterIndex < c2.clusterIndex) ? 1 : 0;
        }
    }

    protected abstract void splitIntoBuckets(ArrayList<KColor> colors, int depth);

    protected abstract int getRange(ArrayList<KColor> colors);
}
