import net.nosocial.traits.core.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author ikh
 * @since 1/26/15
 */
public class AnswersDbTest {

    private File answers;

    @Before
    public void cleanUp() {
        File traitsConfigDir = new File(System.getProperty("user.home"), ".traits");
        if (!traitsConfigDir.exists()) {
            traitsConfigDir.mkdir();
        }
        File traitsProfile = new File(traitsConfigDir, "test");
        if (!traitsProfile.exists()) {
            traitsProfile.mkdir();
        }
        answers = new File(traitsProfile, "answers");
        if (answers.exists()) {
            answers.delete();
        }
    }

    @Test
    public void savesAndLoadsAnswers() throws IOException {
        AnswersDb answersDb = new AnswersDb(answers);
        AnsweredQuestions answeredQuestions = new AnsweredQuestions();
        answeredQuestions.add(new Question(new Behavior("Being curious")), Answer.YES);
        answeredQuestions.add(new Question(new Behavior("Recognizing effects of own actions")), Answer.UNCERTAIN);
        answeredQuestions.add(new Question(new Behavior("!Reverse behavior")), Answer.YES);
        answersDb.save(answeredQuestions);

        AnswersDb updatedAnswersDb = new AnswersDb(answers);
        AnsweredQuestions updatedAnswers = updatedAnswersDb.load();
        assertEquals(3, updatedAnswers.getCount());
        assertEquals("Being curious", updatedAnswers.getQuestionText(0));
        assertEquals("Recognizing effects of own actions", updatedAnswers.getQuestionText(1));
        assertEquals("Reverse behavior", updatedAnswers.getQuestionText(2));
        assertEquals(Answer.YES, updatedAnswers.getQuestionAnswer(0));
        assertEquals(Answer.UNCERTAIN, updatedAnswers.getQuestionAnswer(1));
        assertEquals(Answer.YES, updatedAnswers.getQuestionAnswer(2));

        assertFalse(updatedAnswers.getQuestion(0).getBehavior().isReverse());
        assertTrue(updatedAnswers.getQuestion(2).getBehavior().isReverse());

    }
}
