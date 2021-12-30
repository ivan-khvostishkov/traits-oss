package net.nosocial.traits.core;

import java.util.Objects;

/**
 * @author ikh
 * @since 1/26/15
 */
public class PersonalTrait {
    private final Trait trait;
    private int level = 0;

    public PersonalTrait(Trait trait) {
        this.trait = trait;
    }

    public int getLevel() {
        return level;
    }

    public String getTraitName() {
        return trait.getName();
    }


    public void increaseLevel() {
        level++;
    }

    public void decreaseLevel() {
        level--;
    }

    public String getTraitSignChar() {
        return trait.getSignChar();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalTrait that = (PersonalTrait) o;
        return trait.equals(that.trait);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trait);
    }
}
