package io.codeberg.spotpix.model.search.algorithms;

import io.codeberg.spotpix.model.images.Image;

public class DimSearch implements SearchAlgorithm {
    private int targetHeight, targetWidth, widthThreshold, heightThreshold;

    public DimSearch(int targetWidth, int targetHeight, int widthThreshold, int heightThreshold) {
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        this.heightThreshold = heightThreshold;
        this.widthThreshold = widthThreshold;
    }

    @Override
    public boolean match(Image img) {
        int width=img.getWidth();
        int height=img.getHeight();
        return Math.abs(targetHeight-height)<=heightThreshold && Math.abs(targetWidth-width)<=widthThreshold;
    }

}
