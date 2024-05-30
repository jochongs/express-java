package server;

import exception.HttpException;
import request.RawRequest;
import request.Request;
import response.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class Server {
    private ServerSocket serverSocket;
    private final HashMap<HttpMethod, Router> routers;
    private ExceptionHandler exceptionHandler;

    public Server() {
        routers = new HashMap<>();
    }

    public void listen(int port) {
        initSocket(port);

        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();

                Request request = createRequest(socket);
                Response response = new Response(socket);

                if (request == null) {
                    response.send("No content");
                    socket.close();
                    continue;
                }

                Router router = getRouter(request.method());

                try {
                    router.handleRequest(request.path(), request, response);
                } catch (PathNotFoundException exception) {
                    response.status(404).send("Cannot find path");
                } catch (HttpException exception) {
                    if (exceptionHandler == null) {
                        System.out.println("Cannot find Error Exception Middleware");
                        System.exit(1);
                    }

                    exceptionHandler.handleException(exception, request, response);
                }

                // Close the socket
                closeSocket(socket);
            } catch (IOException exception) {
                closeSocket(socket);
            }
        }
    }

    public void use(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void get(String path, RequestHandler ...requestHandlers) {
        addRoutes(HttpMethod.GET, path, requestHandlers);
    }

    public void post(String path, RequestHandler ...requestHandlers) {
        addRoutes(HttpMethod.POST, path, requestHandlers);
    }

    public void put(String path, RequestHandler ...requestHandlers) {
        addRoutes(HttpMethod.PUT, path, requestHandlers);
    }

    public void delete(String path, RequestHandler ...requestHandlers) {
        addRoutes(HttpMethod.DELETE, path, requestHandlers);
    }

    public void patch(String path, RequestHandler ...requestHandlers) {
        addRoutes(HttpMethod.PATCH, path, requestHandlers);
    }

    private void closeSocket(Socket socket) {
        if (socket == null) {
            return;
        }

        int tryCount = 0;
        try {
            while (tryCount < 3) {
                try {
                    socket.close();
                    return;
                } catch (IOException exception) {
                    tryCount++;
                    Thread.sleep(1000);
                    continue;
                }
            }
        } catch (InterruptedException exception) {
            return;
        }
    }

    private Router getRouter(HttpMethod httpMethod) {
        if (!routers.containsKey(httpMethod)) {
            routers.put(httpMethod, new Router());
        }

        return routers.get(httpMethod);
    }

    private void addRoutes(HttpMethod method, String path, RequestHandler ...requestHandler) {
        Router router;

        if (!routers.containsKey(method)) {
            router = new Router();
            routers.put(method, router);
        } else {
            router = routers.get(method);
        }

        router.addRoute(path, requestHandler);
    }

    private Request createRequest(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            // Method Line
            String methodLine = bufferedReader.readLine();

            if (!isValidHttpLine(methodLine)) {
                return null;
            }

            // Header Line
            String headerLines = "";
            String line = bufferedReader.readLine();
            if (line != null) {
                while (!line.isEmpty()) {
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

            return new Request(
                    new RawRequest(
                            methodLine,
                            headerLines,
                            bodyBuilder.toString()
                    )
            );
        } catch (IOException exception) {
            exception.printStackTrace();

            return null;
        }
    }

    private boolean isValidHttpLine(String methodLine) {
        if (methodLine == null) {
            return false;
        }

        if (methodLine.isEmpty()) {
            return false;
        }

        String[] parts = methodLine.split(" ");

        if (parts.length != 3) {
            return false;
        }

        String[] methods = new String[]{"GET", "POST", "PUT", "DELETE", "PATCH", "OPTION", "HEAD", "TRACE", "CONNECT"};

        if (!Arrays.stream(methods).toList().contains(parts[0])) {
            return false;
        }

        return true;
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


