package io.codeberg.spotpix.model.encoders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import io.codeberg.spotpix.model.images.Image;

public class JEncoder implements Encoder {

    @Override
    public byte[] encode(Image image) {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        try {
            //TODO make sure it's saving an indexed image or force it to or write your own encoder
            ImageIO.write(image.toBufferedImage(), "png", outputStream);
            outputStream.close();
        } catch (IOException e) {
            //Probably should never raise it
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
    
}
