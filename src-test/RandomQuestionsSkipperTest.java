import net.nosocial.traits.core.Behavior;
import net.nosocial.traits.core.Question;
import net.nosocial.traits.core.QuestionSkipper;
import net.nosocial.traits.core.RandomQuestionsSkipper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author ikh
 * @since 1/26/15
 */
public class RandomQuestionsSkipperTest {
    @Test
    public void insertsIntoTheOnlyPossiblePositionForPairOfQuestions() {
        QuestionSkipper skipper = new RandomQuestionsSkipper();
        List<Question> source = new ArrayList<>(2);
        Question q1 = new Question(new Behavior("Thinking about helping people"));
        source.add(q1);
        Question q2 = new Question(new Behavior("Researching different cultures"));
        source.add(q2);
        List<Question> result = skipper.skip(source, q1);
        assertEquals(q2, result.get(0));
        assertEquals(q1, result.get(1));
    }

    @Test
    public void insertsIntoTheOnlyPossiblePositionForTreeQuestions() {
        QuestionSkipper skipper = new RandomQuestionsSkipper();
        List<Question> source = new ArrayList<>(2);
        Question q1 = new Question(new Behavior("Thinking about helping people"));
        source.add(q1);
        Question q2 = new Question(new Behavior("Researching different cultures"));
        source.add(q2);
        Question q3 = new Question(new Behavior("Offering help"));
        source.add(q3);
        List<Question> result = skipper.skip(source, q2);
        assertEquals(q1, result.get(0));
        assertEquals(q3, result.get(1));
        assertEquals(q2, result.get(2));
    }
}
