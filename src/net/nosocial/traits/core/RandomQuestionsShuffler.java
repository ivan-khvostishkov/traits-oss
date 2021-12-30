package net.nosocial.traits.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ikh
 * @since 1/26/15
 */
public class RandomQuestionsShuffler implements QuestionShuffler {
    @Override
    public ShuffledQuestions shuffle(List<Question> source) {
        List<Question> result = new ArrayList<>();
        result.addAll(source);
        Collections.shuffle(result, new SecureRandom());
        return new ShuffledQuestions(result);
    }
}
