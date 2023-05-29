package io.codeberg.spotpix.views;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ViewerRoot extends JFrame {
    private static ViewerRoot viewerRoot=new ViewerRoot();
    private final static int INITIAL_WIDTH = 640;
    private final static int INITAIL_HEIGHT = 480;
    private final static String TITLE = "Spotpix";

    private ViewerRoot() {
        super(TITLE);
        
        add(ImageViewPanel.instance());
        setJMenuBar(new ViewerMenuBar(this));

        setSize(INITIAL_WIDTH, INITAIL_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static ViewerRoot instance(){
        return viewerRoot;
    }
}
