package io.codeberg.spotpix.model.decoders;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.images.ByteImage;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;

public class FLTDecoder implements Decoder {
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
    public Image decode(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        byte magicNumber = buffer.get();
        if (magicNumber != MAGIC_NUMBER) {
            System.out.println("Magic numbers does not match");
            return new ByteImage(null, 0, 0);
        }

        byte quantizedByte = buffer.get();

        boolean quantized;
        if (quantizedByte == QUANTIZED) {
            quantized = true;
        } else if (quantizedByte == NON_QUANTIZED) {
            quantized = false;
        } else {
            System.out.println("quantization flag unknown");
            return new ByteImage(null, 0, 0);
        }

        byte indexType = buffer.get();

        int width = buffer.getInt();
        int height = buffer.getInt();
        int colorMapLength = buffer.getInt();

        int[][] indices = new int[width][height];

        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                switch (indexType) {
                    case BYTE_TYPE:
                        indices[i][j] = toUnsignedInt(buffer.get());
                        break;
                    case SHORT_TYPE:
                        indices[i][j] = toUnsignedInt(buffer.getShort());
                        break;
                    case INT_TYPE:
                        indices[i][j] = buffer.getInt();
                        break;
                }
            }
        }

        ArrayList<Color> colorMap = new ArrayList<>(colorMapLength);
        for (int i = 0; i < colorMapLength; i++) {
            int red = (int) (buffer.get()& 0xffffffffL);
            int green = (int) (buffer.get()& 0xffffffffL);
            int blue = (int) (buffer.get()& 0xffffffffL);
            colorMap.add(new Color(255, red, green, blue));
        }

        if (quantized) {
            int[] quantizationMap = new int[colorMapLength];
            for (int i = 0; i < colorMapLength; i++) {
                quantizationMap[i] = buffer.getInt();
            }

            // TODO return an indexed image with quantization array
        }

        return new IndexedImage(colorMap, indices, height, width);
    }

    private int toUnsignedInt(short i){
        int x= (i&0xFFFF);
        return x;
    }
    private int toUnsignedInt(byte i){
        int x=(i&0xFF);
        return x;
    }
}
