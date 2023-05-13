package io.codeberg.spotpix.model.encoders;

import io.codeberg.spotpix.model.images.Image;

public interface Encoder {
    byte[] encode(Image image);
}
