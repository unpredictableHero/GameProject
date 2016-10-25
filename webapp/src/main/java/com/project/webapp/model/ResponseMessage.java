package com.project.webapp.model;

import java.util.Objects;

/**
 * Model for response message received by the server from the clients.
 * Created by LaMarinescu on 10/23/2016.
 */
public class ResponseMessage {

    private final String name;
    private final String id;
    private final String value;

    public ResponseMessage(String name, String id, String value) {
        this.name = Objects.requireNonNull(name);
        this.id = Objects.requireNonNull(id);
        this.value = Objects.requireNonNull(value);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
