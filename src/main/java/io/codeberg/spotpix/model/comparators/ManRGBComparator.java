package io.codeberg.spotpix.model.comparators;

import io.codeberg.spotpix.model.Color;

public class ManRGBComparator implements EqComparator {
    private int threshold;

    public ManRGBComparator(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean isEqual(Color color0, Color color1) {
        int rDiff = Math.abs(color0.getRed() - color1.getRed());
        int gDiff = Math.abs(color0.getGreen() - color1.getGreen());
        int bDiff = Math.abs(color0.getBlue() - color1.getBlue());

        return rDiff + gDiff + bDiff <= threshold;
    }

}
