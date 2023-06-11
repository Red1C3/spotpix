package io.codeberg.spotpix.controllers;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.search.SearchEngine;
import io.codeberg.spotpix.model.search.algorithms.DateSearch;
import io.codeberg.spotpix.model.search.algorithms.DimSearch;
import io.codeberg.spotpix.model.search.algorithms.SizeSearch;

public class SearchCtrlr {
    private SearchCtrlr() {
    }

    public static ArrayList<Image> getImagesToSearch(String path) {
        return (new SearchEngine(Paths.get(path), null).getImagesInPath());
    }

    public static ArrayList<Image> dateSearch(ArrayList<Image> images, Date starDate, Date endDate) {
        return (new SearchEngine(images, null)).search(new DateSearch(starDate, endDate));
    }

    public static ArrayList<Image> dimSearch(ArrayList<Image> images, int width, int height, int widthThreshold,
            int heightThreshold) {
        return (new SearchEngine(images, null)).search(new DimSearch(width, height, widthThreshold, heightThreshold));
    }

    public static ArrayList<Image> sizeSearch(ArrayList<Image> images, int size, int sizeThreshold) {
        return (new SearchEngine(images, null)).search(new SizeSearch(size, sizeThreshold));
    }
}
