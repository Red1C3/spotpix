package io.codeberg.spotpix.views;

import java.awt.Canvas;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

class DummyCanvas extends JPanel{
    @Override
    public void paint(Graphics g){
        
    }
}

public class DummyView extends JFrame {
    public DummyView(){
        super("Dummy window");
    }
}
