package io.codeberg.spotpix.model.quantizers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.KColor;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;

public class MedianCutQuantizer implements Quantizer {
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

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Pixel pixel = image.getPixel(i, j);
                int x = pixel.getX();
                int y = pixel.getY();

                indices[x][y] = getIndex(pixel);
            }
        }

        IndexedImage indexedImage = new IndexedImage(colorMap, indices, height, width);
        return indexedImage;
    }

    private int getIndex(Pixel pixel) {
        Color color = pixel.getColor();
        double minDist = Integer.MAX_VALUE;
        int id = -1;
        for (int i = 0; i < colorMap.size(); i++) {
            Color c = colorMap.get(i);
            double distance = calcDistance(color, c);
            if (distance < minDist) {
                minDist = distance;
                id = i;
            }
        }
        return id;
    }

    private double channelDif(double c1, double c2, double alphas) {
        double black = c1 - c2, white = black + alphas;
        return Math.max(black * black, white * white);
    }

    protected double calcDistance(Color color, Color c2) {
        double sum = 0;

        double alphas = (c2.getAlpha() / 255) - (color.getAlpha() / 255);
        sum += channelDif((double) color.getRed() / 255.0, (double) c2.getRed() / 255.0, alphas);
        sum += channelDif((double) color.getBlue() / 255.0, (double) c2.getBlue() / 255.0, alphas);
        sum += channelDif((double) color.getGreen() / 255.0, (double) c2.getGreen() / 255.0, alphas);

        return sum;
    }

    private void splitIntoBuckets(ArrayList<KColor> colors, int depth) {
        if (depth <= 0) {
            medianCutQuantizer(colors);
            return;
        }
        KColor range = getRange(colors);
        switch(range.clusterIndex){
            case 0:
            Collections.sort(colors,new AlphaComparator());
            break;

            case 1:
            Collections.sort(colors,new RedComparator());
            break;

            case 2:
            Collections.sort(colors,new GreenComparator());
            break;

            case 3:
            Collections.sort(colors,new BlueComparator());
            break;
        }

        int median_index = (int)((colors.size()+1)/2);

        splitIntoBuckets(new ArrayList<KColor>(colors.subList(0, median_index)), depth-1);
        splitIntoBuckets(new ArrayList<KColor>(colors.subList(median_index,colors.size())), depth-1);
        return;
    }

    private KColor getRange(ArrayList<KColor> colors) {
        int rM = Integer.MIN_VALUE, rm = Integer.MAX_VALUE;
        int gM = Integer.MIN_VALUE, gm = Integer.MAX_VALUE;
        int bM = Integer.MIN_VALUE, bm = Integer.MAX_VALUE;
        int aM = Integer.MIN_VALUE, am = Integer.MAX_VALUE;
        for (KColor color : colors) {
            rM = Math.max(rM, color.getRed());
            rm = Math.min(rm, color.getRed());

            gM = Math.max(gM, color.getGreen());
            gm = Math.min(gm, color.getGreen());

            bM = Math.max(bM, color.getBlue());
            bm = Math.min(bm, color.getBlue());

            aM = Math.max(aM, color.getAlpha());
            am = Math.min(am, color.getAlpha());
        }
        int r, g, b, a;
        r = rM - rm;
        g = gM - gm;
        b = bM - bm;
        a = aM - am;

        KColor returned = new KColor(a, r, g, b);
        if (a == Math.max(a, Math.max(r, Math.max(g, b))))
            returned.clusterIndex = 0;
        else if (r == Math.max(a, Math.max(r, Math.max(g, b))))
            returned.clusterIndex = 1;
        else if (g == Math.max(a, Math.max(r, Math.max(g, b))))
            returned.clusterIndex = 2;
        else
            returned.clusterIndex = 3;
        return returned;
    }

    private void medianCutQuantizer(ArrayList<KColor> colors) {
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



    private static class AlphaComparator implements Comparator<KColor>{
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getAlpha() < c2.getAlpha() ) ? -1: (c1.getAlpha() > c2.getAlpha()) ? 1:0 ;
        }
    }
    private static class RedComparator implements Comparator<KColor>{
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getRed() < c2.getRed() ) ? -1: (c1.getRed() > c2.getRed()) ? 1:0 ;
        }
    }
    private static class GreenComparator implements Comparator<KColor>{
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getGreen() < c2.getGreen() ) ? -1: (c1.getGreen() > c2.getGreen()) ? 1:0 ;
        }
    }
    private static class BlueComparator implements Comparator<KColor>{
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getBlue() < c2.getBlue() ) ? -1: (c1.getBlue() > c2.getBlue()) ? 1:0 ;
        }
    }
}
