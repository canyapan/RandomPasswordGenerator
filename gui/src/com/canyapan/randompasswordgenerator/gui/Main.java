/*
 * Copyright 2015 CAN YAPAN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canyapan.randompasswordgenerator.gui;

import com.canyapan.randompasswordgenerator.RandomPasswordGenerator;
import com.canyapan.randompasswordgenerator.RandomPasswordGeneratorException;

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
