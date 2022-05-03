package it.polimi.ingsw.listenables;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.listeners.AnswerListener;
import java.util.ArrayList;
import java.util.List;

public abstract class AnswerListenable {

    private final List<AnswerListener> listeners;

    public AnswerListenable() {
        this.listeners = new ArrayList<>();
    }

    public void addAnswerListener(AnswerListener answerListener) {
        listeners.add(answerListener);
    }

    public void removeAnswerListener(AnswerListener answerListener) {
        listeners.remove(answerListener);
    }

    public void fireAnswer(AnswerEvent answerEvent) {
        for (AnswerListener answerListener : listeners) {
            answerListener.answerPerformed(answerEvent);
        }
    }

}