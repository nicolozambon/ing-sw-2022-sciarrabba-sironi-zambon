package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.client.Client;

import java.beans.PropertyChangeEvent;

public class UpdateAnswer implements Answer {

    private final String propertyName;
    private final Object changes;

    public UpdateAnswer(String propertyName, Object changes) {
        this.propertyName = propertyName;
        this.changes = changes;
    }

    @Override
    public PropertyChangeEvent process(Object eventSource) {
        return new PropertyChangeEvent(eventSource, propertyName, null, changes);
    }
}
