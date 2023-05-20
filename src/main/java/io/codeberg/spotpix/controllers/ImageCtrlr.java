package io.codeberg.spotpix.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.decoders.FLTDecoder;
import io.codeberg.spotpix.model.decoders.JDecoder;
import io.codeberg.spotpix.model.encoders.FLTEncoder;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.quantizers.AvgRGBQuantizer;
import io.codeberg.spotpix.model.quantizers.Quantizer;
import io.codeberg.spotpix.model.quantizers.KMean.KMeanQuantizerLAB;
import io.codeberg.spotpix.model.quantizers.KMean.KMeanQuantizerRGB;
import io.codeberg.spotpix.model.quantizers.MedianCut.MedianCutQuantizerLAB;
import io.codeberg.spotpix.model.quantizers.MedianCut.MedianCutQuantizerRGB;
import io.codeberg.spotpix.model.quantizers.Octree.OctreeQuantizerLAB;
import io.codeberg.spotpix.model.quantizers.Octree.OctreeQuantizerRGB;
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
    public BufferedImage getBufferedImage(){
        return image.toBufferedImage();
    }
    public int getHeight(){
        return image.getHeight();
    }

    public int getWidth(){
        return image.getWidth();
    }
    public void kMeanQuantize(int colorsCount,ColorSystem colorSystem){
        if(colorSystem==ColorSystem.RGB){
            Quantizer quantizer=new KMeanQuantizerRGB(colorsCount);
            image=quantizer.quantize(image, null, null);
        }else if (colorSystem==ColorSystem.LAB){
            Quantizer quantizer=new KMeanQuantizerLAB(colorsCount);
            image=quantizer.quantize(image, null, null);
        }
    }
    public void medianCutQuantize(int colorsCount,ColorSystem colorSystem){
        if(colorSystem==ColorSystem.RGB){
            Quantizer quantizer= new MedianCutQuantizerRGB(colorsCount);
            image=quantizer.quantize(image, null, null);
        }else if (colorSystem==ColorSystem.LAB){
            Quantizer quantizer= new MedianCutQuantizerLAB(colorsCount);
            image=quantizer.quantize(image, null, null);
        }
    }
    public void avgQuantize(ColorSystem colorSystem,EqComparator comparator,ColorOp colorOp){
        if(colorSystem==ColorSystem.RGB){
            Quantizer quantizer=new AvgRGBQuantizer();
            image=quantizer.quantize(image, comparator, colorOp);
        }
        //No other systems are supported yet
    }
    public void octreeQuantize(int colorsCount,ColorSystem colorSystem){
        if(colorSystem==ColorSystem.RGB){
            Quantizer quantizer=new OctreeQuantizerRGB(colorsCount);
            image=quantizer.quantize(image, null, null);
        }else if (colorSystem==ColorSystem.LAB){
            Quantizer quantizer=new OctreeQuantizerLAB(colorsCount);
            image=quantizer.quantize(image, null, null);
        }
    }
    public void saveImage(String path,ImageFormat format){
        if(format==ImageFormat.FLT){
            byte[] bytes=(new FLTEncoder()).encode(image);
            try{
                Files.write(Paths.get(path), bytes);
            }catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
}