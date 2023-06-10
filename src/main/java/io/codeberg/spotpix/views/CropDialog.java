package io.codeberg.spotpix.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.text.html.ImageView;

public class CropDialog extends JDialog {
    private ImagePanel imagePanel;

    public CropDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Crop", ModalityType.APPLICATION_MODAL);

        imagePanel = new ImagePanel(viewerRoot.getImageViewPanel());
        add(imagePanel);

        setSize(400, 400);
        setVisible(true);
    }
}

class ImagePanel extends JPanel implements MouseListener {
    private ImageViewPanel imageViewPanel;
    private int[] min;
    private int[] max;
    private int imgHeight, imgWidth;

    public ImagePanel(ImageViewPanel imageViewPanel) {
        super();
        min = new int[2];
        max = new int[2];
        this.imageViewPanel = imageViewPanel;
        imgHeight = imageViewPanel.getImgHeight();
        imgWidth = imageViewPanel.getImgWidth();

        min[0] = (int) (imgWidth / 2 - imgWidth * 0.1f);
        min[1] = (int) (imgHeight / 2 - imgHeight * 0.1f);

        max[0] = (int) (imgWidth / 2 + imgWidth * 0.1f);
        max[1] = (int) (imgHeight / 2 + imgHeight * 0.1f);

        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        imageViewPanel.paint(g, getSize().height, getSize().width);
        g.setColor(new Color(0, 0, 0, 100));
        int[] scaledMinMax = getScaledMinMax(getSize().width, getSize().height);
        g.fillRect(scaledMinMax[0], scaledMinMax[1],
                scaledMinMax[2] - scaledMinMax[0],
                scaledMinMax[3] - scaledMinMax[1]);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private int[] getScaledMinMax(int width, int height) {
        int[] scaledMinMax = new int[4];
        scaledMinMax[0] = Math.round(((float) min[0] / (float) imgWidth) * (float) width);
        scaledMinMax[1] = Math.round(((float) min[1] / (float) imgHeight) * (float) height);
        scaledMinMax[2] = Math.round(((float) max[0] / (float) imgWidth) * (float) width);
        scaledMinMax[3] = Math.round(((float) max[1] / (float) imgHeight) * (float) height);

        return scaledMinMax;
    }
}