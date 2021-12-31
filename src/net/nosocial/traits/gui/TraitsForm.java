package net.nosocial.traits.gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import net.nosocial.traits.Traits;
import net.nosocial.traits.core.AnswerResult;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class TraitsForm {
    private JPanel mainPanel;
    private JButton uncertainButton;
    private JButton yesButton;
    private JButton noButton;
    private JButton profileButton;
    private JButton infoButton;
    private JButton skipButton;
    private JButton aboutButton;
    private JButton button1;
    private JButton button2;
    private JTextPane questionTextPane;
    private JLabel profileLabel;
    private JLabel questionCountLabel;

    private Traits traits;


    public TraitsForm() {
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, traits == null ? "You answered 36 of 5271 questions for self.\n" +
                                "\n" +
                                "   3 +happy\n" +
                                "   2 -rowdy\n" +
                                "   2 -hypersensitive\n" +
                                "\n" : traits.getProfileText(),
                        traits == null ? "Profile — johndoe" : traits.getProfileName(),
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutForm.displayDialog();
            }
        });
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel,
                        traits == null ? "This behavior is associated with SOCIALLY RESPONSIBLE trait (positive).\n\n"
                                : traits.getBehaviorInfo(),
                        "Traits info", JOptionPane.PLAIN_MESSAGE);
            }
        });
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnswerResult answerResult = traits.answerYes();

                if (answerResult.isLevelUp()) {
                    JOptionPane.showMessageDialog(mainPanel,
                            traits.getLevelUpMessage(),
                            "New profile level!", JOptionPane.PLAIN_MESSAGE);
                }
                if (answerResult.isNewTraitDiscovered()) {
                    JOptionPane.showMessageDialog(mainPanel,
                            traits.getNewTraitsMessage(answerResult),
                            "New traits!", JOptionPane.PLAIN_MESSAGE);
                }

                TraitsForm.this.updateQuestion();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traits.goBack();
                TraitsForm.this.updateQuestion();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traits.goForward();
                TraitsForm.this.updateQuestion();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("NSN Traits v2.0 — traits.db");
        frame.setContentPane(new TraitsForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // center
        frame.setVisible(true);
    }

    public static void answerLoop(Traits traits) {
        JFrame frame = new JFrame("NSN Traits v2.0 — " + Traits.databaseFile());

        TraitsForm traitsForm = new TraitsForm();
        traitsForm.traits = traits;

        frame.setContentPane(traitsForm.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // center

        traitsForm.updateProfileName();
        traitsForm.updateQuestion();

        frame.setVisible(true);
    }

    private void updateProfileName() {
        profileLabel.setText(traits.getProfileName());
    }

    private void updateQuestion() {

        if (traits.noMoreQuestions()) {
            JFrame frame = (JFrame) SwingUtilities.getRoot(mainPanel);
            frame.setVisible(false);

            int choice = JOptionPane.showOptionDialog(mainPanel,
                    traits.getProfileCompleteMessage() + "\n\n"
                            + "Do you want to get back to review the answers or see the final profile?",
                    "Well done!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Back", "Profile"}, "Back");

            if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }

            traits.goBack();
            frame.setVisible(true);
        }

        questionTextPane.setText(String.format("<html>\n" +
                "  <head>\n" +
                "    <style type=\"text/css\">\n" +
                "      <!--\n" +
                "        p { font-family: sans-serif }\n" +
                "      -->\n" +
                "    </style>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <p style=\"margin-top: 0\">\n" +
                "      <font size=\"18\">%s</font>\n" +
                "    </p>\n" +
                "  </body>\n" +
                "</html>\n", traits.getCurrentQuestionText()));
        questionCountLabel.setText(traits.getQuestionCountText());
    }

    private void createUIComponents() {
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FormLayout("fill:d:grow", "center:max(d;20px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;8dlu):noGrow,top:4dlu:noGrow,fill:d:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;20px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;20px):noGrow"));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow"));
        CellConstraints cc = new CellConstraints();
        mainPanel.add(panel1, cc.xy(1, 19));
        yesButton = new JButton();
        yesButton.setText("Yes");
        yesButton.setMnemonic('Y');
        yesButton.setDisplayedMnemonicIndex(0);
        yesButton.setToolTipText("Yes (adds one level to the trait)");
        panel1.add(yesButton, cc.xy(3, 1));
        uncertainButton = new JButton();
        uncertainButton.setText("Uncertain");
        uncertainButton.setMnemonic('U');
        uncertainButton.setDisplayedMnemonicIndex(0);
        uncertainButton.setToolTipText("Uncertain (both yes and no, neither yes nor no, doesn't change the level of the trait)");
        panel1.add(uncertainButton, cc.xy(7, 1));
        noButton = new JButton();
        noButton.setText("No");
        noButton.setMnemonic('N');
        noButton.setDisplayedMnemonicIndex(0);
        noButton.setToolTipText("No (subtracts one level from the trait)");
        panel1.add(noButton, cc.xy(11, 1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        mainPanel.add(panel2, cc.xy(1, 15));
        questionCountLabel = new JLabel();
        Font questionCountLabelFont = this.$$$getFont$$$(null, -1, 16, questionCountLabel.getFont());
        if (questionCountLabelFont != null) questionCountLabel.setFont(questionCountLabelFont);
        questionCountLabel.setHorizontalAlignment(0);
        questionCountLabel.setHorizontalTextPosition(0);
        questionCountLabel.setText("Question 37 of 5271");
        panel2.add(questionCountLabel, cc.xy(1, 1));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,fill:m:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "fill:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        mainPanel.add(panel3, cc.xy(1, 7, CellConstraints.DEFAULT, CellConstraints.CENTER));
        button1 = new JButton();
        button1.setText("←");
        button1.setToolTipText("Back to the previous question");
        panel3.add(button1, cc.xywh(3, 1, 1, 3, CellConstraints.DEFAULT, CellConstraints.CENTER));
        button2 = new JButton();
        button2.setText("⇥");
        button2.setToolTipText("Fast-forward to the last question");
        panel3.add(button2, cc.xywh(8, 1, 1, 3, CellConstraints.DEFAULT, CellConstraints.CENTER));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow"));
        mainPanel.add(panel4, cc.xy(1, 11));
        infoButton = new JButton();
        infoButton.setText("Info");
        infoButton.setMnemonic('I');
        infoButton.setDisplayedMnemonicIndex(0);
        infoButton.setToolTipText("Give info about traits associated with this behavior");
        panel4.add(infoButton, cc.xy(3, 1));
        skipButton = new JButton();
        skipButton.setText("Skip");
        skipButton.setMnemonic('S');
        skipButton.setDisplayedMnemonicIndex(0);
        skipButton.setToolTipText("Skip the question to answer at later time");
        panel4.add(skipButton, cc.xy(7, 1));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:82px:noGrow,left:6dlu:noGrow,fill:max(d;4px):grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow"));
        mainPanel.add(panel5, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        profileButton = new JButton();
        profileButton.setText("Profile");
        profileButton.setToolTipText("Show traits profile (levels)");
        panel5.add(profileButton, cc.xy(11, 1));
        aboutButton = new JButton();
        aboutButton.setText("About");
        aboutButton.setToolTipText("About this app");
        panel5.add(aboutButton, cc.xy(3, 1));
        profileLabel = new JLabel();
        Font profileLabelFont = this.$$$getFont$$$(null, -1, 16, profileLabel.getFont());
        if (profileLabelFont != null) profileLabel.setFont(profileLabelFont);
        profileLabel.setText("self");
        panel5.add(profileLabel, cc.xy(7, 1));
        questionTextPane = new JTextPane();
        questionTextPane.setContentType("text/html");
        questionTextPane.setMargin(new Insets(30, 30, 30, 30));
        questionTextPane.setMinimumSize(new Dimension(700, 150));
        questionTextPane.setOpaque(false);
        questionTextPane.setPreferredSize(new Dimension(1100, 150));
        questionTextPane.setText("<html>\n  <head>\n    <style type=\"text/css\">\n      <!--\n        p { font-family: sans-serif }\n      -->\n    </style>\n    \n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <font size=\"18\">Taking responsibility for other people (watching a \n      friend to make sure she gets home safely)</font>\n    </p>\n  </body>\n</html>\n");
        mainPanel.add(questionTextPane, cc.xy(1, 9, CellConstraints.FILL, CellConstraints.FILL));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
