package it.polimi.ingsw.server;

import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.NotPlayerTurnException;
import it.polimi.ingsw.listenables.RequestListenable;

import java.lang.reflect.InvocationTargetException;

public class VirtualView extends RequestListenable {

    public VirtualView() {
        super();
    }

    @Override
    public void fireRequest(RequestEvent requestEvent) throws NoSuchMethodException, IllegalAccessException {
        try {
            super.fireRequest(requestEvent);
        } catch (InvocationTargetException e) {
            Throwable x = e.getCause();
            e.printStackTrace();
        }

    }
}
