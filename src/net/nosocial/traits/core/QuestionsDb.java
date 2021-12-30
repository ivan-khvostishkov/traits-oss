package net.nosocial.traits.core;

import java.io.*;

/**
 * @author ikh
 * @since 1/26/15
 */
public class QuestionsDb {
    private final File db;

    public QuestionsDb(File db) {
        this.db = db;
    }

    public void save(ShuffledQuestions shuffledQuestions) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(db, "UTF-8")) {
            for (int i = 0; i < shuffledQuestions.getCount(); i++) {
                writer.print(shuffledQuestions.getQuestionTextWithBehaviorIndication(i));
                writer.println();
            }
        }
    }

    public ShuffledQuestions load() throws IOException {
        ShuffledQuestions result = new ShuffledQuestions();
        try (Reader book = new InputStreamReader(new FileInputStream(db), "UTF-8")) {
            BufferedReader reader = new BufferedReader(book);
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.add(new Question(new Behavior(line)));
            }
        }
        return result;
    }
}
