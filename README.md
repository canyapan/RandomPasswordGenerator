RandomPasswordGenerator is a Java 1.5 compatible random password generation library.<br />

Usage:<br />

```java
RandomPasswordGenerator passwordGenerator = new RandomPasswordGenerator()
  .withPasswordLength(8)
  .withLowerCaseCharacters(true)
  .withUpperCaseCharacters(true)
  .withDigits(true)
  .withSymbols(true)
  .withMinDigitCount(1)
  .withAvoidAmbiguousCharacters(true)
  .withForceEveryCharacterType(true);

String password = passwordGenerator.generate();
```
