package request;

import jdk.jfr.ContentType;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    String authorization;
    String cookie;
    String host;
    String origin;
    String contentType;
    String contentLength;

    private RequestHeader (
            String authorization,
            String cookie,
            String host,
            String origin,
            String contentType,
            String contentLength
    ) {
        this.authorization = authorization;
        this.cookie = cookie;
        this.host = host;
        this.origin = origin;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public RequestHeader(RawRequest rawRequest) {
        Map<String, String> headerMap = new HashMap<>();
    }
}
