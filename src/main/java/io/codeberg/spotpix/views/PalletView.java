package io.codeberg.spotpix.views;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.codeberg.spotpix.model.Color;

public class PalletView extends JPanel {
    private BufferedImage bufferedImage;

    private PalletView() {
        super();
    }

    public static void createPallet(ArrayList<Color> colorMap) {
        JFrame palletFrame=new JFrame("Color Pallet");
        palletFrame.setSize(100, 500);
        PalletView palletView=new PalletView();
        palletView.bufferedImage = new BufferedImage(1, colorMap.size(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < colorMap.size(); i++) {
            palletView.bufferedImage.setRGB(0, i, colorMap.get(i).getARGB());
        }
        palletFrame.add(palletView);
        palletFrame.setLocationRelativeTo(null);
        palletFrame.setVisible(true);
    }

    public void reset() {
        bufferedImage = null;
    }

    @Override
    public void paint(Graphics g) {
        if (bufferedImage != null) {
            g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
