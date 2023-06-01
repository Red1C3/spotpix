package io.codeberg.spotpix.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import io.codeberg.spotpix.App;
import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.decoders.FLTDecoder;
import io.codeberg.spotpix.model.decoders.JDecoder;
import io.codeberg.spotpix.model.encoders.FLTEncoder;
import io.codeberg.spotpix.model.encoders.JEncoder;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;
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

    public ImageCtrlr(Image image){
        this.image=image;
    }

    public BufferedImage getBufferedImage() {
        return image.toBufferedImage();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getWidth() {
        return image.getWidth();
    }

    public void kMeanQuantize(int colorsCount, ColorSystem colorSystem) {
        Image quantizedImage=null;
        if (colorSystem == ColorSystem.RGB) {
            Quantizer quantizer = new KMeanQuantizerRGB(colorsCount);
            quantizedImage = quantizer.quantize(image, null, null);
        } else if (colorSystem == ColorSystem.LAB) {
            Quantizer quantizer = new KMeanQuantizerLAB(colorsCount);
            quantizedImage = quantizer.quantize(image, null, null);
        }
        App.main(quantizedImage);
    }

    public void medianCutQuantize(int colorsCount, ColorSystem colorSystem) {
        Image quantizedImage=null;
        if (colorSystem == ColorSystem.RGB) {
            Quantizer quantizer = new MedianCutQuantizerRGB(colorsCount);
            quantizedImage = quantizer.quantize(image, null, null);
        } else if (colorSystem == ColorSystem.LAB) {
            Quantizer quantizer = new MedianCutQuantizerLAB(colorsCount);
            quantizedImage = quantizer.quantize(image, null, null);
        }
        App.main(quantizedImage);
    }

    public void avgQuantize(ColorSystem colorSystem, EqComparator comparator, ColorOp colorOp) {
        Image quantizedImage=null;
        if (colorSystem == ColorSystem.RGB) {
            Quantizer quantizer = new AvgRGBQuantizer();
            quantizedImage = quantizer.quantize(image, comparator, colorOp);
        }
        App.main(quantizedImage);
        // No other systems are supported yet
    }

    public void octreeQuantize(int colorsCount, ColorSystem colorSystem) {
        Image quantizedImage=null;
        if (colorSystem == ColorSystem.RGB) {
            Quantizer quantizer = new OctreeQuantizerRGB(colorsCount);
            quantizedImage = quantizer.quantize(image, null, null);
        } else if (colorSystem == ColorSystem.LAB) {
            Quantizer quantizer = new OctreeQuantizerLAB(colorsCount);
            quantizedImage = quantizer.quantize(image, null, null);
        }
        App.main(quantizedImage);
    }

    public void saveImage(String path, ImageFormat format) {
        if (format == ImageFormat.FLT) {
            byte[] bytes = (new FLTEncoder()).encode(image);
            try {
                Files.write(Paths.get(path), bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            byte[] bytes = (new JEncoder()).encode(image);
            try {
                Files.write(Paths.get(path), bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Color> getColorMap() {
        if (image instanceof IndexedImage) {
            return ((IndexedImage) image).getColorMap();
        }
        return null;
    }

    public int[] getQuantizationMap() {
        if (image instanceof IndexedImage) {
            IndexedImage indexedImage = (IndexedImage) image;
            if (indexedImage.isQuantized()) {
                return indexedImage.getQuantizedMap();
            }
        }
        return null;
    }

    public int[] getRedChannel() {
        if (image instanceof IndexedImage) {
            IndexedImage indexedImage = (IndexedImage) image;
            return indexedImage.getRedChannel();
        }
        return null;
    }

    public int[] getGreenChannel() {
        if (image instanceof IndexedImage) {
            IndexedImage indexedImage = (IndexedImage) image;
            return indexedImage.getGreenChannel();
        }
        return null;
    }

    public int[] getBlueChannel() {
        if (image instanceof IndexedImage) {
            IndexedImage indexedImage = (IndexedImage) image;
            return indexedImage.getBlueChannel();
        }
        return null;
    }
}