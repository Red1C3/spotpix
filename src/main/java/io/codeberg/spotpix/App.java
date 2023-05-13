package io.codeberg.spotpix;

import io.codeberg.spotpix.views.DummyView;

public class App 
{
    public static void main( String[] args )
    {
        long startTime = System.nanoTime();
        new DummyView();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println((double) totalTime/1.0e9);
    }
}
