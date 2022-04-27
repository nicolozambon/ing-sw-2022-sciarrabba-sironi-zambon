package it.polimi.ingsw.listeners;

import java.beans.PropertyChangeListener;

public interface Listenable {
    void addListener(PropertyChangeListener listener);
    void addListener(String propertyName, PropertyChangeListener listener);
    void removeListener(PropertyChangeListener listener);
    void removeListener(String propertyName, PropertyChangeListener listener);
}
