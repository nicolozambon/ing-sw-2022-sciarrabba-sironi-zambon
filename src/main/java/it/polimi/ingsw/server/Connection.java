package it.polimi.ingsw.server;


public interface Connection {
    void read();
    String getNickname();
    void startConnection();
    void stopConnection();
}
