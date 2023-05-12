package io.codeberg.spotpix.model;

public class Color {
    private int argb;

    // TODO LABS fmt
    public Color(int argb) {
        this.argb = argb;
        // TODO calculate other formats values
    }

    public Color(int a,int r,int g,int b){
        argb=0;
        argb|=((a<<24)&0xFF000000);
        argb|=((r<<16)&0xFF0000);
        argb|=((g<<8)&0xFF00);
        argb|=(b&0xFF);
    }

    public int getARGB() {
        return argb;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color) {
            return argb == ((Color) obj).argb;
        }
        return super.equals(obj);
    }
    public int getAlpha() {
        return (argb & 0xFF000000) >> 24;
    }

    public int getRed() {
        return (argb & 0xFF0000) >> 16;
    }

    public int getGreen() {
        return (argb & 0xFF00) >> 8;
    }

    public int getBlue() {
        return (argb & 0xFF);
    }

    public static Color ARGBAdd(Color color0, Color color1) {
        int alpha = Math.max(color0.getAlpha() + color1.getAlpha(), 255);
        int red = Math.max(color0.getRed() + color1.getRed(), 255);
        int green = Math.max(color0.getGreen() + color1.getGreen(), 255);
        int blue = Math.max(color0.getBlue() + color1.getBlue(), 255);

        return new Color(alpha, red, green, blue);
    }
}
