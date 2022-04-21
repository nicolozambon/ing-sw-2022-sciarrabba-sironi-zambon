package it.polimi.ingsw.client;

import java.beans.PropertyChangeListener;
import java.io.IOException;

public interface Client extends PropertyChangeListener {
    void read();
    void startClient() throws IOException;
    void stopClient();
}
