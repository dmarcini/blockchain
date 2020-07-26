package com.dmarcini.app.blockchain;

public class Data {
    private long id;
    private final String client;
    private final String message;

    public Data(String client, String message) {
        this.client = client;
        this.message = message;
    }

    public String getClient() {
        return client;
    }

    public String getMessage() {
        return message;
    }
}
