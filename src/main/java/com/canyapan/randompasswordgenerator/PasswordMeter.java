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

package com.canyapan.randompasswordgenerator;

/**
 * This algorithm was originally published in javascript on http://www.passwordmeter.com/
 */
public class PasswordMeter {

    /**
     * Meters strength of a given password.
     *
     * @param password A password to meter.
     * @return Strength in percent.
     */
    public static Result check(final String password) throws PasswordMeterException {
        int score, uniqueCharacters, length,
                requirements = 0, alphaUC = 0, alphaLC = 0, number = 0, symbol = 0, midChar = 0,
                alphasOnly = 0, numbersOnly = 0, repChar = 0,
                consecutiveAlphaUC = 0, consecutiveAlphaLC = 0, consecutiveNumber = 0, consecutiveSymbol = 0, consecutiveCharType = 0,
                sequentialAlpha = 0, sequentialNumber = 0, sequentialSymbol = 0, sequentialChar = 0;
        double repInc = 0d;

        if (null == password) {
            throw new PasswordMeterException("Password cannot be null");
        } else if (password.isEmpty()) {
            throw new PasswordMeterException("Password cannot be empty");
        }

        final String alphas = "abcdefghijklmnopqrstuvwxyz";
        final String numerics = "01234567890";
        final String symbols = ")!@#$%^&*()";

        length = password.length();
        String[] arrPwd = password.replaceAll("\\s+", "").split("\\s*");
        int arrPwdLen = arrPwd.length;

        int tmpAlphaUC = -1, tmpAlphaLC = -1, tmpNumber = -1, tmpSymbol = -1;
        /* Loop through password to check for Symbol, Numeric, Lowercase and Uppercase pattern matches */
        for (int a = 0; a < arrPwdLen; a++) {
            if (arrPwd[a].matches("[A-Z]")) {
                if (tmpAlphaUC != -1 && (tmpAlphaUC + 1) == a) {
                    consecutiveAlphaUC++;
                    consecutiveCharType++;
                }
                tmpAlphaUC = a;
                alphaUC++;
            } else if (arrPwd[a].matches("[a-z]")) {
                if (tmpAlphaLC != -1 && (tmpAlphaLC + 1) == a) {
                    consecutiveAlphaLC++;
                    consecutiveCharType++;
                }
                tmpAlphaLC = a;
                alphaLC++;
            } else if (arrPwd[a].matches("[0-9]")) {
                if (a > 0 && a < (arrPwdLen - 1)) {
                    midChar++;
                }
                if (tmpNumber != -1 && (tmpNumber + 1) == a) {
                    consecutiveNumber++;
                    consecutiveCharType++;
                }
                tmpNumber = a;
                number++;
            } else if (arrPwd[a].matches("[^a-zA-Z0-9_]")) {
                if (a > 0 && a < (arrPwdLen - 1)) {
                    midChar++;
                }
                if (tmpSymbol != -1 && (tmpSymbol + 1) == a) {
                    consecutiveSymbol++;
                    consecutiveCharType++;
                }
                tmpSymbol = a;
                symbol++;
            }

            /* Internal loop through password to check for repeat characters */
            boolean charExists = false;
            for (int b = 0; b < arrPwdLen; b++) {
                if (arrPwd[a].equals(arrPwd[b]) && a != b) { /* repeat character exists */
                    charExists = true;
                /*
                Calculate increment deduction based on proximity to identical characters
                Deduction is incremented each time a new match is discovered
                Deduction amount is based on total password length divided by the
                difference of distance between currently selected match
                */
                    repInc += Math.abs(arrPwdLen / (b - a));
                }
            }

            if (charExists) {
                repChar++;
                uniqueCharacters = arrPwdLen - repChar;
                repInc = uniqueCharacters > 0 ? Math.ceil(repInc / uniqueCharacters) : Math.ceil(repInc);
            }
        }

        /* Check for sequential alpha string patterns (forward and reverse) */
        for (int s = 0; s < 23; s++) {
            String fwd = alphas.substring(s, s + 3);
            String rev = new StringBuilder(fwd).reverse().toString();
            if (password.toLowerCase().contains(fwd) || password.toLowerCase().contains(rev)) {
                sequentialAlpha++;
                sequentialChar++;
            }
        }

        /* Check for sequential numeric string patterns (forward and reverse) */
        for (int s = 0; s < 8; s++) {
            String fwd = numerics.substring(s, s + 3);
            String rev = new StringBuilder(fwd).reverse().toString();
            if (password.toLowerCase().contains(fwd) || password.toLowerCase().contains(rev)) {
                sequentialNumber++;
                sequentialChar++;
            }
        }

		    /* Check for sequential symbol string patterns (forward and reverse) */
        for (int s = 0; s < 8; s++) {
            String fwd = symbols.substring(s, s + 3);
            String rev = new StringBuilder(fwd).reverse().toString();
            if (password.toLowerCase().contains(fwd) || password.toLowerCase().contains(rev)) {
                sequentialSymbol++;
                sequentialChar++;
            }
        }

        final int multiplierMidChar = 2, multiplierConsecutiveAlphaUC = 2, multiplierConsecutiveAlphaLC = 2, multiplierConsecutiveNumber = 2,
                multiplierSequentialAlpha = 3, multiplierSequentialNumber = 3, multiplierSequentialSymbol = 3,
                multiplierLength = 4, multiplierNumber = 4, multiplierSymbol = 6;
        score = length * multiplierLength;

        /* Modify overall score value based on usage vs requirements */

        /* General point assignment */
        if (alphaUC > 0 && alphaUC < length) {
            score += (length - alphaUC) * 2;
        }
        if (alphaLC > 0 && alphaLC < length) {
            score += (length - alphaLC) * 2;
        }
        if (number > 0 && number < length) {
            score += number * multiplierNumber;
        }
        if (symbol > 0) {
            score += symbol * multiplierSymbol;
        }
        if (midChar > 0) {
            score += midChar * multiplierMidChar;
        }

        /* Point deductions for poor practices */
        if ((alphaLC > 0 || alphaUC > 0) && symbol == 0 && number == 0) {  // Only Letters
            score -= length;
            alphasOnly = length;
        }
        if (alphaLC == 0 && alphaUC == 0 && symbol == 0 && number > 0) {  // Only Numbers
            score -= length;
            numbersOnly = length;
        }
        if (repChar > 0) {  // Same character exists more than once
            score -= repInc;
        }
        if (consecutiveAlphaUC > 0) {  // Consecutive Uppercase Letters exist
            score -= consecutiveAlphaUC * multiplierConsecutiveAlphaUC;
        }
        if (consecutiveAlphaLC > 0) {  // Consecutive Lowercase Letters exist
            score -= consecutiveAlphaLC * multiplierConsecutiveAlphaLC;
        }
        if (consecutiveNumber > 0) {  // Consecutive Numbers exist
            score -= consecutiveNumber * multiplierConsecutiveNumber;
        }
        if (sequentialAlpha > 0) {  // Sequential alpha strings exist (3 characters or more)
            score -= sequentialAlpha * multiplierSequentialAlpha;
        }
        if (sequentialNumber > 0) {  // Sequential numeric strings exist (3 characters or more)
            score -= sequentialNumber * multiplierSequentialNumber;
        }
        if (sequentialSymbol > 0) {  // Sequential symbol strings exist (3 characters or more)
            score -= sequentialSymbol * multiplierSequentialSymbol;
        }

        /* Determine if mandatory requirements have been met and set image indicators accordingly */
        int minPwdLen = 8;
        if (length == minPwdLen) {
            requirements++;
        } else if (length > minPwdLen) {
            requirements++;
        }

        if (alphaUC == 1) {
            requirements++;
        } else if (alphaUC > 1) {
            requirements++;
        }

        if (alphaLC >= 1) {
            requirements++;
        }

        if (number >= 1) {
            requirements++;
        }

        if (symbol >= 1) {
            requirements++;
        }

        int minimumRequirementsChars = password.length() >= minPwdLen ? 3 : 4;
        if (requirements > minimumRequirementsChars) {  // One or more required characters exist
            score += requirements * 2;
        }

        /* Determine complexity based on overall score */
        if (score > 100) {
            score = 100;
        } else if (score < 0) {
            score = 0;
        }

        return new Result(score);
    }

    public enum Complexity {
        TooWeak("Too Weak"), Weak("Weak"), Good("Good"), Strong("Strong"), VeryStrong("Very Strong");

        private final String text;

        Complexity(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public static class Result {
        private final int score;
        private Complexity complexity;

        private Result(int score) {
            this.score = score;

            if (score >= 0 && score < 20) {
                complexity = Complexity.TooWeak;
            } else if (score >= 20 && score < 40) {
                complexity = Complexity.Weak;
            } else if (score >= 40 && score < 60) {
                complexity = Complexity.Good;
            } else if (score >= 60 && score < 80) {
                complexity = Complexity.Strong;
            } else if (score >= 80 && score <= 100) {
                complexity = Complexity.VeryStrong;
            }
        }

        public int getScore() {
            return score;
        }

        public Complexity getComplexity() {
            return complexity;
        }
    }
}
