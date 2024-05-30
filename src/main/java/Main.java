import com.fasterxml.jackson.databind.ObjectMapper;
import server.exception.BadRequestException;
import server.Server;

import java.io.IOException;
import java.util.HashMap;

class User {
    int idx;
    String name;

    @Override
    public String toString() {
        return "idx: " + this.idx + '\n' +
                "name: " + this.name;
    }
}

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.use("/user", (req, res, nextHandler) -> {
            if (req.header().contentType.equals("application/json")) {
                System.out.println("try to parsing");
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    User user = objectMapper.readValue(req.body(), User.class);

                    System.out.println(user);
                } catch (IOException exception) {
                    throw new BadRequestException("Cannot parsing json");
                }
            }

            nextHandler.next();
        });

        server.get("/", (request, response, nextHandler) -> {
            String body = request.body();
            String query = request.query();

            response.status(201).send("success");
        });

        server.post("/user", ((request, response, nextHandler) -> {
            response.status(201).send("signup success");
        }));

        server.get("/user/:idx", (request, response, nextHandler) -> {
            HashMap<String, String> params = request.params();

            System.out.println(params);

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
