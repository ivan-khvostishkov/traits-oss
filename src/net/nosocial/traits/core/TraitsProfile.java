package net.nosocial.traits.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ikh
 * @since 1/25/15
 */
public class TraitsProfile {
    private final CharacterTraits traits;
    private final QuestionSkipper questionSkipper;

    private ShuffledQuestions questions;

    int num = 0;
    int numBack = 0;

    private AnsweredQuestions answers = new AnsweredQuestions();
    private String name;

    public TraitsProfile(CharacterTraits traits, ShuffledQuestions questions, QuestionSkipper questionSkipper) {
        this.traits = traits;
        this.questionSkipper = questionSkipper;
        this.questions = questions;
    }

    public AnswerResult answerQuestion(Question question, Answer answer) {
        int oldLevel = getLevel();
        Set<PersonalTrait> oldTraits = new HashSet<>(getSortedTraits());

        if (isAlreadyAnswered(question)) {
            answers.replace(question, answer);
        } else {
            answers.add(question, answer);
        }

        numBack = 0;

        Set<PersonalTrait> newTraits = new HashSet<>(getSortedTraits());

        AnswerResult result = new AnswerResult();
        result.setLevelUp(getLevel() > oldLevel);
        result.setNewTraitDiscovered((newTraits.size() > oldTraits.size()) && !answer.equals(Answer.UNCERTAIN));
        newTraits.removeAll(oldTraits);
        result.setNewTraits(newTraits);
        return result;
    }

    public Question nextQuestion() {
        if (!hasMoreQuestions()) {
            throw new IllegalStateException("No more questions");
        }
        return questions.getQuestion(num - numBack);
    }

    public PersonalTraits getPersonalTraits() {
        PersonalTraits result = new PersonalTraits();
        for (int i = 0; i < answers.getCount(); i++) {
            Question question = answers.getQuestion(i);
            Behavior behavior = question.getBehavior();
            result.addAll(traits.getTraitsByBehavior(behavior), answers.getQuestionAnswer(i), behavior.isReverse());
        }
        return result;
    }

    public boolean hasMoreQuestions() {
        if (num >= questions.getCount()) {
            return false;
        }
        Question result;
        do {
            result = questions.getQuestion(num);
            if (answers.isAnswered(result)) {
                num++;
            }
        } while (num < questions.getCount() && answers.isAnswered(result));
        return num < questions.getCount();
    }

    public AnswerResult skipQuestion(Question question) {
        if (isAlreadyAnswered(question)) {
            answers.remove(question);
        }
        questions = new ShuffledQuestions(questionSkipper.skip(questions.getAll(), question));
        AnswerResult answerResult = new AnswerResult();
        answerResult.setSkipped(true);
        return answerResult;
    }

    public int getAnsweredQuestionsCount() {
        return answers.getCount();
    }

    public int getTotalQuestionsCount() {
        return questions.getCount();
    }

    public void setAnswers(AnsweredQuestions answers) {
        this.answers = answers;
    }

    public ShuffledQuestions getShuffledQuestions() {
        return questions;
    }

    public AnsweredQuestions getAnswers() {
        return answers;
    }

    public int getLevel() {
        List<PersonalTrait> traits = getSortedTraits();
        if (traits.isEmpty()) {
            return 0;
        }
        return Math.abs(traits.get(0).getLevel());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPersonalTraitsCount() {
        return getSortedTraits().size();
    }

    private List<PersonalTrait> getSortedTraits() {
        return getPersonalTraits().getSorted();
    }

    public Question previousQuestion() {
        if (numBack < num) {
            numBack++;
        }
        return nextQuestion();
    }

    public boolean isAlreadyAnswered(Question question) {
        return answers.isAnswered(question);
    }

    public void forward() {
        numBack = 0;
    }
}
