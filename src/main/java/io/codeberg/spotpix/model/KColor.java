package io.codeberg.spotpix.model;

public class KColor extends Color{
    public int clusterIndex;

    public KColor(int argb) {
        super(argb);
        clusterIndex=-1;
    }

    public KColor(int a, int r, int g, int b) {
        super(a, r, g, b);
        clusterIndex=-1;
    } 

    public KColor(Color c){
        super(c.getARGB());
        clusterIndex=-1;
    }
}
