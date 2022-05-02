package it.polimi.ingsw.messages.requests;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;

public interface Request extends Serializable {
    PropertyChangeEvent process(Object eventSource);
}