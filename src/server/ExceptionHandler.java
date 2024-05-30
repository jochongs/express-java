package server;

import exception.HttpException;
import request.Request;
import response.Response;

@FunctionalInterface
public interface ExceptionHandler {
    void handleException(HttpException exception, Request req, Response res);
}
