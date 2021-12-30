import net.nosocial.traits.core.Behavior;
import net.nosocial.traits.core.Question;
import net.nosocial.traits.core.QuestionsDb;
import net.nosocial.traits.core.ShuffledQuestions;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * @author ikh
 * @since 1/26/15
 */
public class QuestionsDbTest {

    private File questions;

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
        questions = new File(traitsProfile, "questions");
        if (questions.exists()) {
            questions.delete();
        }
    }

    @Test
    public void savesAndLoadsQuestions() throws IOException {
        QuestionsDb questionsDb = new QuestionsDb(questions);

        ShuffledQuestions shuffledQuestions = new ShuffledQuestions();
        shuffledQuestions.add(new Question(new Behavior("Being curious")));
        shuffledQuestions.add(new Question(new Behavior("Recognizing effects of own actions")));

        questionsDb.save(shuffledQuestions);

        QuestionsDb updatedQuestionsDb = new QuestionsDb(questions);
        ShuffledQuestions updatedQuestions = updatedQuestionsDb.load();

        assertEquals(2, updatedQuestions.getCount());

        assertEquals("Being curious", updatedQuestions.getQuestionText(0));
        assertEquals("Recognizing effects of own actions", updatedQuestions.getQuestionText(1));
    }
}
