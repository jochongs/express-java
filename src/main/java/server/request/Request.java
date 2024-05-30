package server.request;

import server.HttpMethod;
import server.exception.BadRequestException;
import server.exception.HttpException;
import server.pipe.Pipe;

import java.util.HashMap;

public class Request {
    private final RequestHeader header;
    private final String path;
    private final HttpMethod method;
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

        method = HttpMethod.getMethod(splitMethods[0]);

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

    public RequestHeader header() {
        return header;
    }

    public String path() {
        return path;
    }

    public HttpMethod method() {
        return method;
    }

    public String query() {
        return query;
    }

    public String body() {
        return body;
    }

    public String protocol() {
        return protocol;
    }

    public String protocolVersion() {
        return protocolVersion;
    }

    public HashMap<String, String> params() {
        return params;
    }

    public String params(String key) {
        if (!params.containsKey(key)) {
            return null;
        }

        return params.get(key);
    }

    public <T> T params(String key, Class <? extends Pipe<T>> pipeClass) throws HttpException {
        try {
            Pipe<T> pipe = pipeClass.getDeclaredConstructor().newInstance();

            return pipe.run(params, key);
        } catch (Exception exception) {
            throw new BadRequestException("Cannot parse key: " + key);
        }
    }
}
