package io.codeberg.spotpix.model.quantizers;

import io.codeberg.spotpix.model.Color;


public class KMeanQuantizerLAB extends KMeanQuantizer {
    public KMeanQuantizerLAB(int k){
        super(k);
    }

    @Override
    protected double calcDistance(Color color, Color c2) {
        double sum = 0;
        double[] c1LAB=color.getLAB();
        double[] c2LAB=c2.getLAB();

        sum+=(c1LAB[0]-c2LAB[0])*(c1LAB[0]-c2LAB[0]);
        sum+=(c1LAB[1]-c2LAB[1])*(c1LAB[1]-c2LAB[1]);
        sum+=(c1LAB[2]-c2LAB[2])*(c1LAB[2]-c2LAB[2]);

        sum = Math.sqrt(sum);

        return sum;
    }
}
