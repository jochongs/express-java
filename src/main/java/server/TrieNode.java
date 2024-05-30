package server;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

class TrieNode {
    public Map<String, TrieNode> children = new HashMap<>();
    public ArrayList<RequestHandler> requestHandlers = new ArrayList<>();
    public ArrayList<RequestHandler> globalRequestHandlers = new ArrayList<>();
    String paramKey;
}
