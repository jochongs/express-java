package exception;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(401, message);
    }
}
