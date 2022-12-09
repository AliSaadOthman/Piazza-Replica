package org.piazza.utils;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class BackDoorResponse {
    private String message;
    private HttpStatus httpStatus;

    public BackDoorResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HashMap<String, Object> constructResponse() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", httpStatus);
        return  response;
    }
}
