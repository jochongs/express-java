package server.pipe;

import server.exception.BadRequestException;
import server.exception.HttpException;

import java.util.HashMap;

public class ParseIntPipe implements Pipe<Integer> {
    @Override
    public Integer run(HashMap<String, String> params, String key) throws HttpException {
        if (!params.containsKey(key)) {
            throw new BadRequestException("Cannot find path parameter, key: " + key);
        }

        String data = params.get(key);

        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException exception) {
            throw new BadRequestException("Cannot find path parameter, key: " + key);
        }
    }
}
