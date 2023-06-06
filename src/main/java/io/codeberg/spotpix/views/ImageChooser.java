package io.codeberg.spotpix.views;

import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageChooser extends JFileChooser {
    public ImageChooser() {
        super();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images (jpg, png, flt)",
                "jpg", "png", "flt");
        setFileFilter(filter);
        setCurrentDirectory(Paths.get(System.getProperty("user.dir")).toFile());
    }
}
