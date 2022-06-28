package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.AnswerEvent;

/**
 * Implementing this interface allows for an object to listen to AnswerEvent fired by AnswerListenable
 * @see it.polimi.ingsw.listenables.AnswerListenable
 * @see AnswerEvent
 */
public interface AnswerListener {

    /**
     * Performs action based on the AnswerEvent
     * @param answerEvent AnswerEvent received from AnswerListenable
     */
    void onAnswerEvent(AnswerEvent answerEvent);
}
