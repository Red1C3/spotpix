package io.codeberg.spotpix.model.quantizers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;
import io.codeberg.spotpix.model.KColor;

public class MedianCutQuantizerRGB extends MedianCutQuantizer {

    public MedianCutQuantizerRGB(int k) {
        super(k);
    }

    @Override
    protected void splitIntoBuckets(ArrayList<KColor> colors, int depth) {
        if (depth <= 0) {
            medianCutQuantizer(colors);
            return;
        }
        int index = getRange(colors);
        switch (index) {
            case 0:
                Collections.sort(colors, new AlphaComparator());
                break;

            case 1:
                Collections.sort(colors, new RedComparator());
                break;

            case 2:
                Collections.sort(colors, new GreenComparator());
                break;

            case 3:
                Collections.sort(colors, new BlueComparator());
                break;
        }

        int median_index = (int) ((colors.size() + 1) / 2);

        splitIntoBuckets(new ArrayList<KColor>(colors.subList(0, median_index)), depth - 1);
        splitIntoBuckets(new ArrayList<KColor>(colors.subList(median_index, colors.size())), depth - 1);
        return;
    }

    @Override
    protected int getRange(ArrayList<KColor> colors) {

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

        if (a == Math.max(a, Math.max(r, Math.max(g, b))))
            return 0;
        else if (r == Math.max(a, Math.max(r, Math.max(g, b))))
            return 1;
        else if (g == Math.max(a, Math.max(r, Math.max(g, b))))
            return 2;
        else
            return 3;
    }

    @Override
    protected double calcDistance(Color c1, Color c2) {
        return Distances.calcDistanceRGB(c1, c2);
    }

    private static class AlphaComparator implements Comparator<KColor> {
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getAlpha() < c2.getAlpha()) ? -1 : (c1.getAlpha() > c2.getAlpha()) ? 1 : 0;
        }
    }

    private static class RedComparator implements Comparator<KColor> {
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getRed() < c2.getRed()) ? -1 : (c1.getRed() > c2.getRed()) ? 1 : 0;
        }
    }

    private static class GreenComparator implements Comparator<KColor> {
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getGreen() < c2.getGreen()) ? -1 : (c1.getGreen() > c2.getGreen()) ? 1 : 0;
        }
    }

    private static class BlueComparator implements Comparator<KColor> {
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getBlue() < c2.getBlue()) ? -1 : (c1.getBlue() > c2.getBlue()) ? 1 : 0;
        }
    }

}
