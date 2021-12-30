package net.nosocial.traits;

import net.nosocial.traits.core.*;
import net.nosocial.traits.gui.TraitsForm;

import javax.swing.*;
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

    public static String dbFile = null;

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
                dbFile = args[0].substring(5);
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
            traits.answerLoop();
        } else {
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

    public static void displayVersion() {
        System.out.println("v2.0.23");
    }

    public static void displayHelp() {
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
        return result;
    }

    public String databaseFile() {
        return dbFile;
    }

    private void displayProfile() {
        System.out.println();
        displaySummary();
        PersonalTraits personalTraits = profile.getPersonalTraits();
        List<PersonalTrait> personalTraitsList = personalTraits.getSorted();
        if (personalTraitsList.isEmpty()) {
            return;
        }
        System.out.println();
        for (PersonalTrait personalTrait : personalTraitsList) {
            System.out.printf("%4d %s%s%n", personalTrait.getLevel(),
                    personalTrait.getTraitSignChar(),
                    personalTrait.getTraitName().toLowerCase());
        }
    }

    private void init(String profileName) throws IOException {
        initDB(profileName);

        TraitsDb traitsDb;
        traitsDb = new TraitsDb(Objects.requireNonNullElse(dbFile, "traits.db"), true);
        traits = traitsDb.loadAll();

        questionsDb = new QuestionsDb(questionsFile);
        answersDb = new AnswersDb(answersFile);

        ShuffledQuestions questions = questionsDb.load();
        AnsweredQuestions answers = answersDb.load();

        if (questions.getCount() == 0) {
            questions = new RandomQuestionsShuffler().shuffle(traits.getAllQuestions());
            questionsDb.save(questions);
        }

        profile = new TraitsProfile(traits, questions, new RandomQuestionsSkipper());

        if (answers.getCount() > 0) {
            profile.setAnswers(answers);
        }

        profile.setName(profileName);

        if (profile.hasMoreQuestions()) {
            displaySummary();
        }
    }

    private void displaySummary() {
        System.out.printf("You answered %d out of %d questions for %s and discovered %d traits out of %d." +
                        " Profile level is %d.%n",
                profile.getAnsweredQuestionsCount(), profile.getTotalQuestionsCount(), profile.getName(),
                profile.getPersonalTraitsCount(), traits.getCount(),
                profile.getLevel());
    }

    public void answerLoop() throws IOException {
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
                        System.out.println("f - forward towards the last question");
                        System.out.println("? - this help");
                        break;
                    case "i":
                        System.out.println();
                        SimilarTraits similarTraits = traits.getTraitsByBehavior(question.getBehavior());
                        for (Trait trait : similarTraits.getTraits()) {
                            System.out.printf("This behavior is associated with %s trait (%s).%n",
                                    trait.getName(), trait.isPositive() ? "positive" : "negative");
                        }
                        break;
                    case "p":
                        displayProfile();
                        break;
                    case "s":
                        answerResult = profile.skipQuestion(question);
                        questionsDb.save(profile.getShuffledQuestions());
                        break;
                    case "y":
                        answerResult = profile.answerQuestion(question, Answer.YES);
                        answersDb.save(profile.getAnswers());
                        break;
                    case "n":
                        answerResult = profile.answerQuestion(question, Answer.NO);
                        answersDb.save(profile.getAnswers());
                        break;
                    case "u":
                        answerResult = profile.answerQuestion(question, Answer.UNCERTAIN);
                        answersDb.save(profile.getAnswers());
                        break;
                    case "b":
                        /* question = */
                        profile.previousQuestion();
                        answerResult = new AnswerResult();
                        break;
                    case "f":
                        profile.forward();
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
                System.out.printf("Congratulations, level up! New profile level is %d.%n",
                        profile.getLevel());
            }
            if (answerResult.isNewTraitDiscovered()) {
                System.out.println();
                System.out.printf("Congratulations, new traits(s) discovered: %s%n",
                        String.join(", ", answerResult.getNewTraitsAsStrings()));
            }
        }

        // TODO: how to roll back the last question
        displayProfile();
        System.out.println();
        System.out.println("No more questions. Profile complete!");
        reader.readLine();
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
}
