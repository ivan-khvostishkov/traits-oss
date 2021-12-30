package net.nosocial.traits.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ikh
 * @since 1/26/15
 */
public class RandomQuestionsSkipper implements QuestionSkipper {
    @Override
    public List<Question> skip(List<Question> source, Question skippy) {
        int index = source.indexOf(skippy);
        if (index == -1) {
            throw new IllegalArgumentException("Questions doesn't contain question to skip: " + skippy.getText());
        }
        List<Question> result = new ArrayList<>(source.size());
        result.addAll(source);
        if (index == source.size() - 1) {
            return result;
        }
        result.remove(index);
        result.add(index + new SecureRandom().nextInt(result.size() - index) + 1, skippy);
        return result;
    }
}
