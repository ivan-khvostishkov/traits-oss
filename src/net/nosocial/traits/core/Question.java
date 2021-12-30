package net.nosocial.traits.core;

/**
 * @author ikh
 * @since 1/25/15
 */
public class Question {

    private final Behavior behavior;

    public Question(Behavior behavior) {
        this.behavior = behavior;
    }

    public String getText() {
        return behavior.getText();
    }

    public Behavior getBehavior() {
        return behavior;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (!behavior.equals(question.behavior)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return behavior.hashCode();
    }

    @Override
    public String toString() {
        return behavior.getText();
    }

    public String getLatin1Text() {
        String result = getText();
        return result.replaceAll("…", "...").replaceAll("—", "-")
                .replaceAll("‘", "'").replaceAll("’", "'")
                .replaceAll("“", "\"").replaceAll("”", "\"");
    }

    public String getTextWithReverseIndicator() {
        return behavior.getTextWithReverseIndicator();
    }
}
