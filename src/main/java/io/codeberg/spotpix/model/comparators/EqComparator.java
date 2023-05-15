package io.codeberg.spotpix.model.comparators;

import io.codeberg.spotpix.model.Color;

public interface EqComparator {
    boolean isEqual(Color color0,Color color1);
}
