package io.codeberg.spotpix.model.search.algorithms;

import java.util.ArrayList;
import java.util.HashSet;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;

public class ColorSearch implements SearchAlgorithm {
    private static final float labThreshold = 5.0f;
    private ArrayList<Color> searchColorMap;
    private ArrayList<Float> searchPercentageMap;
    private float repeatationThreshold;

    public ColorSearch(HashSet<Color> colors, Image searchImage, float threshold) {
        searchColorMap = new ArrayList<>(colors.size());
        searchPercentageMap = new ArrayList<>(colors.size());
        this.repeatationThreshold = threshold;
        IndexedImage indexedImage = searchImage.toIndexedImage();
        int pixelsCount = indexedImage.getHeight() * indexedImage.getWidth();

        ArrayList<Color> searchImageColorMap = indexedImage.getColorMap();
        int[] searchQuantizationMap = indexedImage.getQuantizedMap();
        for (int i = 0; i < searchImageColorMap.size(); i++) {
            if (colors.contains(searchImageColorMap.get(i))) {
                searchColorMap.add(searchImageColorMap.get(i));
                float colorPercentage = (float) searchQuantizationMap[i] / (float) pixelsCount;
                searchPercentageMap.add(colorPercentage);
            }
        }
    }

    @Override
    public boolean match(Image img) {
        return false;
    }
}
