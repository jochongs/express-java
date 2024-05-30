package server.exception;

public class ForbiddenException extends HttpException {
    public ForbiddenException(String message) {
        super(403, message);
    }
}
