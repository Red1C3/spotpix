package io.codeberg.spotpix.model.search.algorithms;

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.text.html.HTMLDocument.Iterator;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;
import io.codeberg.spotpix.model.search.SearchColor;

public class ColorSearch implements SearchAlgorithm {
    private static final double labThreshold = 5.0;
    private ArrayList<SearchColor> searchColorMap;
    private double repeatationThreshold;

    public ColorSearch(HashSet<Color> colors, Image searchImage, double threshold) {
        searchColorMap = new ArrayList<>(colors.size());
        this.repeatationThreshold = threshold;
        java.util.Iterator<Color> iterator = colors.iterator();
        while (iterator.hasNext()) {
            SearchColor searchColor = (SearchColor) iterator.next();
            searchColorMap.add(searchColor);
        }
    }

    @Override
    public boolean match(Image img) {
        IndexedImage indexedImage = img.toIndexedImage();

        int imgPixelsCount = img.getHeight() * img.getWidth();

        ArrayList<Color> imgColorMap = indexedImage.getColorMap();
        int[] imgQuantizationMap = indexedImage.getQuantizedMap();

        for (int i = 0; i < searchColorMap.size(); i++) {
            SearchColor color = searchColorMap.get(i);
            double percentage = color.getPercentage();
            double sum = 0;

            for (int j = 0; j < imgColorMap.size(); j++) {
                Color imgColor = imgColorMap.get(j);
                float imgPercentage = (float) imgQuantizationMap[j] / (float) imgPixelsCount;

                double distance = Distances.calcDistanceLAB(color, imgColor);
                if (distance <= labThreshold) {
                    sum += imgPercentage;
                }
            }

            if (Math.abs(sum - percentage) > repeatationThreshold) {
                return false;
            }

        }

        return true;
    }
}
