package net.nosocial.traits.core;

import java.util.List;

/**
 * @author ikh
 * @since 1/26/15
 */
public interface QuestionShuffler {
    ShuffledQuestions shuffle(List<Question> source);
}
