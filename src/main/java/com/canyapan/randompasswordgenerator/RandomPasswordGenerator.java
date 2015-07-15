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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Random Password Generator Library
 * This library generates random passwords with specified rules.
 */
public class RandomPasswordGenerator {
    private final Random random;
    private int passwordLength;
    private boolean useUpperCaseCharacters;
    private boolean useLowerCaseCharacters;
    private boolean useDigits;
    private boolean useSymbols;
    private boolean avoidAmbiguousCharacters;
    private boolean forceEveryCharacterType;
    private int minDigitCount;
    private int minLowerCaseCharacterCount;
    private int minUpperCaseCharacterCount;
    private int minSymbolCount;

    /**
     * Random Password Generator Library
     * This library generates random passwords with specified rules. Check the chain methods with 'with' prefix.
     */
    public RandomPasswordGenerator() {
        random = new SecureRandom();
    }

    private List<Character> getAllAvailableCharacters() {
        List<Character> characters = new ArrayList<Character>();

        if (getUseLowerCaseCharacters()) {
            characters.addAll(getLowerCaseCharacters());
        }

        if (getUseUpperCaseCharacters()) {
            characters.addAll(getUpperCaseCharacters());
        }

        if (getUseDigits()) {
            characters.addAll(getDigits());
        }

        if (getUseSymbols()) {
            characters.addAll(getSymbols());
        }

        Collections.shuffle(characters, random);

        return characters;
    }

    private List<CharacterType> getRequiredCharacterTypes() {
        List<CharacterType> characterTypes = new ArrayList<CharacterType>(getPasswordLength());

        if (getUseLowerCaseCharacters()) {
            for (int c = 0; c < getMinLowerCaseCharacterCount(); c++)
                characterTypes.add(CharacterType.LowerCase);
        }

        if (getUseUpperCaseCharacters()) {
            for (int c = 0; c < getMinUpperCaseCharacterCount(); c++)
                characterTypes.add(CharacterType.UpperCase);
        }

        if (getUseDigits()) {
            for (int c = 0; c < getMinDigitCount(); c++)
                characterTypes.add(CharacterType.Digit);
        }

        if (getUseSymbols()) {
            for (int c = 0; c < getMinSymbolCount(); c++)
                characterTypes.add(CharacterType.Special);
        }

        for (; characterTypes.size() < getPasswordLength(); ) {
            characterTypes.add(CharacterType.Any);
        }

        return characterTypes;
    }

    private List<Character> getCharacterList(String characters, String ambiguousCharacters, boolean avoidAmbiguousCharacters) {
        List<Character> characterList = new ArrayList<Character>();
        for (Character c : characters.toCharArray()) {
            characterList.add(c);
        }

        if (!avoidAmbiguousCharacters) {
            for (Character c : ambiguousCharacters.toCharArray()) {
                characterList.add(c);
            }
        }

        Collections.shuffle(characterList, random);

        return characterList;
    }

    private List<Character> getLowerCaseCharacters() {
        return getCharacterList("abcdefghjkmnpqrstuvwxyz", "ilo", getAvoidAmbiguousCharacters());
    }

    private List<Character> getUpperCaseCharacters() {
        return getCharacterList("ABCDEFGHJKMNPQRSTUVWXYZ", "ILO", getAvoidAmbiguousCharacters());
    }

    private List<Character> getDigits() {
        return getCharacterList("23456789", "10", getAvoidAmbiguousCharacters());
    }

    private List<Character> getSymbols() {
        return getCharacterList("!@#$%^&*", null, true);
    }

    /**
     * Checks if the generator has enough knowledge to generate a password.
     *
     * @throws RandomPasswordGeneratorException thrown when there is a problem with inputs.
     */
    private void check() throws RandomPasswordGeneratorException {
        if (!getUseDigits() && !getUseLowerCaseCharacters() && !getUseUpperCaseCharacters() && !getUseSymbols()) {
            throw new RandomPasswordGeneratorException("At least one character set should be selected.");
        }

        if (getPasswordLength() < (getUseDigits() ? getMinDigitCount() : 0)
                + (getUseLowerCaseCharacters() ? getMinLowerCaseCharacterCount() : 0)
                + (getUseUpperCaseCharacters() ? getMinUpperCaseCharacterCount() : 0)
                + (getUseSymbols() ? getMinSymbolCount() : 0)) {
            throw new RandomPasswordGeneratorException("Password length should be greater than sum of minimum character counts.");
        }
    }

