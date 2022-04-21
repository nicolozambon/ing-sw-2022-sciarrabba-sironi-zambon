package it.polimi.ingsw.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Scanner;

public class ExampleView implements PropertyChangeListener {

    private PropertyChangeSupport observable;
    private Scanner stdin;

    public ExampleView() {
        this.stdin = new Scanner(System.in);
        this.observable = new PropertyChangeSupport(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "NICKNAME" -> {
                System.out.println("Choose a nickname:");
                String nickname = stdin.nextLine();
                this.observable.firePropertyChange("NICKNAME", null, nickname);
            }
            case "NUM_PLAYERS" -> {
                System.out.println("You are the first player, How many player would you like to play with (2 -3):");
                int num = stdin.nextInt();
                this.observable.firePropertyChange("NUM_PLAYERS", 0, num);
            }
            case "ERROR" -> {
                System.out.println((String) evt.getNewValue());
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
}
