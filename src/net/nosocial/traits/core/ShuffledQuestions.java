package net.nosocial.traits.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ikh
 * @since 1/26/15
 */
public class ShuffledQuestions {
    private List<Question> questions = new LinkedList<>();

    public ShuffledQuestions() {
    }

    public ShuffledQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void add(Question question) {
        questions.add(question);
    }

    public int getCount() {
        return questions.size();
    }

    public String getQuestionText(int i) {
        Question question = questions.get(i);
        assert question != null;
        return question.getText();
    }

    public boolean hasQuestion(Question question) {
        return questions.contains(question);
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public List<Question> getAll() {
        return questions;
    }

    public String getQuestionTextWithBehaviorIndication(int i) {
        Question q = questions.get(i);
        return q.getTextWithReverseIndicator();
    }
}