    /**
     * Generates a password with specified rules.
     *
     * @return a random password.
     * @throws RandomPasswordGeneratorException thrown when there is a problem with inputs.
     *                                          At least one character set should be set before triggering generate method.
     *                                          Total sum of specified minimum characters should be less or equal than password length.
     */
    public String generate() throws RandomPasswordGeneratorException {
        if (getForceEveryCharacterType()) {
            if (getUseDigits() && getMinDigitCount() <= 0) {
                setMinDigitCount(1);
            }

            if (getUseLowerCaseCharacters() && getMinLowerCaseCharacterCount() <= 0) {
                setMinLowerCaseCharacterCount(1);
            }

            if (getUseUpperCaseCharacters() && getMinUpperCaseCharacterCount() <= 0) {
                setMinUpperCaseCharacterCount(1);
            }

            if (getUseSymbols() && getMinSymbolCount() <= 0) {
                setMinSymbolCount(1);
            }
        }

        check();

        List<CharacterType> requiredCharacterTypes = getRequiredCharacterTypes();

        Collections.shuffle(requiredCharacterTypes, random);

        List<Character> allAvailableCharacters = getAllAvailableCharacters(),
                lowerCaseCharacters = getLowerCaseCharacters(),
                upperCaseCharacters = getUpperCaseCharacters(),
                digits = getDigits(),
                specialCharacters = getSymbols();

        StringBuilder password = new StringBuilder();
        List<Character> charset;
        for (int i = 0; i < getPasswordLength(); i++) {
            switch (requiredCharacterTypes.get(i)) {
                case LowerCase:
                    charset = lowerCaseCharacters;
                    break;
                case UpperCase:
                    charset = upperCaseCharacters;
                    break;
                case Digit:
                    charset = digits;
                    break;
                case Special:
                    charset = specialCharacters;
                    break;
                case Any:
                default:
                    charset = allAvailableCharacters;
                    break;
            }

            password.append(charset.get(random.nextInt(charset.size())));
        }

        return password.toString();
    }

    /**
     * Set password length.
     * @param passwordLength A value > 0 and < 256
     * @return object itself
     */
    public RandomPasswordGenerator withPasswordLength(int passwordLength) {
        this.setPasswordLength(passwordLength);

        return this;
    }

    /**
     * Set upper case characters should be included or not.
     * @param useUpperCaseCharacters
     * @return object itself
     */
    public RandomPasswordGenerator withUpperCaseCharacters(boolean useUpperCaseCharacters) {
        this.setUseUpperCaseCharacters(useUpperCaseCharacters);

        return this;
    }

    /**
     * Set lower case characters should be included or not.
     * @param useLowerCaseCharacters
     * @return object itself
     */
    public RandomPasswordGenerator withLowerCaseCharacters(boolean useLowerCaseCharacters) {
        this.setUseLowerCaseCharacters(useLowerCaseCharacters);

        return this;
    }

    /**
     * Set symbols should be included or not.
     * @param useSymbols
     * @return object itself
     */
    public RandomPasswordGenerator withSymbols(boolean useSymbols) {
        this.setUseSymbols(useSymbols);

        return this;
    }

    /**
     * Set digits should be included or not.
     * @param useDigits
     * @return object itself
     */
    public RandomPasswordGenerator withDigits(boolean useDigits) {
        this.setUseDigits(useDigits);

        return this;
    }

    /**
     * Set minimum digit count.
     * @param minDigitCount
     * @return object itself
     */
    public RandomPasswordGenerator withMinDigitCount(int minDigitCount) {
        this.setMinDigitCount(minDigitCount);

        return this;
    }

    /**
     * Set minimum lower case character count.
     * @param minLowerCaseCharacterCount
     * @return object itself
     */
    public RandomPasswordGenerator withMinLowerCaseCharacterCount(int minLowerCaseCharacterCount) {
        this.setMinLowerCaseCharacterCount(minLowerCaseCharacterCount);

        return this;
    }

    /**
     * Set minimum upper case character count.
     * @param minUpperCaseCharacterCount
     * @return object itself
     */
    public RandomPasswordGenerator withMinUpperCaseCharacterCount(int minUpperCaseCharacterCount) {
        this.setMinUpperCaseCharacterCount(minUpperCaseCharacterCount);

        return this;
    }

    /**
     * Set minimum symbol count.
     *
     * @param minSymbolCount
     * @return object itself
     */
    public RandomPasswordGenerator withMinSymbolCount(int minSymbolCount) {
        this.setMinSymbolCount(minSymbolCount);

        return this;
    }

