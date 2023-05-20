package io.codeberg.spotpix.views;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageSaver extends JFileChooser {
    public ImageSaver() {
        super();
        FileFilter filter = new FileNameExtensionFilter(
                "Images (jpg, png, flt)",
                "jpg", "png", "flt");
        setFileFilter(filter);
        setCurrentDirectory(getCurrentDirectory());
    }
}
