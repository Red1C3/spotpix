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
    // DATA: INDEX_TYPE_INDICES_ARRAY_WIDTH_MAJOR BYTE_RGB_COLOR_MAP
    // INT_QUANTIZE_AMOUNT
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
        int[][] indices = indexedImage.getIndices();

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

        int capacity = 3 + 3 * 4; // HEADER size

        switch (type) {
            case BYTE_TYPE:
                capacity += width * height;
                break;
            case SHORT_TYPE:
                capacity += width * height * 2;
                break;
            case INT_TYPE:
                capacity += width * height * 4;
                break;
        }

        capacity += colorMap.size() * 3;
        if (quantized) {
            capacity += colorMap.size() * 4;
        }
        ByteBuffer b = ByteBuffer.allocate(capacity);

        b.put(MAGIC_NUMBER);
        if (quantized) {
            b.put(QUANTIZED);
        } else {
            b.put(NON_QUANTIZED);
        }

        b.put(type);

        b.putInt(width);
        b.putInt(height);
        b.putInt(colorMap.size());

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                switch (type) {
                    case BYTE_TYPE:
                        b.put((byte) indices[i][j]);
                        break;
                    case SHORT_TYPE:
                        b.putShort((short) (indices[i][j]));
                        break;
                    case INT_TYPE:
                        b.putInt((int) (indices[i][j]));
                        break;
                }
            }
        }

        for (Color color : colorMap) {
            b.put((byte) color.getRed());
            b.put((byte) color.getGreen());
            b.put((byte) color.getBlue());
        }

        if (quantized) {
            int[] quantizationMap = indexedImage.getQuantizedMap();
            for (int i : quantizationMap) {
                b.putInt(i);
            }
        }

        return b.array();
    }

}
