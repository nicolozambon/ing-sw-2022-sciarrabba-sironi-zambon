package it.polimi.ingsw.listenables;

import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.listeners.AnswerListener;

import java.util.ArrayList;
import java.util.List;

public class AnswerListenable implements AnswerListenableInterface{

    private final List<AnswerListener> listeners;

    public AnswerListenable() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addAnswerListener(AnswerListener answerListener) {
        listeners.add(answerListener);
    }

    @Override
    public void removeAnswerListener(AnswerListener answerListener) {
        listeners.remove(answerListener);
    }

    @Override
    public void fireAnswer(AnswerEvent answerEvent) {
        for (AnswerListener answerListener : listeners) {
            answerListener.onAnswerEvent(answerEvent);
        }
    }

}