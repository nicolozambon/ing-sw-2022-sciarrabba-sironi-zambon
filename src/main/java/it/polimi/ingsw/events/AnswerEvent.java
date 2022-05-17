package it.polimi.ingsw.events;

import it.polimi.ingsw.model.Model;

import java.util.ArrayList;
import java.util.List;

public class AnswerEvent {

    private final String propertyName;
    private final String message;
    private final List<String> options;
    private final int num;
    private final Model model;


    public AnswerEvent(String propertyName) {
        this.propertyName = propertyName;
        this.message = null;
        this.options = null;
        this.model = null;
        this.num = -1;
    }

    public AnswerEvent(String propertyName, String message) {
        this.propertyName = propertyName;
        this.message = message;
        this.options = null;
        this.model = null;
        this.num = -1;
    }

    public AnswerEvent(String propertyName, Model model) {
        this.propertyName = propertyName;
        this.message = null;
        this.options = null;
        this.model = model;
        this.num = -1;
    }

    public AnswerEvent(String propertyName, List<String> options) {
        this.propertyName = propertyName;
        this.message = null;
        this.options = new ArrayList<>(options);
        this.model = null;
        this.num = -1;
    }

    public AnswerEvent(String propertyName, int num) {
        this.propertyName = propertyName;
        this.message = null;
        this.options = null;
        this.model = null;
        this.num = num;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getOptions() {
        if (options != null) return new ArrayList<>(options);
        return null;
    }

    public int getNum() {
        return num;
    }

    public Model getModel() {
        return model;
    }
}
