package server;

import server.exception.HttpException;
import server.request.Request;
import server.response.Response;

@FunctionalInterface
public interface RequestHandler {
    void execute(Request request, Response response, NextHandler next) throws HttpException;
}
