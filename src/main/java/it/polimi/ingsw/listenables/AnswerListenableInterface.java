package it.polimi.ingsw.listenables;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.listeners.AnswerListener;

/**
 * Implementing this interface allows for an object to fire AnswerEvent to AnswerListener
 * @see AnswerListener
 * @see AnswerEvent
 */
public interface AnswerListenableInterface {

    /**
     * Add an AnswerListener to the list
     * @param answerListener the AnswerListener to be added
     */
    void addAnswerListener(AnswerListener answerListener);

    /**
     * Remove an AnswerListener from the list
     * @param answerListener the AnswerListener to be removed
     */
    void removeAnswerListener(AnswerListener answerListener);

    /**
     * Updates every listener with the AnswerEvent
     * @param answerEvent the AnswerEvent to be sent to every listener
     * @see AnswerEvent
     */
    void fireAnswer(AnswerEvent answerEvent);
}
