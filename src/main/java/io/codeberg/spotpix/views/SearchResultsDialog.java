package io.codeberg.spotpix.views;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;

import io.codeberg.spotpix.model.images.Image;

public class SearchResultsDialog extends JDialog {
    private static final String SEARCH_RES_STR = "Search Results";
    public static final int imagePanelSize=100;
    private ViewerRoot viewerRoot;
    private ArrayList<Image> images;

    public SearchResultsDialog(ViewerRoot viewerRoot,ArrayList<Image> images){
        super(viewerRoot,SEARCH_RES_STR,ModalityType.APPLICATION_MODAL);
        this.viewerRoot=viewerRoot;
        this.images=images;
        JPanel grid=new JPanel();
        

        setResizable(false);
        setSize(800,800);
        setVisible(true);
    }
}
