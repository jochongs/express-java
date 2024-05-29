package server;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
    public Map<String, TrieNode> children = new HashMap<>();
    public RequestHandler requestHandler;
    String paramKey;
}
