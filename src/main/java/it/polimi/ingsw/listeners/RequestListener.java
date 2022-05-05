package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.RequestEvent;

import java.lang.reflect.InvocationTargetException;

public interface RequestListener {
    void onRequestEvent(RequestEvent requestEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
}
