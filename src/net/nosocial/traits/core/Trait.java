package net.nosocial.traits.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ikh
 * @since 1/25/15
 */
public class Trait {
    private final String name;
    private final boolean positive;
    private List<Behavior> behaviors = new LinkedList<>();

    public Trait(String name, boolean positive) {
        this.name = name;
        this.positive = positive;
    }

    public Trait(String name, boolean positive, Behavior... behaviors) {
        this.name = name;
        this.positive = positive;
        Collections.addAll(this.behaviors, behaviors);
    }

    @Override
    public String toString() {
        return getDbName();
    }

    public String getSignChar() {
        return positive ? "+" : "-";
    }

    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }

    public int getBehaviorsCount() {
        return behaviors.size();
    }

    public String getName() {
        return name;
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public String getDbName() {
        return getSignChar() + name;
    }

    public boolean hasBehavior(Behavior behavior) {
        return behaviors.contains(behavior);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trait trait = (Trait) o;

        if (!name.equals(trait.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean isPositive() {
        return positive;
    }
}
