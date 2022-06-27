package it.polimi.ingsw.server;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;

import java.lang.reflect.InvocationTargetException;

/**
 * Representation of the views on the server side, send the correct AnswerEvent to each client
 */
public class VirtualView implements RequestListener, RequestListenableInterface, AnswerListener {

    /**
     * GameHandler to be used by VirtualView
     * @see GameHandler
     */
    private final GameHandler gameHandler;

    /**
     * RequestListenable that will handle all RequestListener
     * @see RequestListenable
     * @see RequestListener
     */
    private final RequestListenable requestListenable;

    /**
     * Constructor VirtualView, instantiate the VirtualView for the given GameHandler
     * @param gameHandler GameHandler to be linked to the VirtualView
     */
    protected VirtualView(GameHandler gameHandler) {
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

    /**
     * Forwards the received RequestEvent to the RequestListeners
     * @param requestEvent The RequestEvent received from the RequestListenable
     * @throws IllegalAccessException if the RequestListener cannot perform the request
     */
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

    /**
     * Sends the AnswerEvent to the correct client
     * @param answerEvent The AnswerEvent received from the AnswerListenable
     */
    @Override
    public void onAnswerEvent(AnswerEvent answerEvent) {
        switch (answerEvent.getPropertyName()) {
            case "options" -> gameHandler.launchAnswerEventCurrentPlayer(answerEvent);
            case "wait" -> gameHandler.launchAnswerEventPlayer(answerEvent.getNum(), new AnswerEvent(answerEvent.getPropertyName()));
            default -> gameHandler.launchAnswerEventEveryone(answerEvent);
        }
    }
}
