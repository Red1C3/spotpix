package io.codeberg.spotpix.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.ColorSpace;
import io.codeberg.spotpix.model.decoders.JDecoder;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;  

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
    public BufferedImage getIndexedImage(){
        Color black=new Color(0xFF000000);
        Color white=new Color(0xFFFFFF);
        Color[] colorMap=new Color[2];
        colorMap[0]=black;
        colorMap[1]=white;

        int[][] indices=new int[100][100];
        for(int i=0;i<100;i++){
            for(int j=0;j<100;j++){
                if(i%2==0){
                    indices[i][j]=0;
                }else{
                    indices[i][j]=1;
                }
            }
        }

        return (new IndexedImage(colorMap, indices, 100, 100,ColorSpace.sRGB)).toBufferedImage();

    }
}
