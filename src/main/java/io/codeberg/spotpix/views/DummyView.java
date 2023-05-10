package io.codeberg.spotpix.views;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.codeberg.spotpix.controllers.DummyCtrlr;

class DummyCanvas extends JPanel{
    @Override
    public void paint(Graphics g){
        Image img=(new DummyCtrlr()).getImage();

        g.drawImage(img, 0, 0, null);
    }
}

public class DummyView extends JFrame {
    public DummyView(){
        super("Dummy window");
        add(new DummyCanvas());
        setSize(100,100);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
