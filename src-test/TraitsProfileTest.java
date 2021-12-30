import net.nosocial.traits.core.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author ikh
 * @since 1/25/15
 */
public class TraitsProfileTest {
    @Test
    public void answeringQuestionModifiesProfile() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("FLIRTATIOUS", true, new Behavior("Looking into people's eyes")));

        TraitsProfile profile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        Question question = profile.nextQuestion();
        assertEquals("Looking into people's eyes", question.getText());
        profile.answerQuestion(question, Answer.YES);

        PersonalTraits personalTraits = profile.getPersonalTraits();
        PersonalTrait personalTrait = personalTraits.get(0);
        assertEquals("FLIRTATIOUS", personalTrait.getTraitName());
        assertEquals(1, personalTrait.getLevel());
    }

    @Test
    public void previouslyAnsweredQuestionsFormProfile() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("HAPPY", true, new Behavior("Staying connected with friends")));
        traits.add(new Trait("ROWDY", true, new Behavior("Defacing property")));
        traits.add(new Trait("ROWDY", true, new Behavior("Disrespecting others")));

        Question q1 = new Question(new Behavior("Staying connected with friends"));
        Question q2 = new Question(new Behavior("Defacing property"));
        Question q3 = new Question(new Behavior("Disrespecting others"));

        TraitsProfile traitsProfile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());
        traitsProfile.answerQuestion(q1, Answer.NO);
        traitsProfile.answerQuestion(q2, Answer.NO);
        traitsProfile.answerQuestion(q3, Answer.NO);

        assertFalse(traitsProfile.hasMoreQuestions());

        PersonalTraits personalTraits = traitsProfile.getPersonalTraits();
        assertEquals(2, personalTraits.getCount());
        assertEquals("ROWDY", personalTraits.get(0).getTraitName());
        assertEquals(-2, personalTraits.get(0).getLevel());
        assertEquals("HAPPY", personalTraits.get(1).getTraitName());
        assertEquals(-1, personalTraits.get(1).getLevel());
    }

    @Test
    public void canRollbackQuestions() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("HAPPY", true, new Behavior("Staying connected with friends")));
        traits.add(new Trait("ROWDY", false, new Behavior("Defacing property")));
        traits.add(new Trait("ROWDY", false, new Behavior("Disrespecting others")));

        TraitsProfile traitsProfile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        Question question;

        question = traitsProfile.nextQuestion();
        assertEquals("Staying connected with friends", question.getText());
        traitsProfile.answerQuestion(question, Answer.NO);

        question = traitsProfile.nextQuestion();
        assertEquals("Defacing property", question.getText());
        traitsProfile.answerQuestion(question, Answer.NO);

        question = traitsProfile.nextQuestion();
        assertEquals("Disrespecting others", question.getText());
        assertFalse(traitsProfile.isAlreadyAnswered(question));
        assertTrue(traitsProfile.hasMoreQuestions());

        

        question = traitsProfile.previousQuestion();
        assertEquals("Defacing property", question.getText());
        assertTrue(traitsProfile.isAlreadyAnswered(question));
        assertTrue(traitsProfile.hasMoreQuestions());

        question = traitsProfile.previousQuestion();
        assertEquals("Staying connected with friends", question.getText());
        assertTrue(traitsProfile.isAlreadyAnswered(question));
        assertTrue(traitsProfile.hasMoreQuestions());

        traitsProfile.answerQuestion(question, Answer.YES);


        question = traitsProfile.nextQuestion();
        assertEquals("Disrespecting others", question.getText());
        assertFalse(traitsProfile.isAlreadyAnswered(question));

        traitsProfile.answerQuestion(question, Answer.NO);

        assertFalse(traitsProfile.hasMoreQuestions());

        PersonalTraits personalTraits = traitsProfile.getPersonalTraits();
        assertEquals(2, personalTraits.getCount());
        assertEquals("ROWDY", personalTraits.get(0).getTraitName());
        assertEquals(-2, personalTraits.get(0).getLevel());
        assertEquals("HAPPY", personalTraits.get(1).getTraitName());
        assertEquals(1, personalTraits.get(1).getLevel());
    }


    @Test
    public void canRollbackWithoutAnswering() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("HAPPY", true, new Behavior("Staying connected with friends")));
        traits.add(new Trait("ROWDY", false, new Behavior("Defacing property")));
        traits.add(new Trait("ROWDY", false, new Behavior("Disrespecting others")));

        TraitsProfile traitsProfile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        Question question;

        question = traitsProfile.nextQuestion();
        assertEquals("Staying connected with friends", question.getText());
        traitsProfile.answerQuestion(question, Answer.NO);

        question = traitsProfile.nextQuestion();
        assertEquals("Defacing property", question.getText());
        traitsProfile.answerQuestion(question, Answer.NO);

        question = traitsProfile.nextQuestion();
        assertEquals("Disrespecting others", question.getText());


        question = traitsProfile.previousQuestion();
        assertEquals("Defacing property", question.getText());

        traitsProfile.forward();
        
        question = traitsProfile.nextQuestion();
        assertEquals("Disrespecting others", question.getText());
        assertFalse(traitsProfile.isAlreadyAnswered(question));

        traitsProfile.answerQuestion(question, Answer.NO);

        assertFalse(traitsProfile.hasMoreQuestions());

        PersonalTraits personalTraits = traitsProfile.getPersonalTraits();
        assertEquals(2, personalTraits.getCount());
        assertEquals("ROWDY", personalTraits.get(0).getTraitName());
        assertEquals(-2, personalTraits.get(0).getLevel());
        assertEquals("HAPPY", personalTraits.get(1).getTraitName());
        assertEquals(-1, personalTraits.get(1).getLevel());
    }

    @Test
    public void skippingAnsweredQuestionReduceStats() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("HAPPY", true, new Behavior("Staying connected with friends")));
        traits.add(new Trait("ROWDY", false, new Behavior("Defacing property")));
        traits.add(new Trait("ROWDY", false, new Behavior("Disrespecting others")));

        TraitsProfile traitsProfile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        Question question;

        question = traitsProfile.nextQuestion();
        assertEquals("Staying connected with friends", question.getText());
        traitsProfile.answerQuestion(question, Answer.NO);

        question = traitsProfile.nextQuestion();
        assertEquals("Defacing property", question.getText());
        traitsProfile.answerQuestion(question, Answer.NO);

        assertEquals(1, traitsProfile.getLevel());
        assertEquals(2, traitsProfile.getAnsweredQuestionsCount());


        question = traitsProfile.previousQuestion();
        assertEquals("Defacing property", question.getText());

        question = traitsProfile.previousQuestion();
        assertEquals("Staying connected with friends", question.getText());
        traitsProfile.skipQuestion(question);


        question = traitsProfile.previousQuestion();
        assertEquals("Defacing property", question.getText());
        traitsProfile.skipQuestion(question);


        question = traitsProfile.nextQuestion();
        assertEquals("Disrespecting others", question.getText());
        

        assertEquals(0, traitsProfile.getLevel());
        assertEquals(0, traitsProfile.getAnsweredQuestionsCount());
    }

    @Test
    public void uncertainQuestionsHasZeroLevel() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("SOCIALLY RESPONSIBLE", true, new Behavior("Recognizing effects of own actions")));

        Question q1 = new Question(new Behavior("Recognizing effects of own actions"));

        TraitsProfile traitsProfile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());
        traitsProfile.answerQuestion(q1, Answer.UNCERTAIN);

        assertFalse(traitsProfile.hasMoreQuestions());

        PersonalTraits personalTraits = traitsProfile.getPersonalTraits();
        assertEquals(1, personalTraits.getCount());
        assertEquals("SOCIALLY RESPONSIBLE", personalTraits.get(0).getTraitName());
        assertEquals(0, personalTraits.get(0).getLevel());
    }

    @Test
    public void invertedQuestionsHasInvertedImpact() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("TRAIT WITH INVERSE QUESTION", true, new Behavior("!Recognizing effects of own actions")));

        TraitsProfile traitsProfile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        Question question;
        question = traitsProfile.nextQuestion();
        assertEquals("Recognizing effects of own actions", question.getText());
        traitsProfile.answerQuestion(question, Answer.YES);

        PersonalTraits personalTraits = traitsProfile.getPersonalTraits();
        assertEquals(1, personalTraits.getCount());
        assertEquals("TRAIT WITH INVERSE QUESTION", personalTraits.get(0).getTraitName());
        assertEquals(-1, personalTraits.get(0).getLevel());
    }


    @Test
    public void questionsAreShuffledInPredefinedWay() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("HYPERSENSITIVE", false, new Behavior("Crying"),
                new Behavior("Reacting inadequately to teasing"),
                new Behavior("Seeing criticism when none present")));

        TraitsProfile profile = new TraitsProfile(traits,
                new ReverseQuestionShuffler().shuffle(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        Question question;
        question = profile.nextQuestion();
        assertEquals("Seeing criticism when none present", question.getText());
        profile.answerQuestion(question, Answer.UNCERTAIN);

        question = profile.nextQuestion();
        assertEquals("Reacting inadequately to teasing", question.getText());
        profile.answerQuestion(question, Answer.UNCERTAIN);

        question = profile.nextQuestion();
        assertEquals("Crying", question.getText());
        profile.answerQuestion(question, Answer.UNCERTAIN);

        assertFalse(profile.hasMoreQuestions());
    }

    @Test
    public void previouslyAnsweredQuestionsNotRepeated() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("HYPERSENSITIVE", true, new Behavior("Crying"),
                new Behavior("Reacting inadequately to teasing"),
                new Behavior("Seeing criticism when none present")));

        TraitsProfile profile = new TraitsProfile(traits, new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        profile.answerQuestion(new Question(new Behavior("Seeing criticism when none present")), Answer.YES);

        Question question = profile.nextQuestion();
        assertEquals("Crying", question.getText());
        profile.answerQuestion(question, Answer.UNCERTAIN);

        question = profile.nextQuestion();
        assertEquals("Reacting inadequately to teasing", question.getText());
        profile.answerQuestion(question, Answer.UNCERTAIN);

        assertFalse(profile.hasMoreQuestions());
    }

    @Test
    public void questionsAreSkippedInAPredefinedWay() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("HYPERSENSITIVE", true, new Behavior("Crying"),
                new Behavior("Reacting inadequately to teasing"),
                new Behavior("Seeing criticism when none present")));

        TraitsProfile profile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        Question question;
        question = profile.nextQuestion();
        assertEquals("Crying", question.getText());

        profile.skipQuestion(question);

        question = profile.nextQuestion();
        assertEquals("Reacting inadequately to teasing", question.getText());
        profile.answerQuestion(question, Answer.UNCERTAIN);

        question = profile.nextQuestion();
        assertEquals("Seeing criticism when none present", question.getText());
        profile.answerQuestion(question, Answer.UNCERTAIN);

        assertTrue(profile.hasMoreQuestions());

        question = profile.nextQuestion();
        assertEquals("Crying", question.getText());
        profile.answerQuestion(question, Answer.UNCERTAIN);

        assertFalse(profile.hasMoreQuestions());
    }

    @Test
    public void oneQuestionMayContributeFewTraits() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("APPRECIATIVE", true, new Behavior("Friendliness")));
        traits.add(new Trait("BOLD", true, new Behavior("Friendliness")));
        traits.add(new Trait("COURTEOUS", true, new Behavior("Friendliness")));

        TraitsProfile profile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        Question question = profile.nextQuestion();
        profile.answerQuestion(question, Answer.YES);

        assertFalse(profile.hasMoreQuestions());

        PersonalTraits personalTraits = profile.getPersonalTraits();
        assertEquals(3, personalTraits.getCount());
    }

    @Test
    public void levelUpIndication() {
        CharacterTraits traits = new CharacterTraits();
        traits.add(new Trait("APPRECIATIVE", true, new Behavior("Friendliness")));
        traits.add(new Trait("HYPERSENSITIVE", true, new Behavior("Crying"),
                new Behavior("Reacting inadequately to teasing"),
                new Behavior("Seeing criticism when none present")));

        final TraitsProfile profile = new TraitsProfile(traits,
                new ShuffledQuestions(traits.getAllQuestions()),
                new MoveToEndQuestionSkipper());

        assertEquals(0, profile.getLevel());

        new Runnable() {
            @Override
            public void run() {
                Question question = profile.nextQuestion();
                assertEquals("Friendliness", question.getText());
                AnswerResult answerResult = profile.answerQuestion(question, Answer.YES);

                assertTrue(answerResult.isLevelUp());
                assertEquals(1, profile.getLevel());
            }
        }.run();


        new Runnable() {
            @Override
            public void run() {
                Question question = profile.nextQuestion();
                assertEquals("Crying", question.getText());
                AnswerResult answerResult = profile.answerQuestion(question, Answer.NO);

                assertFalse(answerResult.isLevelUp());
                assertEquals(1, profile.getLevel());
            }
        }.run();

        new Runnable() {
            @Override
            public void run() {
                Question question = profile.nextQuestion();
                assertEquals("Reacting inadequately to teasing", question.getText());
                AnswerResult answerResult = profile.answerQuestion(question, Answer.NO);

                assertTrue(answerResult.isLevelUp());
                assertEquals(2, profile.getLevel());
            }
        }.run();

        new Runnable() {
            @Override
            public void run() {
                Question question = profile.nextQuestion();
                assertEquals("Seeing criticism when none present", question.getText());
                AnswerResult answerResult = profile.answerQuestion(question, Answer.YES);

                assertFalse(answerResult.isLevelUp());
                assertEquals(1, profile.getLevel());
            }
        }.run();

    }

}
