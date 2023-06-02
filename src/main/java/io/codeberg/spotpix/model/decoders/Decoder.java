package io.codeberg.spotpix.model.decoders;

import io.codeberg.spotpix.model.images.Image;

public interface Decoder {
    Image decode(byte[] bytes) throws Exception;
}
