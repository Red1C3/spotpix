package io.codeberg.spotpix;

import io.codeberg.spotpix.views.DummyView;
import io.codeberg.spotpix.views.ImageViewPanel;
import io.codeberg.spotpix.views.ViewerRoot;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            new ViewerRoot();
        } else if (args.length == 1) {
            String imgPath = args[0];
            ViewerRoot viewerRoot=new ViewerRoot();
            ImageViewPanel.instance().openImage(imgPath);
            viewerRoot.repaint();
        }
    }
}
