package com.httpClient;

public class ResponseDTO {

    private String status;
    private String body;

    // Constructor
    public ResponseDTO(String status, String body) {
        this.status = status;
        this.body = body;
    }

    // Getters y Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "status='" + status + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
