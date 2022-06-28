package it.polimi.ingsw.listenables;

import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listeners.RequestListener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the RequestListenableInterface
 */
public class RequestListenable implements RequestListenableInterface{

    /**
     * List of RequestListener subscribed
     */
    private final List<RequestListener> listeners;

    /**
     * Constructor, initialize listeners list
     */
    public RequestListenable() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addRequestListener(RequestListener requestListener) {
        listeners.add(requestListener);
    }

    @Override
    public void removeRequestListener(RequestListener requestListener) {
        listeners.remove(requestListener);
    }

    @Override
    public void fireRequest(RequestEvent requestEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException{
        for (RequestListener requestListener : listeners) {
            requestListener.onRequestEvent(requestEvent);
        }
    }

}
