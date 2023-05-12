package io.codeberg.spotpix.model.colorOps;

public class ARGBAdd implements ColorOp{
    @Override
    public Color op(Color color0,Color color1){
        return Color.ARGBAdd(color0,color1);
    }
}