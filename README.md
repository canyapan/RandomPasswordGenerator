RandomPasswordGenerator is a random password generation application developed in Java 1.5 compatible.<br />
<br />
Usage:<br />
<br />
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
