package it.polimi.ingsw.listenables;

import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listeners.RequestListener;

public interface RequestListenableInterface {
    void addRequestListener(RequestListener requestListener);
    void removeRequestListener(RequestListener requestListener);
    void fireRequest(RequestEvent requestEvent) throws Exception;
}
