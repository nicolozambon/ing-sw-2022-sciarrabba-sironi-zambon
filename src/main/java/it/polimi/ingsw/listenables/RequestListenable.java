package it.polimi.ingsw.listenables;

import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listeners.RequestListener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class RequestListenable implements RequestListenableInterface{

    private final List<RequestListener> listeners;

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
            requestListener.requestPerformed(requestEvent);
        }
    }

}
