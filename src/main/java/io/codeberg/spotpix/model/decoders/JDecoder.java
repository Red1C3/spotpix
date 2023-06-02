package io.codeberg.spotpix.model.decoders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.images.ByteImage;
import io.codeberg.spotpix.model.images.Image;

public class JDecoder implements Decoder {

    @Override
    public Image decode(byte[] bytes) throws Exception {
        InputStream byteStream = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(byteStream);
            byteStream.close();
        } catch (IOException e) {
            // Not even IOing
        }
        if(bufferedImage==null){
            throw new Exception("Failed to read image file");
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        Color[][] colors = new Color[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int argb = bufferedImage.getRGB(i, j);
                colors[i][j] = new Color(argb);
            }
        }

        return new ByteImage(colors, height, width);
    }

}
