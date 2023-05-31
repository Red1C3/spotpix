package io.codeberg.spotpix.views;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.text.html.ImageView;

public class ViewerRoot extends JFrame {
    private final static int INITIAL_WIDTH = 640;
    private final static int INITAIL_HEIGHT = 480;
    private final static String TITLE = "Spotpix";
    private final ImageViewPanel imageViewPanel;

    public ViewerRoot() {
        super(TITLE);
        
        imageViewPanel=new ImageViewPanel();
        add(imageViewPanel);
        setJMenuBar(new ViewerMenuBar(this));

        setSize(INITIAL_WIDTH, INITAIL_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public ImageViewPanel getImageViewPanel(){
        return imageViewPanel;
    }
}
