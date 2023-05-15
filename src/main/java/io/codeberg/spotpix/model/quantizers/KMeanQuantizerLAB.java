package io.codeberg.spotpix.model.quantizers;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;


public class KMeanQuantizerLAB extends KMeanQuantizer {
    public KMeanQuantizerLAB(int k){
        super(k);
    }

    @Override
    protected double calcDistance(Color c1, Color c2) {
        return Distances.calcDistanceLAB(c1, c2);
    }
}
