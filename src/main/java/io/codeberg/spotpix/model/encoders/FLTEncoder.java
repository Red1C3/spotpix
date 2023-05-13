package io.codeberg.spotpix.model.encoders;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;

public class FLTEncoder implements Encoder {
    // HEADER: MAGIC_NUMBER NON/QUANTIZED INDEX_TYPE
    // INT_IMAGE_WIDTH INT_IMAGE_HEIGHT INT_COLOR_MAP_LENGTH
    //
    // DATA: INDEX_TYPE_INDICES_ARRAY BYTE_COLOR_MAP INT_QUANTIZE_AMOUNT
    private static final byte MAGIC_NUMBER = 0x69;
    private static final byte NON_QUANTIZED = 0x0;
    private static final byte QUANTIZED = 0x1;
    private static final byte BYTE_TYPE = 0x1;
    private static final byte SHORT_TYPE = 0x2;
    private static final byte INT_TYPE = 0x4;

    @Override
    public byte[] encode(Image image) {
        IndexedImage indexedImage = image.toIndexedImage();

        boolean quantized = indexedImage.isQuantized();

        ArrayList<Color> colorMap = indexedImage.getColorMap();

        int maxIndex = colorMap.size() - 1;

        byte type;

        if (maxIndex < 256) {
            type = BYTE_TYPE;
        } else if (maxIndex < 65536) {
            type = SHORT_TYPE;
        } else {
            type = INT_TYPE;
        }

        int width = indexedImage.getWidth();
        int height = indexedImage.getHeight();

        ByteBuffer b = ByteBuffer.allocate(3+3*4);

        b.put(MAGIC_NUMBER);
        if(quantized){
            b.put(QUANTIZED);
        }else{
            b.put(NON_QUANTIZED);
        }

        b.put(type);

        b.putInt(width);
        b.putInt(height);
        b.putInt(colorMap.size());
        
        return b.array();
    }

}
