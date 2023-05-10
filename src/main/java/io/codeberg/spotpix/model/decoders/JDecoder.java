package io.codeberg.spotpix.model.decoders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import io.codeberg.spotpix.model.images.Image;

public class JDecoder  implements Decoder{

    @Override
    public Image decode(byte[] bytes) {
        InputStream byteStream=new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage;
        try{
            bufferedImage=ImageIO.read(byteStream);
        }catch(IOException e){
            //Not even IOing
        }
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decode'");
    }
    
}
