package net.nosocial.traits;

import net.nosocial.traits.core.*;
import net.nosocial.traits.gui.TraitsForm;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author ikh
 * @since 1/26/15
 */
public class Traits {
    private static final String DEFAULT_PROFILE_NAME = "self";

    private static String dbFile = null;

    private File questionsFile;
    private File answersFile;

    private QuestionsDb questionsDb;
    private AnswersDb answersDb;
    private CharacterTraits traits;
    private TraitsProfile profile;

    public static void main(String[] args) throws IOException {
        List<String> argsList = Arrays.asList(args);

        if (argsList.contains("--version")) {
            displayVersion();
            return;
        }

        welcome();

        if (argsList.contains("--help")) {
            displayHelp();
            return;
        }

        if (argsList.contains("--about")) {
            displayAbout();
            return;
        }

        for (String s : argsList) {
            if (s.startsWith("--db=")) {
                setDatabase(s.substring(5));
                break;
            }
        }

        Traits traits = new Traits();
        int profileNameIndex = 0;

        while (args.length > 0 && args.length > profileNameIndex && args[profileNameIndex].startsWith("-")) {
            profileNameIndex++;
        }

        String profileName = args.length <= profileNameIndex
                ? queryProfileName()
                : args[profileNameIndex];

        traits.init(profileName);

        if (argsList.contains("--profile")) {
            traits.displayProfile();
        } else if (argsList.contains("--cli")) {
            traits.displaySummary();
            traits.answerLoop();
        } else {
            traits.displaySummary();
            TraitsForm.answerLoop(traits);
        }
    }

    private static void welcome() {
        System.out.println("Welcome to NSN Traits v2.0 - your daily assistant in learning a personality!");
        System.out.println();
    }

    private static void displayAbout() {
        System.out.println("Answer as many questions as possible to get the personal profile of positive and negative traits.\n" +
                "\n" +
                "* Self awareness\n" +
                "  Learn about behaviors, discover your traits and develop emotional intelligence\n" +
                "\n" +
                "* Dealing with people\n" +
                "  The more self-aware you are, the more skilled you are at reading personality of others\n" +
                "\n" +
                "* A key to life success\n" +
                "  Use NSN Traits v2.0 day by day and watch how your life becomes easier and more successful\n" +
                "\n" +
                "Copyright (c) NSN 2015-2022.\n" +
                "\n" +
                "For help and support contact traits@nosocial.net.\n");
    }

    private static void displayVersion() {
        System.out.println("v2.0.23");
    }

    private static void displayHelp() {
        System.out.println("Usage: traits.jar [options] [profile_name]\n" +
                "\n" +
                "Options:\n" +
                "    --help                show this help\n" +
                "    --version             show version\n" +
                "    --about               about info\n" +
                "    --cli                 use command-line interface instead of GUI\n" +
                "    --profile             show traits profile and exit\n" +
                "    --db=[db_file]        database file to use (default - 'traits.db')\n" +
                "\n" +
                "Profile name:\n" +
                "    profile_name          name of the profile (default - 'self')\n" +
                "\n" +
                "For help and support contact traits@nosocial.net.\n");
    }

