package io.codeberg.spotpix.model.search.algorithms;

import io.codeberg.spotpix.model.images.Image;

public interface SearchAlgorithm {
    boolean match(Image img);
}
