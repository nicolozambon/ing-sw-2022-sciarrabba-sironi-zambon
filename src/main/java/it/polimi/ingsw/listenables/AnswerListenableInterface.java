package it.polimi.ingsw.listenables;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.listeners.AnswerListener;

public interface AnswerListenableInterface {
    void addAnswerListener(AnswerListener answerListener);
    void removeAnswerListener(AnswerListener answerListener);
    void fireAnswer(AnswerEvent answerEvent);
}
