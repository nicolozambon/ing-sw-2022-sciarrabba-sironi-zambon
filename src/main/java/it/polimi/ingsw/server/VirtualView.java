package it.polimi.ingsw.server;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.exceptions.NotPlayerTurnException;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;
import it.polimi.ingsw.model.Model;

import java.lang.reflect.InvocationTargetException;

public class VirtualView implements RequestListener, RequestListenableInterface, AnswerListener {

    private GameHandler gameHandler;
    private final RequestListenable requestListenable;

    public VirtualView() {
        this.requestListenable = new RequestListenable();
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public void addRequestListener(RequestListener requestListener) {
        this.requestListenable.addRequestListener(requestListener);
    }

    @Override
    public void removeRequestListener(RequestListener requestListener) {
        this.requestListenable.removeRequestListener(requestListener);
    }

    @Override
    public void fireRequest(RequestEvent requestEvent) throws Exception {
        this.requestListenable.fireRequest(requestEvent);
    }

    @Override
    public void requestPerformed(RequestEvent requestEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        this.requestListenable.fireRequest(requestEvent);
        gameHandler.launchOptionsAnswerEvent();
    }

    @Override
    public void answerPerformed(AnswerEvent answerEvent) {
        gameHandler.launchUpdateAnswerEvent(answerEvent);
    }
}
