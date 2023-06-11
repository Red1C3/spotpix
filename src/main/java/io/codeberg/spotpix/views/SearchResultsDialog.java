package io.codeberg.spotpix.views;

import java.util.ArrayList;

import javax.swing.JDialog;

import io.codeberg.spotpix.model.images.Image;

public class SearchResultsDialog extends JDialog {
    private static final String SEARCH_RES_STR = "Search Results";
    private ViewerRoot viewerRoot;
    private ArrayList<Image> images;

    public SearchResultsDialog(ViewerRoot viewerRoot,ArrayList<Image> images){
        super(viewerRoot,SEARCH_RES_STR,ModalityType.APPLICATION_MODAL);
        this.viewerRoot=viewerRoot;
        this.images=images;

        setSize(800,800);
        setVisible(true);
    }
}
