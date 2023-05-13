package io.codeberg.spotpix.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.comparators.ManRGBComparator;
import io.codeberg.spotpix.model.comparators.RGBComparator;
import io.codeberg.spotpix.model.decoders.FLTDecoder;
import io.codeberg.spotpix.model.decoders.JDecoder;
import io.codeberg.spotpix.model.encoders.Encoder;
import io.codeberg.spotpix.model.encoders.FLTEncoder;
import io.codeberg.spotpix.model.encoders.JEncoder;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;
import io.codeberg.spotpix.model.quantizers.AvgRGBQuantizer;

public class DummyCtrlr {
    public BufferedImage getImage() {
        String pathToImg = "./Assets/grumpy-small.png";
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(pathToImg));
        } catch (IOException e) {
            System.out.printf("Failed to read image bytes at %s", pathToImg);
            e.printStackTrace();
            return new BufferedImage(0, 0, BufferedImage.TYPE_3BYTE_BGR);
        }
        Image img = (new JDecoder()).decode(bytes);
        Image quantized= (new AvgRGBQuantizer()).quantize(img, new ManRGBComparator(70), null);


        saveToDrive("./Assets/quantized.png", quantized,new JEncoder());

        return quantized.toBufferedImage();
    }

    public BufferedImage getIndexedImage() {
        Color black = new Color(0xFF000000);
        Color white = new Color(0xFFFFFF);
        ArrayList<Color> colorMap = new ArrayList<>();
        colorMap.add(black);
        colorMap.add(white);

        int[][] indices = new int[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (i % 2 == 0) {
                    indices[i][j] = 0;
                } else {
                    indices[i][j] = 1;
                }
            }
        }

        IndexedImage img = (new IndexedImage(colorMap, indices, 100, 100));

        return (new AvgRGBQuantizer()).quantize(img, new RGBComparator(), null).toBufferedImage();
    }
    public void saveToDrive(String path,Image img,Encoder encoder){
        byte[] output=encoder.encode(img);
        try {
            Files.write(Paths.get(path), output);
        } catch (IOException e) {
            System.out.println("Failed to print quantized image to a file");
            e.printStackTrace();
        }
    }

    public BufferedImage testFLT(){
        String pathToImg = "./Assets/grumpy-small.png";
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(pathToImg));
        } catch (IOException e) {
            System.out.printf("Failed to read image bytes at %s", pathToImg);
            e.printStackTrace();
            return new BufferedImage(0, 0, BufferedImage.TYPE_3BYTE_BGR);
        }
        Image img = (new JDecoder()).decode(bytes);

        byte[] fltBytes=(new FLTEncoder()).encode(img);
        Image img0=(new FLTDecoder()).decode(fltBytes);

        return img0.toBufferedImage();
    }

}
