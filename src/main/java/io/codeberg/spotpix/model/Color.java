package io.codeberg.spotpix.model;

public class Color {
    private int argb;

    // TODO LABS fmt
    public Color(int argb) {
        this.argb = argb;
        // TODO calculate other formats values
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
}
