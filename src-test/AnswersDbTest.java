import net.nosocial.traits.core.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

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
        answersDb.save(answeredQuestions);

        AnswersDb updatedAnswersDb = new AnswersDb(answers);
        AnsweredQuestions updatedQuestions = updatedAnswersDb.load();
        assertEquals(2, updatedQuestions.getCount());
        assertEquals("Being curious", updatedQuestions.getQuestionText(0));
        assertEquals("Recognizing effects of own actions", updatedQuestions.getQuestionText(1));
        assertEquals(Answer.YES, updatedQuestions.getQuestionAnswer(0));
        assertEquals(Answer.UNCERTAIN, updatedQuestions.getQuestionAnswer(1));
    }
}
