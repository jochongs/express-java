package server.pipe;

import server.exception.BadRequestException;
import server.exception.HttpException;

import java.util.HashMap;


public interface Pipe <T>{
    public T run(HashMap<String, String> params, String key) throws HttpException;
}