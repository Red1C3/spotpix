package io.codeberg.spotpix.model.search.algorithms;

import io.codeberg.spotpix.model.images.Image;

public class SizeSearch implements SearchAlgorithm {
    private int thresholdInBytes, targetSizeInBytes;

    public SizeSearch(int targetSizeInBytes, int thresholdInBytes) {
        this.thresholdInBytes = thresholdInBytes;
        this.targetSizeInBytes = targetSizeInBytes;
    }

    @Override
    public boolean match(Image img) {
        return Math.abs(img.getFileSize() - targetSizeInBytes) <= thresholdInBytes;
    }

}
