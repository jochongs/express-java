package request;

import java.util.HashMap;

public class Request {
    private final RequestHeader header;
    private final String path;
    private final String method;
    private final String query;
    private final String body;
    private final String protocol;
    private final String protocolVersion;
    private HashMap<String, String> params;

    public Request(RawRequest rawRequest) {
        // Header
        header = new RequestHeader(rawRequest);

        // Body
        body = rawRequest.rawBody;

        // ETC...
        String[] splitMethods = rawRequest.rawMethod.split(" ");
        method = splitMethods[0];

        String pullPath = splitMethods[1];

        path = pullPath.split("\\?")[0];

        if (pullPath.split("\\?").length >= 2) {
            query = pullPath.split("\\?")[1];
        } else {
            query = "";
        }

        String protocolWithVersion = splitMethods[2];
        protocol = protocolWithVersion.split("/")[0];
        protocolVersion = protocolWithVersion.split("/")[1];
    }

    public void printHeader() {
        System.out.println(header);
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return method + " " + path + "?" + query + " " + protocol + '/' + protocolVersion + '\n' +
                header.toString() + '\n' +
                body;
    }
}
