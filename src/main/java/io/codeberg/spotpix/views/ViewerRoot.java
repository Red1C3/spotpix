package io.codeberg.spotpix.views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ViewerRoot extends JFrame {
    private final static int INITIAL_WIDTH = 640;
    private final static int INITAIL_HEIGHT = 480;
    private final static String TITLE = "Spotpix";

    public final JSplitPane splitPane;

    public ViewerRoot() {
        super(TITLE);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), ImageViewPanel.instance());
        add(splitPane);
        setJMenuBar(new ViewerMenuBar(this));

        setSize(INITIAL_WIDTH, INITAIL_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
