package io.codeberg.spotpix.model.quantizers;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.images.Image;

public interface Quantizer {
    Image quantize(Image image,EqComparator compartor,ColorOp colorOp);
    double calcDistance(Color c1, Color c2);
}
