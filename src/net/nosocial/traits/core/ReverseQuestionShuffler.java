package net.nosocial.traits.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ikh
 * @since 1/26/15
 */
public class ReverseQuestionShuffler implements QuestionShuffler {
    @Override
    public ShuffledQuestions shuffle(List<Question> source) {
        List<Question> result = new ArrayList<>();
        result.addAll(source);
        Collections.reverse(result);
        return new ShuffledQuestions(result);
    }
}
