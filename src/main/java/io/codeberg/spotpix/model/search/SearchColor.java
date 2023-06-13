package io.codeberg.spotpix.model.search;

import io.codeberg.spotpix.model.Color;

public class SearchColor extends Color {
    private float percentage;

    public SearchColor(int argb, float percentage) {
        super(argb);
        this.percentage = percentage;
    }
    public float getPercentage(){
        return percentage;
    }
    @Override
    public String toString() {
        return "(" + getRed() + "," + getGreen() + "," + getBlue() + ")" + " " + percentage * 100.0f + "%";
    }
}
