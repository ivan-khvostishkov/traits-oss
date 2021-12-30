package net.nosocial.traits.core;

/**
 * @author ikh
 * @since 1/25/15
 */
public class Behavior {
    private final String text;
    private final boolean reverse;

    public Behavior(String text) {
        if (text.startsWith("!")) {
            this.text = text.substring(1);
            reverse = true;
        } else {
            this.text = text;
            reverse = false;
        }
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Behavior behavior = (Behavior) o;

        if (!text.equals(behavior.text)) return false;

        return true;
    }

    public boolean isReverse() {
        return reverse;
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
