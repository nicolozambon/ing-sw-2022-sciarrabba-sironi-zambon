package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.client.Client;

import java.beans.PropertyChangeEvent;

public class ErrorAnswer implements Answer {

    private final String message;

    public ErrorAnswer(String message) {
        this.message = message;
    }

    @Override
    public PropertyChangeEvent process(Object eventSource) {
        return new PropertyChangeEvent(eventSource, "error", null, message);
    }
}
