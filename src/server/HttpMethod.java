package server;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    OPTION("OPTION");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public static HttpMethod getMethod(String method) {
        if (method.equals("GET")) {
            return HttpMethod.GET;
        }
        if (method.equals("POST")) {
            return HttpMethod.POST;
        }
        if (method.equals("PUT")) {
            return HttpMethod.PUT;
        }
        if (method.equals("DELETE")) {
            return HttpMethod.DELETE;
        }
        if (method.equals("OPTION")) {
            return HttpMethod.OPTION;
        }

        throw new IllegalArgumentException("No matching method");
    }

    public String getMethod() {
        return method;
    }
}
