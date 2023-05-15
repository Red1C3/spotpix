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
        img=(new DummyCtrlr()).testFLT();

    }
    @Override
    public void paint(Graphics g){
        g.drawImage(img, 0, 0, null);
    }
}

public class DummyView extends JFrame {
    public DummyView(){
        super("Dummy window");
        DummyCanvas canvas=new DummyCanvas();
        add(canvas);
        setSize(canvas.img.getWidth(this),canvas.img.getHeight(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
