package io.codeberg.spotpix.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;

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

    public ImageCtrlr(String path) throws Exception {
        byte[] bytes = null;
        FileTime creationDate = null;
        try {
            bytes = Files.readAllBytes(Paths.get(path));
            creationDate = (FileTime) Files.getAttribute(Paths.get(path), "creationTime");
        } catch (IOException e) {
            System.out.printf("Failed to read image bytes at %s", path);
            e.printStackTrace();
        }

        if (bytes[0] == FLTDecoder.MAGIC_NUMBER) {
            image = (new FLTDecoder()).decode(bytes, creationDate);
        } else {
            image = (new JDecoder()).decode(bytes, creationDate);
        }
    }

    public ImageCtrlr(Image image) {
        this.image = image;
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
        Image quantizedImage = null;
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
        Image quantizedImage = null;
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
        Image quantizedImage = null;
        if (colorSystem == ColorSystem.RGB) {
            Quantizer quantizer = new AvgRGBQuantizer();
            quantizedImage = quantizer.quantize(image, comparator, colorOp);
        }
        App.main(quantizedImage);
        // No other systems are supported yet
    }

    public void octreeQuantize(int colorsCount, ColorSystem colorSystem) {
        Image quantizedImage = null;
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
        image = image.toIndexedImage();
        IndexedImage indexedImage = (IndexedImage) image;
        return indexedImage.getColorMap();
    }

    public int[] getQuantizationMap() {
        image = image.toIndexedImage();
        IndexedImage indexedImage = (IndexedImage) image;
        if (!indexedImage.isQuantized()) {
            indexedImage.calculateQuantizationMap();
        }
        return indexedImage.getQuantizedMap();
    }

    public int[] getRedChannel() {
        image = image.toIndexedImage();
        IndexedImage indexedImage = (IndexedImage) image;
        return indexedImage.getRedChannel();
    }

    public int[] getGreenChannel() {
        image = image.toIndexedImage();
        IndexedImage indexedImage = (IndexedImage) image;
        return indexedImage.getGreenChannel();
    }

    public int[] getBlueChannel() {
        image = image.toIndexedImage();
        IndexedImage indexedImage = (IndexedImage) image;
        return indexedImage.getBlueChannel();
    }

    public int getFileSize() {
        return image.getFileSize();
    }

    public void crop(int[] min, int[] max) {
        image = image.crop(min, max);
    }

    public void nearestScale(float ratio) {
        image = image.nearestFilter(ratio);
    }

    public void linearScale(float ratio) {
        image = image.linearFilter(ratio);
    }
    public FileTime getFileTime(){
        return image.getFileTime();
    }
}