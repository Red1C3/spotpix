package io.codeberg.spotpix;

import io.codeberg.spotpix.views.DummyView;
import io.codeberg.spotpix.views.ViewerRoot;

public class App 
{
    public static void main( String[] args )
    {
        long startTime = System.nanoTime();
        ViewerRoot.instance();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println((double) totalTime/1.0e9);
    }
}
