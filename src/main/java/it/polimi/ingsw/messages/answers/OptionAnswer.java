package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.client.Client;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionAnswer implements Answer {

    private final List<String> options;

    public OptionAnswer(String ... options) {
        this.options = Arrays.stream(options).toList();
    }

    @Override
    public PropertyChangeEvent process(Object eventSource) {
        return new PropertyChangeEvent(eventSource, "options", null, new ArrayList<>(options));
    }
}
