package io.codeberg.spotpix;

import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.views.DummyView;
import io.codeberg.spotpix.views.ImageViewPanel;
import io.codeberg.spotpix.views.ViewerRoot;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            new ViewerRoot(true);
        } else if (args.length == 1) {
            String imgPath = args[0];
            ViewerRoot viewerRoot=new ViewerRoot(true);
            viewerRoot.getImageViewPanel().openImage(imgPath);
            viewerRoot.repaint();
        }
    }

    public static void main(Image image){
        ViewerRoot viewerRoot=new ViewerRoot(false);
        viewerRoot.getImageViewPanel().openImage(image);
        viewerRoot.repaint();
    }
}
