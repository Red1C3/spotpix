package io.codeberg.spotpix.model;

public class Distances {
    public static double channelDif(double c1, double c2, double alphas) {
        double black = c1 - c2, white = black + alphas;
        return Math.max(black * black, white * white);
    }

    public static double calcDistanceRGB(Color c1, Color c2) {
        double sum = 0;

        double alphas = (c2.getAlpha() / 255) - (c1.getAlpha() / 255);
        sum += channelDif((double) c1.getRed() / 255.0, (double) c2.getRed() / 255.0, alphas);
        sum += channelDif((double) c1.getBlue() / 255.0, (double) c2.getBlue() / 255.0, alphas);
        sum += channelDif((double) c1.getGreen() / 255.0, (double) c2.getGreen() / 255.0, alphas);

        return sum;
    }

    public static double calcDistanceLAB(Color c1, Color c2) {
        double sum = 0;
        double[] c1LAB=c1.getLAB();
        double[] c2LAB=c2.getLAB();

        sum+=(c1LAB[0]-c2LAB[0])*(c1LAB[0]-c2LAB[0]);
        sum+=(c1LAB[1]-c2LAB[1])*(c1LAB[1]-c2LAB[1]);
        sum+=(c1LAB[2]-c2LAB[2])*(c1LAB[2]-c2LAB[2]);

        sum = Math.sqrt(sum);

        return sum;
    }
}
