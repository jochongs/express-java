package server.exception;

public class HttpException extends Exception {
    public int status;
    public String message;

    public HttpException(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
