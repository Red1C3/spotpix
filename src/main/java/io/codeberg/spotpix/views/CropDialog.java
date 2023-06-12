package io.codeberg.spotpix.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.html.ImageView;

public class CropDialog extends JDialog implements ActionListener {
    private static final String CROP_STR = "Crop";
    private ImagePanel imagePanel;
    private JButton cropButton;
    private ImageViewPanel imageViewPanel;
    private ViewerRoot viewerRoot;

    public CropDialog(ViewerRoot viewerRoot) {
        super(viewerRoot, "Crop", ModalityType.APPLICATION_MODAL);
        this.viewerRoot = viewerRoot;
        this.imageViewPanel = viewerRoot.getImageViewPanel();
        setLayout(new BorderLayout());

        imagePanel = new ImagePanel(viewerRoot.getImageViewPanel());
        add(imagePanel, BorderLayout.CENTER);

        cropButton = new JButton(CROP_STR);
        cropButton.addActionListener(this);

        JPanel southPanel=new JPanel();
        southPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        southPanel.add(cropButton);
        add(southPanel, BorderLayout.SOUTH);
        

        setSize(400, 400);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cropButton) {
            int[] min = imagePanel.getMin();
            int[] max = imagePanel.getMax();
            if(min[0]==max[0] || min[1]==max[1]){
                JOptionPane.showMessageDialog(this, "Invalid selection area");
                return;
            }
            imageViewPanel.crop(min, max);
            viewerRoot.repaint();
            dispose();
        }
    }
}

class ImagePanel extends JPanel implements MouseListener {
    private ImageViewPanel imageViewPanel;
    private int[] min;
    private int[] max;
    private int imgHeight, imgWidth;
    private int[] dims;

    public ImagePanel(ImageViewPanel imageViewPanel) {
        super();
        min = new int[2];
        max = new int[2];
        this.imageViewPanel = imageViewPanel;
        imgHeight = imageViewPanel.getImgHeight();
        imgWidth = imageViewPanel.getImgWidth();

        min[0] = 0;
        min[1] = 0;

        max[0] = imgWidth;
        max[1] = imgHeight;

        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        dims = imageViewPanel.paint(g, getSize().height, getSize().width);
        g.setColor(new Color(0, 0, 0, 100));
        int[] scaledMin = getScaledPoint(dims[0], dims[1],
                imgWidth, imgHeight, min);
        int[] scaledMax = getScaledPoint(dims[0], dims[1],
                imgWidth, imgHeight, max);
        g.fillRect(dims[2] + scaledMin[0], dims[3] + scaledMin[1],
                scaledMax[0] - scaledMin[0],
                scaledMax[1] - scaledMin[1]);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int[] point = new int[2];
        point[0] = e.getX();
        point[1] = e.getY();

        point[0] -= dims[2];
        point[1] -= dims[3];

        int[] localPoint = getScaledPoint(imgWidth, imgHeight,
                dims[0], dims[1], point);

        double disToMin = Math.sqrt(Math.pow(localPoint[0] - min[0], 2) +
                Math.pow(localPoint[1] - min[1], 2));
        double disToMax = Math.sqrt(Math.pow(localPoint[0] - max[0], 2) +
                Math.pow(localPoint[1] - max[1], 2));

        if (disToMax < disToMin) {
            max = localPoint;
        } else {
            min = localPoint;
        }
        if(min[0]>max[0]){
            int temp=min[0];
            min[0]=max[0];
            max[0]=temp;
        }
        if(min[1]>max[1]){
            int temp=min[1];
            min[1]=max[1];
            max[1]=temp;
        }

        min[0]=Math.min(imgWidth,Math.max(0,min[0]));
        max[0]=Math.min(imgWidth,Math.max(0,max[0]));
        min[1]=Math.min(imgHeight,Math.max(0,min[1]));
        max[1]=Math.min(imgHeight,Math.max(0,max[1]));

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

    public int[] getMin() {
        return min;
    }

    public int[] getMax() {
        return max;
    }
}