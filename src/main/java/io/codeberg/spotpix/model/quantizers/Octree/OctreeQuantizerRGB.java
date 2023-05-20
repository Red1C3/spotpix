package io.codeberg.spotpix.model.quantizers.Octree;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;


public class OctreeQuantizerRGB extends OctreeQuantizer {
    public OctreeQuantizerRGB(int k) {
        super(k);
    }

    @Override
    public double calcDistance(Color c1, Color c2) {
        return Distances.calcDistanceRGB(c1, c2);
    }
}
