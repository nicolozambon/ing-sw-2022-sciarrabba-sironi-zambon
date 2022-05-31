package it.polimi.ingsw.exceptions;

public class WinnerException extends Exception {
    private final int playerId;

    public WinnerException(int playerId) {
        super();
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
