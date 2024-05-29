package server;

import request.Request;
import response.Response;

@FunctionalInterface
public interface RequestHandler {
    void execute(Request request, Response response);
}