    /**
     * Set should ambiguous characters be avoided.
     * @param avoidAmbiguousCharacters
     * @return object itself
     */
    public RandomPasswordGenerator withAvoidAmbiguousCharacters(boolean avoidAmbiguousCharacters) {
        this.setAvoidAmbiguousCharacters(avoidAmbiguousCharacters);

        return this;
    }

    /**
     * Set the result password should contain at least 1 character from all selected character types.
     * @param forceEveryCharacterType
     * @return object itself
     */
    public RandomPasswordGenerator withForceEveryCharacterType(boolean forceEveryCharacterType) {
        this.setForceEveryCharacterType(forceEveryCharacterType);

        return this;
    }

    public RandomPasswordGenerator withDefault() {
        return this.withPasswordLength(8)
                .withLowerCaseCharacters(true)
                .withUpperCaseCharacters(true)
                .withDigits(true)
                .withSymbols(false)
                .withMinLowerCaseCharacterCount(0)
                .withMinUpperCaseCharacterCount(0)
                .withMinDigitCount(1)
                .withMinSymbolCount(0)
                .withAvoidAmbiguousCharacters(true)
                .withForceEveryCharacterType(true);
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    private void setPasswordLength(int passwordLength) {
        if (passwordLength <= 0) {
            passwordLength = 8;
        } else if (passwordLength > 256) {
            passwordLength = 256;
        }

        this.passwordLength = passwordLength;
    }

    public boolean getUseUpperCaseCharacters() {
        return useUpperCaseCharacters;
    }

    private void setUseUpperCaseCharacters(Boolean useUpperCaseCharacters) {
        this.useUpperCaseCharacters = useUpperCaseCharacters;
    }

    public boolean getUseLowerCaseCharacters() {
        return useLowerCaseCharacters;
    }

    private void setUseLowerCaseCharacters(Boolean useLowerCaseCharacters) {
        this.useLowerCaseCharacters = useLowerCaseCharacters;
    }

    public boolean getUseDigits() {
        return useDigits;
    }

    private void setUseDigits(Boolean useDigits) {
        this.useDigits = useDigits;
    }

    public boolean getUseSymbols() {
        return useSymbols;
    }

    private void setUseSymbols(Boolean useSymbols) {
        this.useSymbols = useSymbols;
    }

    public boolean getAvoidAmbiguousCharacters() {
        return avoidAmbiguousCharacters;
    }

    private void setAvoidAmbiguousCharacters(Boolean avoidAmbiguousCharacters) {
        this.avoidAmbiguousCharacters = avoidAmbiguousCharacters;
    }

    public boolean getForceEveryCharacterType() {
        return forceEveryCharacterType;
    }

    private void setForceEveryCharacterType(Boolean requireEveryCharacterType) {
        this.forceEveryCharacterType = requireEveryCharacterType;
    }

    public int getMinDigitCount() {
        return minDigitCount;
    }

    private void setMinDigitCount(int minDigitCount) {
        if (minDigitCount < 0) {
            minDigitCount = 8;
        } else if (minDigitCount > 256) {
            minDigitCount = 256;
        }

        this.minDigitCount = minDigitCount;
    }

    public int getMinLowerCaseCharacterCount() {
        return minLowerCaseCharacterCount;
    }

    public void setMinLowerCaseCharacterCount(int minLowerCaseCharacterCount) {
        if (minLowerCaseCharacterCount < 0) {
            minLowerCaseCharacterCount = 8;
        } else if (minLowerCaseCharacterCount > 256) {
            minLowerCaseCharacterCount = 256;
        }

        this.minLowerCaseCharacterCount = minLowerCaseCharacterCount;
    }

    public int getMinUpperCaseCharacterCount() {
        return minUpperCaseCharacterCount;
    }

    public void setMinUpperCaseCharacterCount(int minUpperCaseCharacterCount) {
        if (minUpperCaseCharacterCount < 0) {
            minUpperCaseCharacterCount = 8;
        } else if (minUpperCaseCharacterCount > 256) {
            minUpperCaseCharacterCount = 256;
        }

        this.minUpperCaseCharacterCount = minUpperCaseCharacterCount;
    }

    public int getMinSymbolCount() {
        return minSymbolCount;
    }

    public void setMinSymbolCount(int minSymbolCount) {
        if (minSymbolCount < 0) {
            minSymbolCount = 8;
        } else if (minSymbolCount > 256) {
            minSymbolCount = 256;
        }

        this.minSymbolCount = minSymbolCount;
    }

    enum CharacterType {
        LowerCase, UpperCase, Digit, Special, Any
    }
}
