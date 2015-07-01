import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    public void check() throws RandomPasswordGeneratorException {
        if (!getUseDigits() && !getUseLowerCaseCharacters() && !getUseUpperCaseCharacters() && !getUseSymbols()) {
            throw new RandomPasswordGeneratorException("At least one character set should be selected.");
        }

        if (getPasswordLength() < (getUseDigits() ? getMinDigitCount() : 0)
                + (getUseLowerCaseCharacters() ? getMinLowerCaseCharacterCount() : 0)
                + (getUseUpperCaseCharacters() ? getMinUpperCaseCharacterCount() : 0)
                + (getUseSymbols() ? getMinSymbolCount(): 0)) {
            throw new RandomPasswordGeneratorException("Password length should be greater than sum of minimum character counts.");
        }
    }

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

    public RandomPasswordGenerator withPasswordLength(int passwordLength) {
        this.setPasswordLength(passwordLength);

        return this;
    }

    public RandomPasswordGenerator withUpperCaseCharacters(boolean useUpperCaseCharacters) {
        this.setUseUpperCaseCharacters(useUpperCaseCharacters);

        return this;
    }

    public RandomPasswordGenerator withLowerCaseCharacters(boolean useLowerCaseCharacters) {
        this.setUseLowerCaseCharacters(useLowerCaseCharacters);

        return this;
    }

    public RandomPasswordGenerator withSymbols(boolean useSymbols) {
        this.setUseSymbols(useSymbols);

        return this;
    }

    public RandomPasswordGenerator withDigits(boolean useDigits) {
        this.setUseDigits(useDigits);

        return this;
    }

    public RandomPasswordGenerator withMinDigitCount(int minDigitCount) {
        this.setMinDigitCount(minDigitCount);

        return this;
    }

    public RandomPasswordGenerator withMinLowerCaseCharacterCount(int minLowerCaseCharacterCount) {
        this.setMinLowerCaseCharacterCount(minLowerCaseCharacterCount);

        return this;
    }

    public RandomPasswordGenerator withMinUpperCaseCharacterCount(int minUpperCaseCharacterCount) {
        this.setMinUpperCaseCharacterCount(minUpperCaseCharacterCount);

        return this;
    }

    public RandomPasswordGenerator withMinSpecialCharacterCount(int minSpecialCharacterCount) {
        this.setMinSymbolCount(minSpecialCharacterCount);

        return this;
    }

    public RandomPasswordGenerator withAvoidAmbiguousCharacters(boolean avoidAmbiguousCharacters) {
        this.setAvoidAmbiguousCharacters(avoidAmbiguousCharacters);

        return this;
    }

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
                .withMinSpecialCharacterCount(0)
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