    private static String queryProfileName() throws IOException {
        System.out.printf("Enter profile name [%s]: ", DEFAULT_PROFILE_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String result = reader.readLine();
        if (result.trim().isEmpty()) {
            result = DEFAULT_PROFILE_NAME;
        }
        System.out.println();
        return result;
    }

    public static synchronized String databaseFile() {
        return dbFile;
    }

    public static synchronized void setDatabase(String dbFile) {
        Traits.dbFile = dbFile;
    }

    private void displayProfile() {
        displaySummary();
        List<PersonalTrait> personalTraitsList = profile.getSortedTraits();
        if (personalTraitsList.isEmpty()) {
            return;
        }

        System.out.println();
        System.out.println(getLevelsText(profile));
    }

    private String getLevelsText(TraitsProfile profile) {
        StringBuilder result = new StringBuilder();
        for (PersonalTrait personalTrait : profile.getSortedTraits()) {
            result.append(String.format("%4d %s%s%n", personalTrait.getLevel(),
                    personalTrait.getTraitSignChar(),
                    personalTrait.getTraitName().toLowerCase()));
        }
        return result.toString();
    }

    private synchronized void init(String profileName) throws IOException {
        initDB(profileName);

        TraitsDb traitsDb;
        traitsDb = new TraitsDb(Objects.requireNonNullElse(Traits.databaseFile(),
                "traits.db"), true);
        traits = traitsDb.loadAll();

        questionsDb = new QuestionsDb(questionsFile);
        answersDb = new AnswersDb(answersFile);

        ShuffledQuestions questions = questionsDb.load();
        AnsweredQuestions answers = answersDb.load();

        if (questions.getCount() == 0) {
            System.out.printf("Creating the new profile for %s and randomizing the questions.%n%n", profileName);
            questions = new RandomQuestionsShuffler().shuffle(traits.getAllQuestions());
        }

        profile = new TraitsProfile(traits, questions, new RandomQuestionsSkipper());
        saveQuestionsDb();

        if (answers.getCount() > 0) {
            profile.setAnswers(answers);
        }

        profile.setName(profileName);
    }

    private synchronized void displaySummary() {
        System.out.println(getSummaryText());
    }

    private String getSummaryText() {
        return String.format("You answered %d out of %d questions for %s and discovered %d traits out of %d.\n" +
                        "Profile level is %d.",
                profile.getAnsweredQuestionsCount(), profile.getTotalQuestionsCount(), profile.getName(),
                profile.getPersonalTraitsCount(), traits.getCount(),
                profile.getLevel());
    }

    private void answerLoop() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (profile.hasMoreQuestions()) {
            Question question = profile.nextQuestion();

            AnswerResult answerResult = null;

            while (answerResult == null) {
                System.out.println();
                System.out.printf("%s [s/y/n/u/i/p/b/f/?] ", OSUtils.isWindows()
                        ? question.getLatin1Text()
                        : question.getText());
                String line = reader.readLine();
                if (line == null) {
                    return;
                }
                String answerChar = line.trim().toLowerCase();
                switch (answerChar) {
                    case "?":
                        System.out.println();
                        System.out.println("s - skip (to answer later)");
                        System.out.println("y - yes (adds one level to the trait)");
                        System.out.println("n - no (subtracts one level from the trait)");
                        System.out.println("u - uncertain (doesn't change the level of the trait)");
                        System.out.println("i - behavior info / associated trait hint");
                        System.out.println("p - show profile");
                        System.out.println("b - back to the previous question");
                        System.out.println("f - fast-forward to the last question");
                        System.out.println("? - this help");
                        break;
                    case "i":
                        System.out.println();
                        System.out.println(getBehaviorInfo());
                        break;
                    case "p":
                        System.out.println();
                        displayProfile();
                        break;
                    case "s":
                        answerResult = skipQuestion();
                        break;
                    case "y":
                        answerResult = answerYes();
                        break;
                    case "n":
                        answerResult = answerNo();
                        break;
                    case "u":
                        answerResult = answerUncertain();
                        break;
                    case "b":
                        goBack();
                        answerResult = new AnswerResult();
                        break;
                    case "f":
                        goForward();
                        answerResult = new AnswerResult();
                        break;
                    default:
                        System.out.println();
                        System.out.println("Invalid choice, type ? for help.");
                        break;
                }
            }

            if (answerResult.isLevelUp()) {
                System.out.println();
                System.out.println(getLevelUpMessage());
            }
            if (answerResult.isNewTraitDiscovered()) {
                System.out.println();
                System.out.println(getNewTraitsMessage(answerResult));
            }
        }

        if (!profile.hasMoreQuestions()) {
            System.out.print(getProfileCompleteMessage() + "\n\n" +
                    "Do you want to get back to review the answers or see the final profile? [b/p] ");
            String exitAnswer = reader.readLine();
            if (exitAnswer.equals("b")) {
                goBack();
                answerLoop(); // recursive workaround
                return;
            }
        }

        displayProfile();
        System.out.println();
        System.out.println(getProfileCompleteMessage());
    }

