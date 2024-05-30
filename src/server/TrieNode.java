package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TrieNode {
    public Map<String, TrieNode> children = new HashMap<>();
    public List<RequestHandler> requestHandlers;
    String paramKey;
}
