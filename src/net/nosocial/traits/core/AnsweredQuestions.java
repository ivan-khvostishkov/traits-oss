package net.nosocial.traits.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ikh
 * @since 1/26/15
 */
public class AnsweredQuestions {
    List<Question> questionList = new LinkedList<>();
    List<Answer> answerList = new LinkedList<>();

    public void add(Question question, Answer answer) {
        if (isAnswered(question)) {
            throw new IllegalStateException("Already answered");
        }
        questionList.add(question);
        answerList.add(answer);
    }

    public int getCount() {
        return questionList.size();
    }

    public String getQuestionText(int i) {
        Question question = questionList.get(i);
        assert question != null;
        return question.getText();
    }

    public String getQuestionTextWithReverseIndicator(int i) {
        Question question = questionList.get(i);
        assert question != null;
        return question.getTextWithReverseIndicator();
    }

    public Answer getQuestionAnswer(int i) {
        return answerList.get(i);
    }

    public boolean isAnswered(Question question) {
        return questionList.contains(question);
    }

    public Question getQuestion(int i) {
        return questionList.get(i);
    }

    public void replace(Question question, Answer answer) {
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).equals(question)) {
                answerList.set(i, answer);
            }
        }
    }

    public void remove(Question question) {
        Integer indexToRemove = null;

        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).equals(question)) {
                indexToRemove = i;
            }
        }

        if (indexToRemove != null) {
            questionList.remove(indexToRemove.intValue());
            answerList.remove(indexToRemove.intValue());
        }
    }
}
