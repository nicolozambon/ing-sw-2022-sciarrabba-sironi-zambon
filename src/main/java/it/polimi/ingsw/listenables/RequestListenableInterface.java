package it.polimi.ingsw.listenables;

import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listeners.RequestListener;

/**
 * Implementing this interface allows for an object to fire RequestEvent to RequestListeners
 * @see RequestListener
 * @see RequestEvent
 */
public interface RequestListenableInterface {

    /**
     * Add a RequestListener to the list
     * @param requestListener the RequestListener to be added
     */
    void addRequestListener(RequestListener requestListener);

    /**
     * Remove a RequestListener from the list
     * @param requestListener the RequestListener to be removed
     */
    void removeRequestListener(RequestListener requestListener);

    /**
     * Updates every listener with the RequestEvent
     * @param requestEvent the RequestEvent to be sent to every listener
     * @throws Exception if listener has problem to process the RequestEvent
     * @see RequestEvent
     */
    void fireRequest(RequestEvent requestEvent) throws Exception;
}
