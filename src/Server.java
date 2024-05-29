import request.RawRequest;
import request.Request;
import request.RequestHeader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    void listen(int port) {
        initSocket(port);

        while(true) {
            try {
                // Waiting for connecting
                System.out.println("==================================");
                Socket socket = serverSocket.accept();

                // Input Process
                RawRequest rawRequest = createRequest(socket);
                System.out.println(rawRequest);
                RequestHeader requestHeader = new RequestHeader(rawRequest);

                // Output Process
                send(socket, "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "\r\n" +
                        "Hello, World!");

                // Close the socket
                socket.close();
            } catch(IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void send(Socket socket, String sendStr) {
        try {
            OutputStream outputStream = socket.getOutputStream();

            byte[] bytes = sendStr.getBytes("UTF-8");

            outputStream.write(bytes);
            outputStream.flush();
        } catch(IOException exception) {
            exception.printStackTrace();
            System.out.println("Fail to send");
        }
    }

    private RawRequest createRequest(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            // Method Line
            String methodLine = bufferedReader.readLine();

            // Header Line
            String headerLines = "";
            String line = bufferedReader.readLine();
            if (line != null) {
                while(!line.isEmpty()) {
                    headerLines = headerLines + line + "\n";
                    line = bufferedReader.readLine();
                }
                headerLines = headerLines.substring(0, headerLines.length() - 1);
            }

            // Body Line
            StringBuilder bodyBuilder = new StringBuilder();
            while (bufferedReader.ready()) {
                bodyBuilder.append((char) bufferedReader.read());
            }

            return new RawRequest(
                    methodLine,
                    headerLines,
                    bodyBuilder.toString()
            );
        } catch(IOException exception) {
            exception.printStackTrace();

            return null;
        }
    }

    private void initSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException exception) {
            System.out.println("Fail to create socket");

            System.exit(1);
        }
    }

    private void destroySocket() {
        try {
            if (serverSocket.isBound()) {
                serverSocket.close();
            }

            serverSocket = null;
        } catch(IOException exception) {
            System.out.println("Fail to destroy socket");

            System.exit(1);
        }
    }
}
