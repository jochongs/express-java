#  Express.java

Socket class를 이용하여 Express를 구현하였습니다.

```java
import server.Server;

public static void main(String[] args) {
    Server server = new Server();

    server.post("/user", ((server.request, server.response, nextHandler) -> {
        server.response.status(201).send("signup success");
    }));

    server.listen(80);
}
```

Express와 비슷한 사용감을 가질 수 있도록 구현하였습니다. 