package app.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import server.exception.BadRequestException;
import server.exception.HttpException;

public class User {
    public int idx;
    public String name;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toString() {
        return "idx: " + this.idx + '\n' +
                "name: " + this.name;
    }

    private User signUpValidate() throws HttpException {
        if (name.length() <= 2 || name.length() > 8) {
            throw new BadRequestException("Invalid name");
        }

        return this;
    }

    public static User of(String json) throws HttpException {
        try {;
            return objectMapper.readValue(json, User.class);
        } catch (Exception exception) {
            throw new BadRequestException("Invalid body");
        }
    }
}
