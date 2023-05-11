package io.codeberg.spotpix.model.comparators;

import io.codeberg.spotpix.model.Color;

public class RGBComparator implements Comparator{

    @Override
    public boolean isEqual(Color color0, Color color1) {
        return color0.getARGB()==color1.getARGB();
    }
    
}
