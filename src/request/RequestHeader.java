package request;

import jdk.jfr.ContentType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    String authorization;
    String cookie;
    String host;
    String origin;
    String contentType;
    String contentLength;
    String userAgent;
    String accept;

    private RequestHeader (
            String authorization,
            String cookie,
            String host,
            String origin,
            String contentType,
            String contentLength,
            String userAgent,
            String accept
    ) {
        this.authorization = authorization;
        this.cookie = cookie;
        this.host = host;
        this.origin = origin;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.userAgent = userAgent;
        this.accept = accept;
    }

    public RequestHeader(RawRequest rawRequest) {
        String rawHeader = rawRequest.rawHeader;

        if (rawHeader.isEmpty()) {
            return;
        }

        String[] rawHeaderLines = rawHeader.split("\n");

        for (int i = 0; i < rawHeaderLines.length; i++) {
            String headerLine = rawHeaderLines[i];

            String[] splitHeaders = headerLine.split(": ");

            String key = splitHeaders[0];
            String value = splitHeaders[1];

            if (key.equals("Host")) {
                this.host = value;
                continue;
            }

            if (key.equals("User-Agent")) {
                this.userAgent = value;
                continue;
            }

            if (key.equals("Accept")) {
                this.accept = value;
                continue;
            }

            if (key.equals("Content-Type")) {
                this.contentType = value;
                continue;
            }

            if (key.equals("Content-Length")) {
                this.contentLength = value;
                continue;
            }
        }
    }

    @Override
    public String toString() {
        return "RequestHeader{" + '\n' +
                 "\t" + "authorization=" + authorization + "," + '\n' +
                 "\t" + "cookie=" + cookie + "," + '\n' +
                 "\t" + "host=" + host + "," + '\n' +
                 "\t" + "origin=" + origin + "," + '\n' +
                 "\t" + "contentType=" + contentType + "," + '\n' +
                 "\t" + "contentLength=" + contentLength + "," + '\n' +
                 "\t" + "userAgent=" + userAgent + "," + '\n' +
                 "\t" + "accept=" + accept + '\n' +
                '}';
    }
}
