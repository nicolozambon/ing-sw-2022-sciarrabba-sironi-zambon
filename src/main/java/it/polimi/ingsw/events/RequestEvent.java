package it.polimi.ingsw.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestEvent implements Serializable {

    private static final long serialVersionUID = 987654321L;

    private final String propertyName;
    private final int playerId;
    private final int[] values;
    private final String string;


    public RequestEvent(String propertyName, int playerId, int ... values) {
        this.propertyName = propertyName;
        this.playerId = playerId;
        this.values = values;
        this.string = null;
    }

    public RequestEvent(String propertyName, int playerId, String string) {
        this.propertyName = propertyName;
        this.playerId = playerId;
        this.values = null;
        this.string = string;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int[] getValues() {
        return values;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getString() {
        return string;
    }
}
