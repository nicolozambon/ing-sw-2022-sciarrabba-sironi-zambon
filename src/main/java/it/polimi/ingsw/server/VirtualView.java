package it.polimi.ingsw.server;

import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.NotPlayerTurnException;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listeners.RequestListener;

import java.lang.reflect.InvocationTargetException;

public class VirtualView extends RequestListenable implements RequestListener {

    private GameHandler gameHandler;

    public VirtualView() {
        super();
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public void fireRequest(RequestEvent requestEvent) throws NoSuchMethodException, IllegalAccessException {
        try {
            super.fireRequest(requestEvent);
            this.gameHandler.launchUpdateAnswerEvent();
        } catch (InvocationTargetException e) {
            Throwable x = e.getCause();
            x.printStackTrace();
        }
    }

    @Override
    public void requestPerformed(RequestEvent requestEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        fireRequest(requestEvent);
    }
}
