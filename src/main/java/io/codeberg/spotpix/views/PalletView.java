package io.codeberg.spotpix.views;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import io.codeberg.spotpix.model.Color;

public class PalletView extends JPanel {
    private static PalletView palletView;
    private BufferedImage bufferedImage;

    private PalletView(){}
    public static PalletView instance(){
        if(palletView==null) palletView=new PalletView();
        return palletView;
    }
    public void createPallet(ArrayList<Color> colorMap) {
        bufferedImage = new BufferedImage(1, colorMap.size(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < colorMap.size(); i++) {
            bufferedImage.setRGB(0, i, colorMap.get(i).getARGB());
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (bufferedImage != null) {
            g.drawImage(bufferedImage, 0, 0, null);
        }
    }
}
