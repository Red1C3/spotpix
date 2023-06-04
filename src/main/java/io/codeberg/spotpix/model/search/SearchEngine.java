package io.codeberg.spotpix.model.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.stream.Stream;

import io.codeberg.spotpix.model.decoders.FLTDecoder;
import io.codeberg.spotpix.model.decoders.JDecoder;
import io.codeberg.spotpix.model.images.Image;
import io.codeberg.spotpix.model.search.algorithms.SearchAlgorithm;

public class SearchEngine {
    private static final String PNG_FMT = "png";
    private static final String JPG_FMT = "jpg";
    private static final String FLT_FMT = "flt";
    private Image image;
    private ArrayList<Image> imagesInPath;

    public SearchEngine(Path searchPath, Image image) {
        this.image = image;
        imagesInPath = new ArrayList<>();
        try (Stream<Path> walkStream = Files.walk(searchPath)) {
            walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                try {
                    if (f.toString().toLowerCase().endsWith(FLT_FMT)) {
                        FileTime fileTime = (FileTime) Files.getAttribute(f, "creationTime");
                        byte[] bytes = Files.readAllBytes(f);
                        imagesInPath.add((new FLTDecoder().decode(bytes, fileTime)));
                    }
                    if (f.toString().toLowerCase().endsWith(JPG_FMT) ||
                            f.toString().toLowerCase().endsWith(PNG_FMT)) {
                        FileTime fileTime = (FileTime) Files.getAttribute(f, "creationTime");
                        byte[] bytes = Files.readAllBytes(f);
                        try {
                            imagesInPath.add((new JDecoder().decode(bytes, fileTime)));
                        } catch (Exception e) {
                            // No need ig
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error while reading a file in searching path");
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("Error while searching");
            e.printStackTrace();
        }
    }

    public ArrayList<Image> search(SearchAlgorithm algorithm) {
        ArrayList<Image> matched = new ArrayList<>();
        for (Image imageInPath : imagesInPath) {
            if (algorithm.match(imageInPath)) {
                matched.add(imageInPath);
            }
        }
        return matched;
    }

    public Image getSearchingImage() {
        return image;
    }
}
