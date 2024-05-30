package exception;

public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException(String message) {
        super(500, message);
    }
}
