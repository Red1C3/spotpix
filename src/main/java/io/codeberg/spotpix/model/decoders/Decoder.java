package io.codeberg.spotpix.model.decoders;

import java.nio.file.attribute.FileTime;

import io.codeberg.spotpix.model.images.Image;

public interface Decoder {
    Image decode(byte[] bytes,FileTime fileTime);
}
