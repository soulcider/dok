package com.dok.common.http;

public interface Request {

    public static enum Method {
        GET, POST, PUT, DELETE
    }


    public int execute();
    public String getResponseBody();

}
