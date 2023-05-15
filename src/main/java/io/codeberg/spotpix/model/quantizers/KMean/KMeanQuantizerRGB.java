package io.codeberg.spotpix.model.quantizers.KMean;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;

public class KMeanQuantizerRGB extends KMeanQuantizer {
    public KMeanQuantizerRGB(int k){
        super(k);
    }

    @Override
    public double calcDistance(Color c1, Color c2) {
        return Distances.calcDistanceRGB(c1, c2);
    }
}
