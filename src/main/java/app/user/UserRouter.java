package app.user;

import app.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.exception.NotFoundException;
import server.pipe.ParseIntPipe;

import java.util.ArrayList;
import java.util.HashMap;

public class UserRouter {
    static private int lastId = 1;
    static private final HashMap<Integer, User> users = new HashMap<Integer, User>();

    static public void register() {
        App.server.get("/user/all", (req, res, nextHandler) -> {
            ArrayList<User> userList = new ArrayList<>();

            for (int key : users.keySet()) {
                userList.add(users.get(key));
            }

            res.status(200).send(userList);
        });

        App.server.get("/user/:idx", (req, res, nextHandler) -> {
            int userIdx = req.params("idx", ParseIntPipe.class);

            if(!users.containsKey(userIdx)) {
                throw new NotFoundException("Cannot find user : " + userIdx);
            }

            res.status(200).send(users.get(userIdx));
        });

        App.server.post("/user", (req, res, next) -> {
            User user = User.of(req.body());
            user.idx = lastId;
            users.put(lastId, user);

            lastId++;

            res.status(200).send("sign up success");
        });
    }
}
