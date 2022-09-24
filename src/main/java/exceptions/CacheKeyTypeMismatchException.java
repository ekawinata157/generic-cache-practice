package exceptions;

public class CacheKeyTypeMismatchException extends RuntimeException {
    public CacheKeyTypeMismatchException(String message) {
        super(message);
    }
}
