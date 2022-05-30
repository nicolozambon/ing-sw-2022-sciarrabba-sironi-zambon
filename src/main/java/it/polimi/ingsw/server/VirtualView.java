package it.polimi.ingsw.server;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;

import java.lang.reflect.InvocationTargetException;

public class VirtualView implements RequestListener, RequestListenableInterface, AnswerListener {

    private final GameHandler gameHandler;
    private final RequestListenable requestListenable;

    public VirtualView(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.requestListenable = new RequestListenable();
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
    public void fireRequest(RequestEvent requestEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        this.requestListenable.fireRequest(requestEvent);
    }

    @Override
    public synchronized void onRequestEvent(RequestEvent requestEvent) throws IllegalAccessException {
        try {
            fireRequest(requestEvent);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            gameHandler.launchAnswerEventPlayer(requestEvent.getPlayerId(), new AnswerEvent("error", e.getCause().getMessage()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            gameHandler.launchAnswerEventPlayer(requestEvent.getPlayerId(), new AnswerEvent("error", "Bad Request! Retry"));
        }

    }

    @Override
    public void onAnswerEvent(AnswerEvent answerEvent) {
        switch (answerEvent.getPropertyName()) {
            case "update" -> gameHandler.launchAnswerEventEveryone(answerEvent);
            case "options" -> gameHandler.launchAnswerEventCurrentPlayer(answerEvent);
            case "wait" -> gameHandler.launchAnswerEventPlayer(answerEvent.getNum(), new AnswerEvent(answerEvent.getPropertyName()));
        }
    }
}
