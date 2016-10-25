package com.project.webapp.model;

import java.util.Objects;

/**
 * Model for interpreting the message sent from the server to the clients.
 * Created by LaMarinescu on 10/23/2016.
 */
public class SendMessage {

    private final String name;
    private final boolean hasStarted;
    private final boolean isOver;
    private final int[] pits;

    public SendMessage(final String name, final boolean hasStarted,
                       final boolean isOver, final int[] pits) {
        this.name = Objects.requireNonNull(name);
        this.hasStarted = Objects.requireNonNull(hasStarted);
        this.isOver = Objects.requireNonNull(isOver);
        this.pits = Objects.requireNonNull(pits);
    }

    public String getName() {
        return name;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public boolean isOver() {
        return isOver;
    }

    public int[] getPits() {
        return pits;
    }
}
