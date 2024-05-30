package server;

import request.RawRequest;
import request.Request;
import response.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Pattern;

class InvalidHttpRequestException extends Exception {
    InvalidHttpRequestException(String message) {
        super(message);
    }
}

public class Server {
    private ServerSocket serverSocket;
    private final HashMap<HttpMethod, Router> routers;

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
                    continue;
                }
                if (request.method() == null) {
                    response.send("No content");
                    continue;
                }

                Router router = getRouter(request.method());

                try {
                    router.handleRequest(request.path(), request, response);
                } catch (PathNotFoundException exception) {
                    response.status(404).send("Cannot find path");
                }

                // Close the socket
                closeSocket(socket);
            } catch (InvalidHttpRequestException exception) {
                closeSocket(socket);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void get(String path, RequestHandler requestHandler) {
        addRoutes(HttpMethod.GET, path, requestHandler);
    }

    public void post(String path, RequestHandler requestHandler) {
        addRoutes(HttpMethod.POST, path, requestHandler);
    }

    public void put(String path, RequestHandler requestHandler) {
        addRoutes(HttpMethod.PUT, path, requestHandler);
    }

    public void delete(String path, RequestHandler requestHandler) {
        addRoutes(HttpMethod.DELETE, path, requestHandler);
    }

    public void patch(String path, RequestHandler requestHandler) {
        addRoutes(HttpMethod.PATCH, path, requestHandler);
    }

    private void closeSocket(Socket socket) {
        if (socket == null) {
            return;
        }

        int tryCount = 0;
        try {
            while (tryCount < 3) {
                wait(1000);
                try {
                    socket.close();
                } catch (IOException exception) {
                    tryCount++;
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

    private void addRoutes(HttpMethod method, String path, RequestHandler requestHandler) {
        Router router;

        if (!routers.containsKey(method)) {
            router = new Router();
            routers.put(method, router);
        } else {
            router = routers.get(method);
        }

        router.addRoute(path, requestHandler);
    }

    private Request createRequest(Socket socket) throws InvalidHttpRequestException {
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            // Method Line
            String methodLine = bufferedReader.readLine();

            if (isValidHttpLine(methodLine)) {
                throw new InvalidHttpRequest("Invalid Http Request");
            }

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

            return new Request(
                    new RawRequest(
                            methodLine,
                            headerLines,
                            bodyBuilder.toString()
                    )
            );
        } catch(IOException exception) {
            exception.printStackTrace();

            return null;
        }
    }

    private boolean isValidHttpLine(String methodLine) {
        Pattern METHOD_LINE_PATTERN = Pattern.compile("^(GET|POST|PUT|DELETE|HEAD|OPTIONS|PATCH|TRACE|CONNECT)\\\\s+\\\\S+\\\\s+HTTP/\\\\d\\\\.\\\\d$");

        if (methodLine == null) {
            return false;
        }

        if (methodLine.isEmpty()) {
            return false;
        }

        return METHOD_LINE_PATTERN.matcher(methodLine).matches();
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


