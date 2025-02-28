package com.ok.samples.model;

public class CustomMessage {
    private String content;
    private int code;

    public CustomMessage(String content, int code) {
        this.content = content;
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public int getCode() {
        return code;
    }
}
