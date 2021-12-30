package net.nosocial.traits.core;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ikh
 * @since 1/26/15
 */
public class AnswersDb {
    private final File db;

    public AnswersDb(File file) throws IOException {
        this.db = file;

        if (!this.db.exists()) {
            boolean newFile = this.db.createNewFile();
            if (!newFile) {
                throw new IllegalArgumentException("cannot create new file");
            }
        }
    }

    public void save(AnsweredQuestions answeredQuestions) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(db, "UTF-8")) {
            for (int i = 0; i < answeredQuestions.getCount(); i++) {
                writer.print(answeredQuestions.getQuestionText(i));
                writer.print(String.format(" [%s]", answeredQuestions.getQuestionAnswer(i).name()));
                writer.println();
            }
        }

    }

    public AnsweredQuestions load() throws IOException {
        AnsweredQuestions result = new AnsweredQuestions();
        try (Reader book = new InputStreamReader(new FileInputStream(db), "UTF-8")) {
            BufferedReader reader = new BufferedReader(book);
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Pattern p = Pattern.compile("(.*) \\[(.+)\\]");
                Matcher m = p.matcher(line);
                if (!m.matches()) {
                    throw new IllegalStateException("Answers db corrupted, not matching format: " + line);
                }
                String question = m.group(1);
                String answer = m.group(2);
                result.add(new Question(new Behavior(question)), Answer.valueOf(answer));
            }
        }
        return result;
    }
}
