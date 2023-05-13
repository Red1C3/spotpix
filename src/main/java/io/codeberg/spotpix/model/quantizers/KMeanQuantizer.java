package io.codeberg.spotpix.model.quantizers;

import java.util.ArrayList;
import java.util.Random;

import io.codeberg.spotpix.model.KColor;
import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.Comparator;
import io.codeberg.spotpix.model.comparators.ManRGBComparator;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;

public class KMeanQuantizer implements Quantizer {
    int K = 64;

    Random rand = new Random();
    
    ArrayList<Color> centroids  = new ArrayList<>();
    ArrayList<ArrayList<KColor>> kMean = new ArrayList<ArrayList<KColor>>();
    ArrayList<KColor> allColors = new ArrayList<>();
    
    @Override
    public Image quantize(Image image, Comparator comparator, ColorOp colorOp) {
        int height = image.getHeight();
        int width = image.getWidth();

        int tx=0,ty=0;
        while (centroids.size()<K) {
            centroids.add(image.getPixel(tx, ty).getColor());
            kMean.add(new ArrayList<KColor>());
            tx+=rand.nextInt(width);
            ty+=rand.nextInt(height);
            tx%=width;
            ty%=height;
        }


        boolean[][] visited = new boolean[width][height];

        Pixel[] pixels = image.getRegion(image.getPixel(0, 0),new ManRGBComparator(255*3),visited);
        for (Pixel pixel : pixels) {
            if(allColors.contains(pixel.getColor()) || pixel.getX()<0 || pixel.getY()<0)
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

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Pixel pixel = image.getPixel(i, j);
                int x = pixel.getX();
                int y = pixel.getY();

                indices[x][y] = getClosestClustedId(pixel);
            }
        }

        IndexedImage indexedImage = new IndexedImage(centroids, indices, height, width);
        return indexedImage;
    }

    private void assignCluster(){
        for (KColor color : allColors) {
            double minDist=Double.MAX_VALUE;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = calcDistance(color, centroids.get(i));
                if(distance<minDist){
                    minDist=distance;
                    color.clusterIndex=i;
                }
            }
            kMean.get(color.clusterIndex).add(color);
        }
    }

    private int getClosestClustedId(Pixel pixel){
        Color color = pixel.getColor();
        double minDist=Integer.MAX_VALUE;
        int id=-1;
        for (int i = 0; i < centroids.size(); i++) {
            Color c = centroids.get(i);
            double distance = calcDistance(color, c);
            if(distance<minDist){
                minDist=distance;
                id=i;
            }
        }
        return id;
    }

    private void reCalcCeteroids() {
        ArrayList<KColor> lastFullCluster=null;
        for (int i = 0; i < centroids.size(); i++) {
            ArrayList<KColor> cluster = kMean.get(i);
            int n = cluster.size();
            if(n==0){
                if(lastFullCluster==null){
                    for (int j = i; j < centroids.size(); j++) {
                        ArrayList<KColor> currCluster = kMean.get(j);
                        if(currCluster.size()>0){
                            lastFullCluster=currCluster;
                            break;
                        }
                    }
                }
                centroids.set(i,setEmptyCentroids(lastFullCluster));
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
            
            centroids.set(i,new Color(sumA, sumR, sumG, sumB));
            kMean.set(i, new ArrayList<KColor>());
            lastFullCluster=cluster;
        }


    }

    private Color setEmptyCentroids(ArrayList<KColor> currCluster){
        KColor randColor=currCluster.get(rand.nextInt(currCluster.size()));
        int r=getRand(randColor.getRed());
        int g=getRand(randColor.getGreen());
        int b=getRand(randColor.getBlue());
        int a=getRand(randColor.getAlpha());
        return new Color(a, r, g, b);
    }

    private int getRand(int base){
        int returned=base+rand.nextInt(3);
        if(returned>=256)
            returned-=6;
        return returned;
    }

    private double channelDif(double c1,double c2, double alphas){
        double black = c1-c2, white = black+alphas;
        return Math.max(black*black, white*white);
    }

    private double calcDistanceRGB(Color color, Color c2) {
        double sum = 0;

        double alphas = (c2.getAlpha()/255)-(color.getAlpha()/255);
        sum += channelDif((double) color.getRed()/255.0,(double) c2.getRed()/255.0, alphas);
        sum += channelDif((double) color.getBlue()/255.0,(double) c2.getBlue()/255.0, alphas);
        sum += channelDif((double) color.getGreen()/255.0,(double) c2.getGreen()/255.0, alphas);

        return sum;
    }

    private double calcDistance(Color color, Color c2) {
        double sum = 0;
        double[] c1LAB=color.getLAB();
        double[] c2LAB=c2.getLAB();

        sum+=(c1LAB[0]-c2LAB[0])*(c1LAB[0]-c2LAB[0]);
        sum+=(c1LAB[1]-c2LAB[1])*(c1LAB[1]-c2LAB[1]);
        sum+=(c1LAB[2]-c2LAB[2])*(c1LAB[2]-c2LAB[2]);

        sum = Math.sqrt(sum);

        return sum;
    }
}
