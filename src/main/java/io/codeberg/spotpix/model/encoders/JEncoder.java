package io.codeberg.spotpix.model.encoders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.codeberg.spotpix.model.images.Image;

public class JEncoder implements Encoder {

    @Override
    public byte[] encode(Image image) {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        try {
            ImageIO.write(image.toBufferedImage(), "png", outputStream);
            outputStream.close();
        } catch (IOException e) {
            //Probably should never raise it
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
    
}
