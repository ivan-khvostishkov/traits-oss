package net.nosocial.traits.core;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ikh
 * @since 8/28/19
 */
public class AnswerResult {
    private boolean levelUp;
    private boolean skipped;
    private boolean newTraitDiscovered;
    private Set<PersonalTrait> newTraits;

    public boolean isLevelUp() {
        return levelUp;
    }

    public void setLevelUp(boolean levelUp) {
        this.levelUp = levelUp;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public void setNewTraitDiscovered(boolean newTraitDiscovered) {
        this.newTraitDiscovered = newTraitDiscovered;
    }

    public boolean isNewTraitDiscovered() {
        return newTraitDiscovered;
    }

    public void setNewTraits(Set<PersonalTrait> newTraits) {
        this.newTraits = newTraits;
    }

    public Set<PersonalTrait> getNewTraits() {
        return newTraits;
    }

    public Set<String> getNewTraitsAsStrings() {
        Set<String> result = new HashSet<>();
        for (PersonalTrait newTrait : newTraits) {
            result.add(newTrait.getTraitName());
        }
        return result;
    }
}
