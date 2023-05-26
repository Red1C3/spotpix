package io.codeberg.spotpix.model.quantizers.KMean;

import java.util.ArrayList;
import java.util.Random;

import io.codeberg.spotpix.model.KColor;
import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;
import io.codeberg.spotpix.model.quantizers.Quantizer;

public abstract class KMeanQuantizer implements Quantizer {
    int K;

    public KMeanQuantizer(int k) {
        super();
        K = k;
    }

    Random rand = new Random();

    // centroids are the colorMap 
    ArrayList<Color> centroids = new ArrayList<>();
    ArrayList<ArrayList<KColor>> kMean = new ArrayList<ArrayList<KColor>>();
    ArrayList<KColor> allColors = new ArrayList<>();

    @Override
    public Image quantize(Image image, EqComparator comparator, ColorOp colorOp) {
        int height = image.getHeight();
        int width = image.getWidth();

        int tx = 0, ty = 0;
        while (centroids.size() < K) {
            centroids.add(image.getPixel(tx, ty).getColor());
            kMean.add(new ArrayList<KColor>());
            tx += rand.nextInt(width);
            ty += rand.nextInt(height);
            tx %= width;
            ty %= height;
        }

        Pixel[] pixels = image.getFlattenPixels();
        for (Pixel pixel : pixels) {
            if (allColors.contains(pixel.getColor()) || pixel.getX() < 0 || pixel.getY() < 0)
                continue;
            else
                allColors.add(new KColor(pixel.getColor()));
        }
        assignCluster();
        for (int i = 0; i < 20; i++) {
            reCalcCeteroids();
            assignCluster();
        }

        int[][] indices = new int[width][height];
        int[] quantizationMap = new int[centroids.size()];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Pixel pixel = image.getPixel(i, j);
                int x = pixel.getX();
                int y = pixel.getY();

                int index = Distances.getClosestColorId(pixel,centroids,this);
                indices[x][y] = index;
                quantizationMap[index] += 1;
            }
        }

        IndexedImage indexedImage = new IndexedImage(centroids, indices, height, width, quantizationMap,image.getFileSize(),image.getFileTime());
        return indexedImage;
    }

    private void assignCluster() {
        for (KColor color : allColors) {
            double minDist = Double.MAX_VALUE;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = calcDistance(color, centroids.get(i));
                if (distance < minDist) {
                    minDist = distance;
                    color.clusterIndex = i;
                }
            }
            kMean.get(color.clusterIndex).add(color);
        }
    }

    private void reCalcCeteroids() {
        ArrayList<KColor> lastFullCluster = null;
        for (int i = 0; i < centroids.size(); i++) {
            ArrayList<KColor> cluster = kMean.get(i);
            int n = cluster.size();
            if (n == 0) {
                if (lastFullCluster == null) {
                    for (int j = i; j < centroids.size(); j++) {
                        ArrayList<KColor> currCluster = kMean.get(j);
                        if (currCluster.size() > 0) {
                            lastFullCluster = currCluster;
                            break;
                        }
                    }
                }
                centroids.set(i, setEmptyCentroids(lastFullCluster));
                kMean.set(i, new ArrayList<KColor>());
                continue;
            }
            int sumR = 0;
            int sumG = 0;
            int sumB = 0;
            int sumA = 0;

            for (KColor color : cluster) {
                sumR += color.getRed();
                sumG += color.getGreen();
                sumB += color.getBlue();
                sumA += color.getAlpha();
            }

            sumR /= n;
            sumG /= n;
            sumB /= n;
            sumA /= n;

            centroids.set(i, new Color(sumA, sumR, sumG, sumB));
            kMean.set(i, new ArrayList<KColor>());
            lastFullCluster = cluster;
        }

    }

    private Color setEmptyCentroids(ArrayList<KColor> currCluster) {
        KColor randColor = currCluster.get(rand.nextInt(currCluster.size()));
        int r = getRand(randColor.getRed());
        int g = getRand(randColor.getGreen());
        int b = getRand(randColor.getBlue());
        int a = getRand(randColor.getAlpha());
        return new Color(a, r, g, b);
    }

    private int getRand(int base) {
        int returned = base + rand.nextInt(3);
        if (returned >= 256)
            returned -= 6;
        return returned;
    }
}
