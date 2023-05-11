package io.codeberg.spotpix.model.images;

import java.awt.image.BufferedImage;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.ColorSpace;

public class IndexedImage implements Image {
    private Color[] colorMap;
    private int[][] indices;
    private int height,width;
    private ColorSpace colorSpace;
    public IndexedImage(Color[] colorMap,int[][] indices,int height,int width,ColorSpace colorSpace){
        this.colorMap=colorMap;
        this.indices=indices;
        this.height=height;
        this.width=width;
        this.colorSpace=colorSpace;
    }
    public IndexedImage(Color[] colorMap,int[][] indices,int height,int width){
        this.colorMap=colorMap;
        this.indices=indices;
        this.height=height;
        this.width=width;
        this.colorSpace=ColorSpace.LINEAR;
    }

    public ByteImage toByteImage(){
        Color[][] colors=new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = colorMap[indices[i][j]];
            }
        }
        return new ByteImage(colors, height, width,colorSpace);
    }
    @Override
    public BufferedImage toBufferedImage() {
        return toByteImage().toBufferedImage();
    }
    
}
