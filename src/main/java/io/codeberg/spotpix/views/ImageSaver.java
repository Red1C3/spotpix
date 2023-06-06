package io.codeberg.spotpix.views;

import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageSaver extends JFileChooser {
    public ImageSaver() {
        super();
        FileFilter filter = new FileNameExtensionFilter(
                "Images (png, flt)",
                 "png", "flt");
        setFileFilter(filter);
        setCurrentDirectory(Paths.get(System.getProperty("user.dir")).toFile());
    }
}
