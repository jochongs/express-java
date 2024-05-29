package server;

import request.Request;
import response.Response;
import java.util.HashMap;

class PathNotFoundException extends Exception {
    public PathNotFoundException(String message) {
        super(message);
    }
}

class Router {
    private final TrieNode root;

    Router() {
        root = new TrieNode();
    }

    void addRoute(String path, RequestHandler requestHandler) {
        String[] parts = path.split("/");
        TrieNode currNode = root;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            String key = part.startsWith(":") ? ":" : part;

            currNode.children.putIfAbsent(key, new TrieNode());
            currNode = currNode.children.get(key);
            currNode.paramKey = part.substring(1);
        }

        currNode.requestHandler = requestHandler;
    }

    void handleRequest(String path, Request request, Response response) throws PathNotFoundException {
        String[] parts = path.split(path);
        HashMap<String, String> params = new HashMap<>();

        TrieNode currNode = root;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
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

        request.setParams(params);
        currNode.requestHandler.execute(request, response);
    }
}

