package net.nosocial.traits.core;

import java.util.*;

/**
 * @author ikh
 * @since 1/26/15
 */
public class PersonalTraits {
    LinkedHashMap<Trait, PersonalTrait> personalTraitsMap = new LinkedHashMap<>();

    public PersonalTrait get(int i) {
        List<PersonalTrait> sortedTraits = getSorted();
        return sortedTraits.get(i);
    }

    public List<PersonalTrait> getSorted() {
        List<PersonalTrait> sortedTraits = new ArrayList<>(personalTraitsMap.size());
        sortedTraits.addAll(personalTraitsMap.values());
        Collections.sort(sortedTraits, new Comparator<PersonalTrait>() {
            @Override
            public int compare(PersonalTrait o1, PersonalTrait o2) {
                int levelDiff = Math.abs(o2.getLevel()) - Math.abs(o1.getLevel());
                if (levelDiff == 0) {
                    return o1.getTraitName().compareTo(o2.getTraitName());
                }
                return levelDiff;
            }
        });
        return sortedTraits;
    }

    public int getCount() {
        return personalTraitsMap.size();
    }

    private void add(Trait trait, Answer answer, boolean reverse) {
        PersonalTrait personalTrait = personalTraitsMap.get(trait);
        if (personalTrait == null) {
            personalTrait = new PersonalTrait(trait);
            personalTraitsMap.put(trait, personalTrait);
        }
        if ((answer == Answer.YES && !reverse) || (answer == Answer.NO && reverse)) {
            personalTrait.increaseLevel();
        } else if (answer == Answer.NO || answer == Answer.YES /* reverse will be true if reached */ ) {
            personalTrait.decreaseLevel();
        }
    }

    public void addAll(SimilarTraits similarTraits, Answer answer, boolean reverse) {
        for (Trait trait : similarTraits.getTraits()) {
            add(trait, answer, reverse);
        }
    }
}
