package server;

import server.exception.HttpException;
import server.request.Request;
import server.response.Response;

@FunctionalInterface
public interface ExceptionHandler {
    void handleException(HttpException exception, Request req, Response res);
}