    private void initDB(String profileName) throws IOException {
        File traitsConfigDir = new File(System.getProperty("user.home"), ".traits");
        if (!traitsConfigDir.exists()) {
            traitsConfigDir.mkdir();
        }
        File traitsProfile = new File(traitsConfigDir, profileName);
        if (!traitsProfile.exists()) {
            traitsProfile.mkdir();
        }
        questionsFile = new File(traitsProfile, "questions");
        if (!questionsFile.exists()) {
            boolean newFile = questionsFile.createNewFile();
            if (!newFile) {
                throw new IllegalStateException("Cannot create questions file " + questionsFile.getAbsolutePath());
            }
        }
        answersFile = new File(traitsProfile, "answers");
        if (!answersFile.exists()) {
            boolean newFile = answersFile.createNewFile();
            if (!newFile) {
                throw new IllegalStateException("Cannot create answers file " + answersFile.getAbsolutePath());
            }
        }
    }

    private void saveAnswersDb() {
        try {
            answersDb.save(profile.getAnswers());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private void saveQuestionsDb() {
        try {
            questionsDb.save(profile.getShuffledQuestions());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized String getProfileName() {
        return profile.getName();
    }

    public synchronized String getCurrentQuestionText() {
        return profile.nextQuestion().getText();
    }

    public synchronized String getProfileText() {
        return getSummaryText() + "\n\n" + getLevelsText(profile);
    }

    public synchronized String getBehaviorInfo() {
        Question question = profile.nextQuestion();
        SimilarTraits similarTraits = traits.getTraitsByBehavior(question.getBehavior());
        StringBuilder sb = new StringBuilder();
        for (Trait trait : similarTraits.getTraits()) {
            sb.append(String.format("This behavior is associated with %s trait (%s).%n",
                    trait.getName(), trait.isPositive() ? "positive" : "negative"));
        }
        return sb.toString();
    }

    public synchronized String getQuestionCountText() {
        return String.format("Question %d of %d", profile.getCurrentQuestionNumber(),
                profile.getTotalQuestionsCount());
    }

    public synchronized AnswerResult answerYes() {
        Question question = profile.nextQuestion();
        AnswerResult answerResult = profile.answerQuestion(question, Answer.YES);
        saveAnswersDb();
        return answerResult;
    }

    public synchronized AnswerResult answerNo() {
        Question question = profile.nextQuestion();
        AnswerResult answerResult = profile.answerQuestion(question, Answer.NO);
        saveAnswersDb();
        return answerResult;
    }

    public synchronized AnswerResult answerUncertain() {
        Question question = profile.nextQuestion();
        AnswerResult answerResult = profile.answerQuestion(question, Answer.UNCERTAIN);
        saveAnswersDb();
        return answerResult;
    }

    public synchronized AnswerResult skipQuestion() {
        Question question = profile.nextQuestion();
        AnswerResult answerResult = profile.skipQuestion(question);
        saveAnswersDb();
        saveQuestionsDb();
        return answerResult;
    }

    public synchronized String getLevelUpMessage() {
        return String.format("Congratulations, level up! New profile level is %d.%n",
                profile.getLevel());
    }

    public synchronized String getNewTraitsMessage(AnswerResult answerResult) {
        return String.format("Congratulations, new traits(s) discovered: %s%n",
                String.join(", ", answerResult.getNewTraitsAsStrings()));
    }

    public synchronized boolean noMoreQuestions() {
        return !profile.hasMoreQuestions();
    }

    public synchronized String getProfileCompleteMessage() {
        return "Congratulations! No more questions left. Traits profile is complete.";
    }

    public synchronized void goBack() {
        /* question = */
        profile.previousQuestion();
    }

    public synchronized void goForward() {
        profile.forward();
    }
}
