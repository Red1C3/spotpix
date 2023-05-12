package io.codeberg.spotpix.model.quantizers;

import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.Comparator;
import io.codeberg.spotpix.model.images.Image;

public interface Quantizer {
    Image quantize(Image image,Comparator compartor,ColorOp colorOp);
}
