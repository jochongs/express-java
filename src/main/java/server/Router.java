package server;

import server.exception.HttpException;
import server.request.Request;
import server.response.Response;

import java.util.Arrays;
import java.util.HashMap;

class PathNotFoundException extends Exception {
    public PathNotFoundException(String message) {
        super(message);
    }
}

class Router {
    public final TrieNode root;

    Router() {
        root = new TrieNode();
    }

    void addRoute(String path, RequestHandler ...requestHandlers) {
        String[] parts = path.split("/");
        TrieNode currNode = root;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            String key = part.startsWith(":") ? ":" : part;

            if (part.startsWith(":")) {
                currNode.paramKey = part.substring(1);
            }

            currNode.children.putIfAbsent(key, new TrieNode());
            currNode = currNode.children.get(key);
        }

        currNode.requestHandlers.addAll(Arrays.stream(requestHandlers).toList());
    }

    void addGlobalRoute(String path, RequestHandler ...requestHandlers) {
        String[] parts = path.split("/");
        TrieNode currNode = root;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            currNode.children.putIfAbsent(part, new TrieNode());
            currNode = currNode.children.get(part);
        }

        currNode.globalRequestHandlers.addAll(Arrays.stream(requestHandlers).toList());
    }

    void handleRequest(String path, Request request, Response response) throws PathNotFoundException, HttpException {
        String[] parts = path.split("/");
        HashMap<String, String> params = new HashMap<>();

        TrieNode currNode = root;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            // 현재 경로에 global server.request handler가 있으면
            for (RequestHandler requestHandler : currNode.globalRequestHandlers) {
                NextHandler nextHandler = new NextHandler();

                requestHandler.execute(request, response, nextHandler);

                if (!nextHandler.isCalled()) {
                    return;
                }
            }

            // 지정된 경로가 있으면
            if (currNode.children.containsKey(part)) {
                currNode = currNode.children.get(part);
                continue;
            }

            // path parameter 이면
            if (currNode.children.containsKey(":")) {
                params.put(currNode.paramKey, part);
                currNode = currNode.children.get(":");
                continue;
            }

            throw new PathNotFoundException("Cannot find path");
        }

        if (currNode.requestHandlers.isEmpty()) {
            throw new PathNotFoundException("Cannot find path.");
        }

        request.setParams(params);

        for (RequestHandler requestHandler : currNode.requestHandlers) {
            NextHandler nextHandler = new NextHandler();

            requestHandler.execute(request, response, nextHandler);

            if (nextHandler.isCalled()) {
                continue;
            }

            break;
        }
    }
}
