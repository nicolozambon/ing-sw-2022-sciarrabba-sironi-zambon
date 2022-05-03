package it.polimi.ingsw.events;

import java.io.Serializable;

public class AnswerEvent implements Serializable {

    private static final long serialVersionUID = 987654321L;

    private final String propertyName;
    private final Object value;


    public AnswerEvent(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getValue() {
        return value;
    }
}
