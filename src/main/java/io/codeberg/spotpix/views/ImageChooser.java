package io.codeberg.spotpix.views;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageChooser extends JFileChooser {
    public ImageChooser(){
        //super() use current directory
        FileNameExtensionFilter filter=new FileNameExtensionFilter("Images", "jpg","png","flt");
        setFileFilter(filter);
        setCurrentDirectory(getCurrentDirectory());
    }
}
