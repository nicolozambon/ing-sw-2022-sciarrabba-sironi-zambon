package it.polimi.ingsw.messages;

import com.google.gson.Gson;

import java.io.Serializable;

public class Message implements Serializable {

    private final String name;
    private final String value;
    private final static Gson gson = new Gson();

    public Message(String name, Object value) {
        this.name = name;
        this.value = gson.toJson(value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return this.value;
    }
}
