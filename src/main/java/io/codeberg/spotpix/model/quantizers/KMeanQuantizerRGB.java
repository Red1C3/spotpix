package io.codeberg.spotpix.model.quantizers;

import io.codeberg.spotpix.model.Color;

public class KMeanQuantizerRGB extends KMeanQuantizer {
    public KMeanQuantizerRGB(int k){
        super(k);
    }
    
    private double channelDif(double c1,double c2, double alphas){
        double black = c1-c2, white = black+alphas;
        return Math.max(black*black, white*white);
    }
    @Override
    protected double calcDistance(Color color, Color c2) {
        double sum = 0;

        double alphas = (c2.getAlpha()/255)-(color.getAlpha()/255);
        sum += channelDif((double) color.getRed()/255.0,(double) c2.getRed()/255.0, alphas);
        sum += channelDif((double) color.getBlue()/255.0,(double) c2.getBlue()/255.0, alphas);
        sum += channelDif((double) color.getGreen()/255.0,(double) c2.getGreen()/255.0, alphas);

        return sum;
    }
}
