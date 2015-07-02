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

package com.canyapan.randompasswordgenerator.test;

import com.canyapan.randompasswordgenerator.RandomPasswordGenerator;
import com.canyapan.randompasswordgenerator.RandomPasswordGeneratorException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RandomPasswordGeneratorTest {

    @Test
    public void testGenerateScenario1() throws RandomPasswordGeneratorException {
        RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator().withPasswordLength(16)
                .withLowerCaseCharacters(true)
                .withUpperCaseCharacters(true)
                .withDigits(true)
                .withMinDigitCount(3)
                .withAvoidAmbiguousCharacters(true)
                .withForceEveryCharacterType(true);

        String password = passwordGenerator.generate();
        assertTrue(password, password.matches("(?=^.{16}$)(?=(.*\\d){3})(?=.*[A-Za-z])(?!.*[iloILO01])(?!.*[!@#$%^&*])(?!.*[\\s])^.*"));
    }

    @Test
    public void testGenerateScenario2() throws RandomPasswordGeneratorException {
        RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator().withPasswordLength(8)
                .withDigits(true);

        String password = passwordGenerator.generate();
        assertTrue(password, password.matches("(?=^.{8}$)(?=\\d{8})(?!.*[A-Za-z])(?!.*[!@#$%^&*])(?!.*[\\s])^.*"));
    }

    @Test
    public void testGenerateScenario3() throws RandomPasswordGeneratorException {
        RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator().withPasswordLength(8)
                .withLowerCaseCharacters(true)
                .withUpperCaseCharacters(true)
                .withDigits(true)
                .withSymbols(true)
                .withMinDigitCount(1)
                .withAvoidAmbiguousCharacters(true)
                .withForceEveryCharacterType(true);

        String password = passwordGenerator.generate();
        assertTrue(password, password.matches("(?=^.{8}$)(?=.*\\d)(?=.*[A-Za-z])(?=.*[!@#$%^&*])(?!.*[iloILO01])(?!.*[\\s])^.*"));
    }

    @Test(expected = RandomPasswordGeneratorException.class)
    public void testGenerateScenarioException1() throws RandomPasswordGeneratorException {
        RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator().withPasswordLength(8);

        try {
            passwordGenerator.generate();
            fail("An exception should have been thrown here.");
        } catch (RandomPasswordGeneratorException e) {
            if (!e.getMessage().equals("At least one character set should be selected.")) {
                fail("Exception error message wrong.");
            }

            throw e;
        }
    }

    @Test(expected = RandomPasswordGeneratorException.class)
    public void testGenerateScenarioException2() throws RandomPasswordGeneratorException {
        RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator().withPasswordLength(8)
                .withDigits(true)
                .withMinDigitCount(12);

        try {
            passwordGenerator.generate();
            fail("An exception should have been thrown here.");
        } catch (RandomPasswordGeneratorException e) {
            if (!e.getMessage().equals("Password length should be greater than sum of minimum character counts.")) {
                fail("Exception error message wrong.");
            }

            throw e;
        }
    }

    //assertTrue(password.matches("(?=^.{8,30}$)(?=(.*\\d){2})(?=(.*[A-Za-z]){2})(?=.*[!@#$%^&*])(?!.*[\\s])^.*"));
}
