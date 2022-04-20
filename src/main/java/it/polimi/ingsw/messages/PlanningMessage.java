package it.polimi.ingsw.messages;

import it.polimi.ingsw.enums.PlanningEnum;

public class PlanningMessage implements Message {

    private final PlanningEnum type;
    private final int value;

    public PlanningMessage(PlanningEnum type, int value) {
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
