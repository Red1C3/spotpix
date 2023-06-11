package io.codeberg.spotpix.views;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.ImageView;

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
        grid.setLayout(new GridLayout((int) Math.ceil(images.size() / 8.0f), 8));

        for (Image image : images) {
            grid.add(new ImageItem(viewerRoot.getImageViewPanel(), image));
        }

        JScrollPane scrollPane = new JScrollPane(grid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
        setSize(800, 800);
        setVisible(true);
    }
}

class ImageItem extends JPanel {
    private ImageViewPanel imageViewPanel;
    private Image image;

    public ImageItem(ImageViewPanel imageViewPanel, Image image) {
        this.imageViewPanel = imageViewPanel;
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        imageViewPanel.paint(g, getHeight(), getWidth(), image);
    }
}
