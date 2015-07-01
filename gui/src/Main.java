import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main implements ActionListener {
    private JPanel panel;
    private JSpinner spinnerPasswordLength;
    private JCheckBox checkBoxLowerCase;
    private JCheckBox checkBoxUpperCase;
    private JCheckBox checkBoxDigits;
    private JCheckBox checkBoxSymbols;
    private JCheckBox checkBoxAvoidAmbiguousCharacters;
    private JCheckBox checkBoxForceEveryCharacterType;
    private JTextField textFieldPassword;
    private JButton buttonGenerate;

    public Main() {
        buttonGenerate.addActionListener(this);
        spinnerPasswordLength.setValue(8);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Random Password Generator");
        frame.setContentPane(new Main().panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(buttonGenerate)) {
            RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator()
                    .withPasswordLength((Integer) spinnerPasswordLength.getValue())
                    .withLowerCaseCharacters(checkBoxLowerCase.isSelected())
                    .withUpperCaseCharacters(checkBoxUpperCase.isSelected())
                    .withDigits(checkBoxDigits.isSelected())
                    .withSymbols(checkBoxSymbols.isSelected())
                    .withAvoidAmbiguousCharacters(checkBoxAvoidAmbiguousCharacters.isSelected())
                    .withForceEveryCharacterType(checkBoxForceEveryCharacterType.isSelected());
            try {
                textFieldPassword.setText(passwordGenerator.generate());
            } catch (RandomPasswordGeneratorException ex) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Password generation failed.", ex);
            }
        }
    }
}
