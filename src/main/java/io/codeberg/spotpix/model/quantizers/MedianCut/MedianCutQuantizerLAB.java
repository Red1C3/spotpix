package io.codeberg.spotpix.model.quantizers.MedianCut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;
import io.codeberg.spotpix.model.KColor;

public class MedianCutQuantizerLAB extends MedianCutQuantizer {

    public MedianCutQuantizerLAB(int k) {
        super(k);
    }

    @Override
    protected int getRange(ArrayList<KColor> colors) {
        double lM = Double.MIN_VALUE, lm = Double.MAX_VALUE;
        double aM = Double.MIN_VALUE, am = Double.MAX_VALUE;
        double bM = Double.MIN_VALUE, bm = Double.MAX_VALUE;
        for (KColor color : colors) {
            double[] lab = color.getLAB();

            lM = Math.max(lM, lab[0]);
            lm = Math.min(lm, lab[0]);

            aM = Math.max(aM, lab[1]);
            am = Math.min(am, lab[1]);

            bM = Math.max(bM, lab[2]);
            bm = Math.min(bm, lab[2]);
        }
        double l, a, b;
        l = lM - lm;
        a = aM - am;
        b = bM - bm;

        if (l == Math.max(l, Math.max(a, b)))
            return 0;
        else if (a == Math.max(l, Math.max(a, b)))
            return 1;
        else
            return 2;
    }

    @Override
    public double calcDistance(Color c1, Color c2) {
        return Distances.calcDistanceLAB(c1, c2);
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
                Collections.sort(colors, new LComparator());
                break;

            case 1:
                Collections.sort(colors, new AComparator());
                break;

            case 2:
                Collections.sort(colors, new BComparator());
                break;
        }

        int median_index = (int) ((colors.size() + 1) / 2);

        splitIntoBuckets(new ArrayList<KColor>(colors.subList(0, median_index)), depth - 1);
        splitIntoBuckets(new ArrayList<KColor>(colors.subList(median_index, colors.size())), depth - 1);
        return;
    }

    private static class LComparator implements Comparator<KColor> {
        @Override
        public int compare(KColor c1, KColor c2) {

            return (c1.getLAB()[0] < c2.getLAB()[0]) ? -1 : (c1.getLAB()[0] > c2.getLAB()[0]) ? 1 : 0;
        }
    }

    private static class AComparator implements Comparator<KColor> {
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getLAB()[1] < c2.getLAB()[1]) ? -1 : (c1.getLAB()[1] > c2.getLAB()[1]) ? 1 : 0;
        }
    }

    private static class BComparator implements Comparator<KColor> {
        @Override
        public int compare(KColor c1, KColor c2) {
            return (c1.getLAB()[2] < c2.getLAB()[2]) ? -1 : (c1.getLAB()[2] > c2.getLAB()[2]) ? 1 : 0;
        }
    }
}
