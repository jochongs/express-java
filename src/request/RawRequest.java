package request;

public class RawRequest {
    String rawMethod;
    String rawHeader;
    String rawBody;

    public RawRequest(
            String rawMethod,
            String rawHeader,
            String rawBody
    ) {
        this.rawMethod = rawMethod;
        this.rawHeader = rawHeader;
        this.rawBody = rawBody;
    }

    @Override
    public String toString() {
        if (rawHeader.isEmpty()) {
            return "null";
        }

        if (rawBody.isEmpty()) {
            return rawMethod + '\n' +
                    rawHeader;
        }

        return rawMethod + '\n' +
                rawHeader + "\n\n" +
                rawBody;
    }
}
