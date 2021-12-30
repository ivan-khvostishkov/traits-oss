package net.nosocial.traits.core;

import java.util.LinkedList;

/**
 * @author ikh
 * @since 2/6/15
 */
public class SimilarTraits {
    private final LinkedList<Trait> traits;

    public SimilarTraits(LinkedList<Trait> traits) {
        this.traits = traits;
    }

    public LinkedList<Trait> getTraits() {
        return traits;
    }
}
