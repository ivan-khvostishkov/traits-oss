package net.nosocial.traits.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ikh
 * @since 1/26/15
 */
public class MoveToEndQuestionSkipper implements QuestionSkipper {
    @Override
    public List<Question> skip(List<Question> source, Question skippy) {
        List<Question> result = new ArrayList<>();
        result.addAll(source);
        result.remove(skippy);
        result.add(skippy);
        return result;

    }
}
