package io.codeberg.spotpix.model.colorOps;

import io.codeberg.spotpix.model.Color;

public interface ColorOp {
    Color op(Color color0,Color color1);
}
