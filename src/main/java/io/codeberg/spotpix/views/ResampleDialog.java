package io.codeberg.spotpix.views;

import javax.swing.JDialog;

public class ResampleDialog extends JDialog{
    private static final String RESAMPLE_STR="Resample";
    private ViewerRoot viewerRoot;
    
    public ResampleDialog(ViewerRoot viewerRoot){
        super(viewerRoot,RESAMPLE_STR,ModalityType.APPLICATION_MODAL);
        this.viewerRoot=viewerRoot;

        setSize(220,100);
        setVisible(true);
    }
}
