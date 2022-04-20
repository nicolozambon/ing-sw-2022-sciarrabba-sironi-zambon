package it.polimi.ingsw.messages;

import it.polimi.ingsw.enums.ActionEnum;

public class ActionMessage implements Message {

    private final ActionEnum type;
    private final int value;

    public ActionMessage(ActionEnum type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type.toString();
    }

    public int getValue() {
        return value;
    }
}
