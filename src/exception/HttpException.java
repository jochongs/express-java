package exception;

public class HttpException {
    public int status;
    public String message;

    public HttpException(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
