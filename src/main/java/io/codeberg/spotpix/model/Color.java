package io.codeberg.spotpix.model;

public class Color {
    private int argb;
    private double[] xyz;

    // TODO LABS fmt
    public Color(int argb) {
        this.argb = argb;
        this.xyz=RGB2XYZ(getRed(), getGreen(), getBlue());
        // TODO calculate other formats values
    }

    public Color(int a,int r,int g,int b){
        argb=0;
        argb|=((a<<24)&0xFF000000);
        argb|=((r<<16)&0xFF0000);
        argb|=((g<<8)&0xFF00);
        argb|=(b&0xFF);
        this.xyz=RGB2XYZ(r, g, b);
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

    public static double[] RGB2XYZ(int ir,int ig,int ib){
        double r=((double)ir)/255.0;
        double g=((double)ig)/255.0;
        double b=((double)ib)/255.0;

        if(r>0.04045){
            r=Math.pow(((r+0.055)/1.055), 2.4);
        }else{
            r=r/12.92;
        }

        if(g>0.04045){
            g=Math.pow(((g+0.055)/1.055),2.4);
        }else{
            g=g/12.92;
        }

        if(b>0.04045){
            b=Math.pow((b+0.055)/1.055,2.4);
        }else{
            b=b/12.92;
        }

        r=r*100;
        g=g*100;
        b=b*100;

        double[] xyz=new double[3];
        xyz[0]=r*0.4124+g*0.3576+b*0.1805;
        xyz[1]=r*0.2126+g*0.7152+b*0.0722;
        xyz[2]=r*0.0193+b*0.1192+b*0.9505;

        return xyz;
    }
}
