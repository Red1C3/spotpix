package io.codeberg.spotpix.views;

import javax.swing.JFrame;

public class ViewerRoot extends JFrame {
    private final static int INITIAL_WIDTH = 640;
    private final static int INITAIL_HEIGHT = 480;
    private final static String TITLE = "Spotpix";

    public ViewerRoot() {
        super(TITLE);
        setJMenuBar(new ViewerMenuBar());
        setSize(INITIAL_WIDTH, INITAIL_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
