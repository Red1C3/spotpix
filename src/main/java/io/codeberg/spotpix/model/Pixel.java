package io.codeberg.spotpix.model;

public class Pixel {
    private Color color;
    private int x,y;
    public Pixel(Color color, int x,int y){
        this.color=color;
        this.x=x;
        this.y=y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Color getColor(){
        return color;
    }
    public void setColor(Color color){
        this.color=color;
    }
}
