import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(80);

            Socket socket = serverSocket.accept();

            System.out.println("socket = " + socket);
        } catch (IOException exception) {
            System.out.println("exception = " + exception);
        }
    }
}
