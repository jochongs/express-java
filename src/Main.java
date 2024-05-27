import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.listen(80);
    }

    static class Server {
        private ServerSocket serverSocket;

        void listen(int port) {
            initSocket(port);

            while(true) {
                try {
                    Socket socket = serverSocket.accept();

                    InputStream inputStream = socket.getInputStream();

                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    List<String> readLine = bufferedReader.lines().toList();

                    for(int i = 0; i < readLine.size(); i++) {
                        System.out.println(readLine.get(i));
                    }

                    send(socket, "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "\r\n" +
                            "Hello, this is the server response.");
                } catch(IOException exception) {
                    System.out.println("Fail to accept socket");

                    break;
                }
            }

            destroySocket();
        }

        private void send(Socket socket, String sendStr) {
            System.out.println("Server.send");
            try {
                OutputStream outputStream = socket.getOutputStream();

                byte[] bytes = sendStr.getBytes("UTF-8");

                outputStream.write(bytes);
                outputStream.flush();

                socket.close();
            } catch(IOException exception) {
                exception.printStackTrace();
                System.out.println("Fail to send");
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
            } catch(IOException exception) {
                System.out.println("Fail to destroy socket");

                System.exit(1);
            }
        }
    }
}
