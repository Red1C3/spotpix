package io.codeberg.spotpix.model.quantizers.Octree;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;


public class OctreeQuantizerLAB extends OctreeQuantizer {
    public OctreeQuantizerLAB(int k) {
        super(k);
    }

    @Override
    public double calcDistance(Color c1, Color c2) {
        return Distances.calcDistanceLAB(c1, c2);
    }
}
