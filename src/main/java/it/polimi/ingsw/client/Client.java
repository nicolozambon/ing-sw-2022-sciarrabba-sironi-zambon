package it.polimi.ingsw.client;

import java.io.IOException;

public interface Client {
    void startClient() throws IOException;
    void stopClient();
}
