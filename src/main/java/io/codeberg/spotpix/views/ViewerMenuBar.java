package io.codeberg.spotpix.views;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ViewerMenuBar extends JMenuBar {
    private static final String FILE_STR="File";
    private static final String OPEN_STR="Open";
    public ViewerMenuBar(){
        JMenu file=new JMenu(FILE_STR);
        

        JMenuItem open=new JMenuItem(OPEN_STR);


        file.add(open);
        add(file);
    }
}
