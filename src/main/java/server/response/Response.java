package server.response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Response {
    private HttpStatus statusCode = HttpStatus.OK;
    private String contentType;
    private String body;
    private final Socket socket;

    public Response(Socket socket) {
        this.socket = socket;
    }

    public void send(String data) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            body = data;
            contentType = "text/plain";

            byte[] bytes = this.toString().getBytes("UTF-8");

            outputStream.write(bytes);
            outputStream.flush();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public Response status(int statusCode) {
        this.statusCode = HttpStatus.fromCode(statusCode);
        return this;
    }

    @Override
    public String toString() {
        if (body == null) {
            return String.format(
                    "HTTP/1.1 %d %s\r\n" +
                    statusCode.getCode(), statusCode.getMessage()
            );
        }

        return String.format(
                """
                        HTTP/1.1 %d %s\r
                        Content-Type: %s\r
                        \r
                        %s""",
                statusCode.getCode(), statusCode.getMessage(), contentType, body
        );
    }
}
