package io.codeberg.spotpix.views;

import javax.swing.JFrame;

public class ViewerRoot extends JFrame {
    private final static int INITIAL_WIDTH = 640;
    private final static int INITAIL_HEIGHT = 480;
    private final static String TITLE = "Spotpix";
    private final ImageViewPanel imageViewPanel;

    public ViewerRoot(boolean exitOnClose) {
        super(TITLE);

        imageViewPanel = new ImageViewPanel();
        add(imageViewPanel);
        setJMenuBar(new ViewerMenuBar(this));

        setSize(INITIAL_WIDTH, INITAIL_HEIGHT);
        if (exitOnClose)
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ImageViewPanel getImageViewPanel() {
        return imageViewPanel;
    }
}
