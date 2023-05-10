package io.codeberg.spotpix.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.codeberg.spotpix.model.decoders.JDecoder;
import io.codeberg.spotpix.model.images.Image;  

public class DummyCtrlr {
    public BufferedImage getImage(){
        String pathToImg="./Assets/grumpy.jpg";
        byte[] bytes=null;
        try {
            bytes=Files.readAllBytes(Paths.get(pathToImg));
        } catch (IOException e) {
            System.out.printf("Failed to read image bytes at %s",pathToImg);
            e.printStackTrace();
            return new BufferedImage(0, 0, BufferedImage.TYPE_3BYTE_BGR);
        }
        Image img=(new JDecoder()).decode(bytes);
        return img.toBufferedImage();
    }   
}
