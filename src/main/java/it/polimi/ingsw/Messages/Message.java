package it.polimi.ingsw.Messages;

import java.io.Serializable;

public class Message implements Serializable {

    private final String name;
    private final Object value;

    public Message(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
