import exception.NotFoundException;
import server.Server;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.get("/", (request, response, nextHandler) -> {
            String body = request.body();
            HashMap<String, String> params = request.params();
            String query = request.query();

            System.out.println("Body");
            System.out.println(body);
            System.out.println("Params");
            System.out.println(params);
            System.out.println("query");
            System.out.println(query);

            response.status(201).send("success");
        });

        server.post("/user", ((request, response, nextHandler) -> {
            response.status(201).send("signup success");
        }));

        server.get("/user/:idx", (request, response, nextHandler) -> {
            HashMap<String, String> params = request.params();

            System.out.println(params);

            if (true) {
                throw new NotFoundException("Cannot find user");
            }

            //nextHandler.next();
            response.status(200).send("success");
        }, (req, res, nextHandler) -> {
            res.send("미들웨어성공");
        });

        server.use(((exception, req, res) -> {
            System.out.println("예외 발생");

            res.status(exception.status).send(exception.message);
        }));

        server.listen(80);
    }
}
