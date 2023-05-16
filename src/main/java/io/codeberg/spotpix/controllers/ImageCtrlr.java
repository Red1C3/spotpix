package io.codeberg.spotpix.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.codeberg.spotpix.model.decoders.FLTDecoder;
import io.codeberg.spotpix.model.decoders.JDecoder;
import io.codeberg.spotpix.model.images.Image;

public class ImageCtrlr {
    private Image image;

    public ImageCtrlr(String path) {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            System.out.printf("Failed to read image bytes at %s", path);
            e.printStackTrace();
        }
        
        if (bytes[0] == FLTDecoder.MAGIC_NUMBER) {
            image = (new FLTDecoder()).decode(bytes);
        } else {
            image = (new JDecoder()).decode(bytes);
        }
    }
}