package it.polimi.ingsw.client.view;

import it.polimi.ingsw.listeners.Listenable;
import it.polimi.ingsw.listeners.Listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Scanner;

public class ExampleView implements Listener, Listenable {

    private PropertyChangeSupport observable;
    private Scanner stdin;

    public ExampleView() {
        this.stdin = new Scanner(System.in);
        this.observable = new PropertyChangeSupport(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getNewValue());
        switch (evt.getPropertyName()) {
            case "NICKNAME" -> {
                String nickname = stdin.nextLine();
                this.observable.firePropertyChange("NICKNAME", null, nickname);
            }
            case "NUM_PLAYERS" -> {
                int num = stdin.nextInt();
                this.observable.firePropertyChange("NUM_PLAYERS", 0, num);
            }
            case "ERROR" -> {
            }
        }
        System.out.println("Wait...");
    }

    public void addListener(PropertyChangeListener listener) {
        this.observable.addPropertyChangeListener(listener);
    }

    public void addListener(String propertyName, PropertyChangeListener listener) {
        this.observable.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        this.observable.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {
        this.observable.addPropertyChangeListener(propertyName, listener);
    }
}
