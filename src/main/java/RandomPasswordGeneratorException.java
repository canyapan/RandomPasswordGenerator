import java.io.IOException;

public class RandomPasswordGeneratorException extends IOException {
    public RandomPasswordGeneratorException() {
        super();
    }

    public RandomPasswordGeneratorException(String message) {
        super(message);
    }

    public RandomPasswordGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
