package it.polimi.ingsw.messages.requests;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

public class CommandRequest implements Request {

    private final String propertyName;
    private final Object values;

    public CommandRequest(String propertyName, Object values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    @Override
    public PropertyChangeEvent process(Object eventSource) {
        return new PropertyChangeEvent(eventSource, propertyName, null, values);
    }
}
