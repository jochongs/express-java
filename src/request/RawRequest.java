package request;

public class RawRequest {
    String rawMethod = "";
    String rawHeader = "";
    String rawBody = "";

    RawRequest(
            String rawMethod,
            String rawHeader,
            String rawBody
    ) {
        this.rawMethod = rawMethod;
        this.rawHeader = rawHeader;
        this.rawBody = rawBody;
    }
}
