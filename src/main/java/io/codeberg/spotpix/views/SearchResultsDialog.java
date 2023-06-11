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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import io.codeberg.spotpix.App;
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
            grid.add(new ImageItem(viewerRoot.getImageViewPanel(), image, this));
        }

        add(new JScrollPane(grid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        setSize(600, 600);
        setVisible(true);
    }
}

class ImageItem extends JPanel implements MouseListener {
    private ImageViewPanel imageViewPanel;
    private Image image;
    private SearchResultsDialog searchResultsDialog;

    public ImageItem(ImageViewPanel imageViewPanel, Image image, SearchResultsDialog searchResultsDialog) {
        this.imageViewPanel = imageViewPanel;
        this.image = image;
        this.searchResultsDialog = searchResultsDialog;
        setBorder(BorderFactory.createDashedBorder(Color.DARK_GRAY));
        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        imageViewPanel.paint(g, getHeight(), getWidth(), image);
        paintBorder(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        searchResultsDialog.dispose();
        App.main(image);
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
}
