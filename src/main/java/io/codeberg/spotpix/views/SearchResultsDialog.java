package io.codeberg.spotpix.views;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.ImageView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import io.codeberg.spotpix.model.images.Image;

public class SearchResultsDialog extends JDialog {
    private static final String SEARCH_RES_STR = "Search Results";
    public static final int imagePanelSize = 100;
    private ViewerRoot viewerRoot;
    private ArrayList<Image> images;

    public SearchResultsDialog(ViewerRoot viewerRoot, ArrayList<Image> images) {
        super(viewerRoot, SEARCH_RES_STR, ModalityType.APPLICATION_MODAL);
        this.viewerRoot = viewerRoot;
        this.images = images;
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout((int) Math.ceil(images.size() / 6.0f), 6, 5, 5));

        for (Image image : images) {
            grid.add(new ImageItem(viewerRoot.getImageViewPanel(), image));
        }

        add(new JScrollPane(grid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        setSize(600, 600);
        setVisible(true);
    }
}

class ImageItem extends JPanel {
    private ImageViewPanel imageViewPanel;
    private Image image;

    public ImageItem(ImageViewPanel imageViewPanel, Image image) {
        this.imageViewPanel = imageViewPanel;
        this.image = image;
        setBorder(BorderFactory.createDashedBorder(Color.DARK_GRAY));
    }

    @Override
    public void paint(Graphics g) {
        imageViewPanel.paint(g, getHeight(), getWidth(), image);
        paintBorder(g);
    }
}
