package exception;

public class ConflictException extends HttpException {
    public ConflictException(String message) {
        super(409, message);
    }
}
