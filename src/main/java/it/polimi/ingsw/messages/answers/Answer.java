package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.client.Client;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;

public interface Answer extends Serializable {
    PropertyChangeEvent process(Object eventSource);
}
