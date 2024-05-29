import server.Server;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.get("/", (request, response) -> {
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

        server.post("/user", ((request, response) -> {
            response.status(201).send("signup success");
        }));

        server.get("/user/:idx", (request, response) -> {
            HashMap<String, String> params = request.params();

            System.out.println(params);

            response.status(200).send("success");
        } );

        server.listen(80);
    }
}
