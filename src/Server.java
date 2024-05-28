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
                String inputStr = readOutputStream(socket);

                System.out.println(inputStr);

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

    private String readOutputStream(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String methodLine = bufferedReader.readLine();
            System.out.println(methodLine);

            String headerLines = bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while(!line.isEmpty()) {
                headerLines = headerLines + line + "\n";
                line = bufferedReader.readLine();
            }

            bufferedReader.readLine();

            return headerLines.substring(0, headerLines.length() - 1);
        } catch(IOException exception) {
            exception.printStackTrace();
        }

        return "";
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
