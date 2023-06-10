package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.text.html.ImageView;

public class CropDialog extends JDialog implements ActionListener {
    private static final String CROP_STR="Crop";
    private static final String CANCEL_STR="Cancel";
    private ImagePanel imagePanel;
    private JButton cropButton,cancelButton;
    private ImageViewPanel imageViewPanel;
    private ViewerRoot viewerRoot;


    public CropDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Crop", ModalityType.APPLICATION_MODAL);
        this.viewerRoot=viewerRoot;
        this.imageViewPanel=viewerRoot.getImageViewPanel();
        setLayout(new BorderLayout());

        imagePanel = new ImagePanel(viewerRoot.getImageViewPanel());
        add(imagePanel,BorderLayout.CENTER);

        cropButton=new JButton(CROP_STR);
        cancelButton=new JButton(CANCEL_STR);
        cropButton.addActionListener(this);
        cancelButton.addActionListener(this);

        add(cropButton,BorderLayout.SOUTH);
        //add(cancelButton);

        setSize(400, 400);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==cropButton){
            int[] min=imagePanel.getMin();
            int[] max=imagePanel.getMax();
            imageViewPanel.crop(min, max);
            viewerRoot.repaint();
            dispose();
        }else if (e.getSource()==cancelButton){
            dispose();
        }
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
        int[] scaledMin = getScaledPoint(getSize().width, getSize().height,
                imgWidth, imgHeight, min);
        int[] scaledMax = getScaledPoint(getSize().width, getSize().height,
                imgWidth, imgHeight, max);
        g.fillRect(scaledMin[0], scaledMin[1],
                scaledMax[0] - scaledMin[0],
                scaledMax[1] - scaledMin[1]);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int[] point = new int[2];
        point[0] = e.getX();
        point[1] = e.getY();

        int[] localPoint = getScaledPoint(imgWidth, imgHeight,
                getSize().width, getSize().height, point);

        double disToMin = Math.sqrt(Math.pow(localPoint[0] - min[0], 2) +
                Math.pow(localPoint[1] - min[1], 2));
        double disToMax = Math.sqrt(Math.pow(localPoint[0] - max[0], 2) +
                Math.pow(localPoint[1] - max[1], 2));

        if (disToMax < disToMin) {
            max = localPoint;
        } else {
            min = localPoint;
        }

        repaint();
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

    private int[] getScaledPoint(int width, int height, int ogWidth, int ogHeight, int[] point) {
        int[] scaledPoint = new int[2];
        scaledPoint[0] = Math.round(((float) point[0] / (float) ogWidth) * (float) width);
        scaledPoint[1] = Math.round(((float) point[1] / (float) ogHeight) * (float) height);

        return scaledPoint;
    }
    public int[] getMin(){
        return min;
    }
    public int[] getMax(){
        return max;
    }
}