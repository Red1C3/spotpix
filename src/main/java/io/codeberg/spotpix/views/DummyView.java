package io.codeberg.spotpix.views;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.codeberg.spotpix.controllers.DummyCtrlr;

class DummyCanvas extends JPanel{
    Image img;
    public DummyCanvas(){
        super();
        img=(new DummyCtrlr()).getImage();

    }
    @Override
    public void paint(Graphics g){
        g.drawImage(img, 0, 0, null);
    }
}

public class DummyView extends JFrame {
    public DummyView(){
        super("Dummy window");
        add(new DummyCanvas());
        setSize(600,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
