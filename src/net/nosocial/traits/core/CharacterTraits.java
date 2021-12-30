package net.nosocial.traits.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ikh
 * @since 1/25/15
 */
public class CharacterTraits {
    private final List<Trait> traitList = new LinkedList<>();

    public void add(Trait trait) {
        traitList.add(trait);
    }

    public int getCount() {
        return traitList.size();
    }

    public int getBehaviorsCount() {
        int result = 0;
        for (Trait trait : traitList) {
            result += trait.getBehaviorsCount();
        }
        return result;
    }

    public List<Trait> getTraitList() {
        return traitList;
    }

    public SimilarTraits getTraitsByBehavior(Behavior behavior) {
        LinkedList<Trait> traits = new LinkedList<>();
        for (Trait trait : traitList) {
            if (trait.hasBehavior(behavior)) {
                traits.add(trait);
            }
        }
        if (traits.isEmpty()) {
            throw new IllegalArgumentException("Know nothing about behavior " + behavior);
        }
        return new SimilarTraits(traits);
    }

    public List<Behavior> getAllBehaviors() {
        LinkedList<Behavior> result = new LinkedList<>();
        for (Trait trait : traitList) {
            result.addAll(trait.getBehaviors());
        }
        return result;
    }

    public List<Question> getAllQuestions() {
        List<Behavior> behaviors = getAllBehaviors();
        List<Question> questions = new ArrayList<>(behaviors.size());
        for (Behavior behavior : behaviors) {
            questions.add(new Question(behavior));
        }
        return questions;
    }
}
