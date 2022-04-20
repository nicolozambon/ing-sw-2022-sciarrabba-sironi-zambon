package it.polimi.ingsw.messages;

import it.polimi.ingsw.enums.LobbyEnum;

public class LobbyMessage implements Message {

    private final LobbyEnum type;
    private final int value;
    private final String nickname;

    public LobbyMessage(LobbyEnum type, int value) {
        this.type = type;
        this.value = value;
        this.nickname = null;
    }

    public LobbyMessage(LobbyEnum type, String nickname) {
        this.type = type;
        this.nickname = nickname;
        this.value = -1;
    }

    public String getType() {
        return type.toString();
    }

    public int getValue() {
        return value;
    }

    public String getNickname() {
        return nickname;
    }
}
