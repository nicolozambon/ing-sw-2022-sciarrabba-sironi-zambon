package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.RequestEvent;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementing this interface allows for an object to listen to RequestEvent fired by RequestListenable
 * @see it.polimi.ingsw.listenables.RequestListenable
 * @see RequestEvent
 */
public interface RequestListener {

    /**
     * Performs action based on the RequestEvent
     * @param requestEvent RequestEvent received from RequestListenable
     */
    void onRequestEvent(RequestEvent requestEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
}
