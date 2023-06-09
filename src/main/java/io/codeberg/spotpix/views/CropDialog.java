package io.codeberg.spotpix.views;

import javax.swing.JDialog;

public class CropDialog extends JDialog {
    public CropDialog(ViewerRoot viewerRoot){
        super(viewerRoot,"Crop",ModalityType.APPLICATION_MODAL);

        setSize(400,400);
        setVisible(true);
    }
    
}
