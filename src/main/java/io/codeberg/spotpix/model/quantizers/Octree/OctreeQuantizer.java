package io.codeberg.spotpix.model.quantizers.Octree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.codeberg.spotpix.model.Color;
import io.codeberg.spotpix.model.Distances;
import io.codeberg.spotpix.model.KColor;
import io.codeberg.spotpix.model.Pixel;
import io.codeberg.spotpix.model.colorOps.ColorOp;
import io.codeberg.spotpix.model.comparators.EqComparator;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.images.IndexedImage;
import io.codeberg.spotpix.model.quantizers.Quantizer;

public abstract class OctreeQuantizer implements Quantizer {
    int treeDepth;
    int K;
    Node root;
    public ArrayList<Color> colorMap = new ArrayList<>();
    ArrayList<KColor> allColors = new ArrayList<>();
    ArrayList<Node> leafs = new ArrayList<>();

    public OctreeQuantizer(int k) {
        treeDepth = (int) Math.ceil(Math.log10(k) / Math.log10(8)) + 1;
        K = k;
    }

    @Override
    public Image quantize(Image image, EqComparator comparator, ColorOp colorOp) {
        int height = image.getHeight();
        int width = image.getWidth();

        Pixel[] pixels = image.getFlattenPixels();
        for (Pixel pixel : pixels) {
            if (allColors.contains(pixel.getColor()) || pixel.getX() < 0 || pixel.getY() < 0)
                continue;
            else
                allColors.add(new KColor(pixel.getColor()));
        }
        root = getRoot();
        initTree(root, treeDepth);
        for (KColor color : allColors) {
            root.addColor(color);
        }
        for (Node leaf : leafs) {
            leaf.calcNColor();
        }

        while (!hasKColor()) {
            getMaxNode().expand();
        }

        creatColorMap();

        int[][] indices = new int[width][height];
        int[] quantizationMap = new int[colorMap.size()];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Pixel pixel = image.getPixel(i, j);
                int x = pixel.getX();
                int y = pixel.getY();

                int index = Distances.getClosestColorId(pixel, colorMap, this);
                indices[x][y] = index;
                quantizationMap[index] += 1;
            }
        }

        IndexedImage indexedImage = new IndexedImage(colorMap, indices, height, width, quantizationMap);
        return indexedImage;
    }

    public void creatColorMap() {
        Collections.sort(leafs, new leafComparator());
        for (int i = 0; i < K; i++) {
            ArrayList<KColor> colors = leafs.get(i).colors;
            int sumR = 0;
            int sumG = 0;
            int sumB = 0;
            int sumA = 0;

            for (KColor color : colors) {
                sumR += color.getRed();
                sumG += color.getGreen();
                sumB += color.getBlue();
                sumA += color.getAlpha();
                color.clusterIndex = colorMap.size();
            }

            sumR /= colors.size();
            sumG /= colors.size();
            sumB /= colors.size();
            sumA /= colors.size();

            Color curr = new Color(sumA, sumR, sumG, sumB);
            if (!colorMap.contains(curr))
                colorMap.add(curr);
        }
    }

    public Node getRoot() {

        int rM = Integer.MIN_VALUE, rm = Integer.MAX_VALUE;
        int gM = Integer.MIN_VALUE, gm = Integer.MAX_VALUE;
        int bM = Integer.MIN_VALUE, bm = Integer.MAX_VALUE;
        int aM = Integer.MIN_VALUE, am = Integer.MAX_VALUE;
        for (Color color : allColors) {
            rM = Math.max(rM, color.getRed());
            rm = Math.min(rm, color.getRed());

            gM = Math.max(gM, color.getGreen());
            gm = Math.min(gm, color.getGreen());

            bM = Math.max(bM, color.getBlue());
            bm = Math.min(bm, color.getBlue());

            aM = Math.max(aM, color.getAlpha());
            am = Math.min(am, color.getAlpha());
        }
        Node root;

        root = new Node(new Color(am, rm, gm, bm), new Color(aM, rM, gM, bM));

        return root;
    }

    public void initTree(Node node, int treeDepth) {
        if (treeDepth <= 0) {
            leafs.add(node);
            return;
        }
        for (int i = 0; i < 8; i++) {
            if (node.child == null)
                node.child = new Node[8];
            Node c = new Node(i, node);
            node.setChild(i, c);
            initTree(c, treeDepth - 1);
        }
    }

    public Node getMaxNode() {
        ArrayList<Node> del = new ArrayList<>();
        Node maxNode = leafs.get(0);
        for (int i = 0; i < leafs.size(); i++) {
            Node leaf = leafs.get(i);
            if (leaf.nColors <= 0) {
                leaf.prune();
                if (!del.contains(leaf))
                    del.add(leaf);
            } else if (leaf.nColors > maxNode.nColors && leaf.child == null)
                maxNode = leaf;
        }
        leafs.removeAll(del);
        return maxNode;
    }

    public Boolean hasKColor() {
        if (leafs.size() < K)
            return false;
        ArrayList<Node> curr = new ArrayList<Node>();
        ArrayList<Node> del = new ArrayList<Node>();
        int numberOfFilledLeafs = 0;
        for (int i = 0; i < leafs.size(); i++) {
            Node leaf = leafs.get(i);
            if (leaf.nColors <= 0) {
                leaf.prune();
                if (!del.contains(leaf))
                    del.add(leaf);
            } else if (!curr.contains(leaf)) {
                curr.add(leaf);
                numberOfFilledLeafs++;
            }
        }
        leafs.removeAll(del);
        return numberOfFilledLeafs >= K;
    }

    public abstract double calcDistance(Color c1, Color c2);

    private static class leafComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return (n1.nColors > n2.nColors) ? -1 : (n1.nColors < n2.nColors) ? 1 : 0;
        }
    }

    private class Node {
        Color midColor;
        // index is 3bit num which's MSB is 1 if blue is gt par_blue and LSB is 1 if red
        // is gt par_red and so on
        int index;
        Node parent;
        Node[] child;
        ArrayList<KColor> colors;
        int nColors;

        public Node(Color start, Color end) {
            int a = (int) Math.ceil((end.getAlpha() - start.getAlpha()) / 2.0);
            int r = (int) Math.ceil((end.getRed() - start.getRed()) / 2.0);
            int g = (int) Math.ceil((end.getBlue() - start.getBlue()) / 2.0);
            int b = (int) Math.ceil((end.getGreen() - start.getGreen()) / 2.0);
            midColor = new Color(a, r, g, b);
            parent = null;
            index = -1;
            child = null;
            colors = new ArrayList<KColor>();
        }

        public Node(int i, Node p) {
            int offsetR = (int) Math.ceil(p.midColor.getRed() / 2.0) * ((i & 1) >= 1 ? +1 : -1);
            int offsetG = (int) Math.ceil(p.midColor.getGreen() / 2.0) * ((i & 2) >= 1 ? +1 : -1);
            int offsetB = (int) Math.ceil(p.midColor.getBlue() / 2.0) * ((i & 4) >= 1 ? +1 : -1);
            midColor = new Color(p.midColor.getAlpha(), p.midColor.getRed() + offsetR, p.midColor.getGreen() + offsetG,
                    p.midColor.getBlue() + offsetB);
            parent = p;
            index = i;
            child = null;
            colors = new ArrayList<KColor>();
        }

        public void setChild(int i, Node node) {
            child[i] = node;
            return;
        }

        public void addColor(KColor color) {
            if (child == null) {
                colors.add(color);
                return;
            }
            int childIndex = getChildIndex(color);
            child[childIndex].addColor(color);
        }

        public int calcNColor() {
            nColors = colors.size();
            if (child != null) {
                for (int i = 0; i < 8; i++) {
                    if (child[i] == null)
                        continue;
                    int childNColors = child[i].calcNColor();
                    if (nColors > 0)
                        nColors += childNColors;
                }
            }
            return nColors;
        }

        public void prune() {
            if (parent == null)
                return;
            parent.colors = this.colors;
            parent.child[index] = null;
            colors.clear();
            parent.calcNColor();
        }

        public void expand() {
            child = new Node[8];
            for (int i = 0; i < 8; i++) {
                Node c = new Node(i, this);
                setChild(i, c);
                leafs.add(child[i]);
            }
            for (KColor color : colors) {
                addColor(color);
            }
            colors.clear();
            calcNColor();
        }

        private int getChildIndex(KColor color) {
            int index = 1 * (color.getRed() > midColor.getRed() ? 1 : 0);
            index += 2 * (color.getGreen() > midColor.getGreen() ? 1 : 0);
            index += 4 * (color.getBlue() > midColor.getBlue() ? 1 : 0);
            return index;
        }
    }
}
