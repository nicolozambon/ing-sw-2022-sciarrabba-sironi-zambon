package it.polimi.ingsw.events;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ThinModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Event fired by an AnswerListenable to AnswerListeners
 * @see it.polimi.ingsw.listeners.AnswerListener
 * @see it.polimi.ingsw.listenables.AnswerListenable
 */
public class AnswerEvent {

    /**
     * Property name of the event
     */
    private final String propertyName;

    /**
     * String to be sent with the event
     */
    private final String message;

    /**
     * List of String sent with the event
     */
    private final List<String> options;

    /**
     * Number sent with the event
     */
    private final int num;

    /**
     * ThinModel sent with the event
     */
    private final ThinModel model;


    /**
     * Constructor for an event without any attribute
     * @param propertyName name of the property that has changed
     */
    public AnswerEvent(String propertyName) {
        this.propertyName = propertyName;
        this.message = null;
        this.options = null;
        this.model = null;
        this.num = -1;
    }

    /**
     * Constructor for an event with a message as attribute
     * @param propertyName name of the property that has changed
     * @param message message to be sent with the event
     */
    public AnswerEvent(String propertyName, String message) {
        this.propertyName = propertyName;
        this.message = message;
        this.options = null;
        this.model = null;
        this.num = -1;
    }

    /**
     * Constructor for an event with Model as attribute, the Model is stripped down to a ThinModel
     * @param propertyName name of the property that has changed
     * @param model Model to be sent with the event
     * @see Model
     * @see ThinModel
     */
    public AnswerEvent(String propertyName, Model model) {
        this.propertyName = propertyName;
        this.message = null;
        this.options = null;
        this.model = new ThinModel(model);
        this.num = -1;
    }

    /**
     * Constructor for an event with a List of String as attribute
     * @param propertyName name of the property that has changed
     * @param options List of String to be sent with the event
     */
    public AnswerEvent(String propertyName, List<String> options) {
        this.propertyName = propertyName;
        this.message = null;
        this.options = new ArrayList<>(options);
        this.model = null;
        this.num = -1;
    }

    /**
     * Constructor for an event with a number as attribute
     * @param propertyName name of the property that has changed
     * @param num number to be sent with the event
     */
    public AnswerEvent(String propertyName, int num) {
        this.propertyName = propertyName;
        this.message = null;
        this.options = null;
        this.model = null;
        this.num = num;
    }

    /**
     * Returns the property name of the event
     * @return the property name of the event
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the message sent with the event
     * @return the message sent with the event
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the List of String sent with the event
     * @return the List of String sent with the event
     */
    public List<String> getOptions() {
        if (options != null) return new ArrayList<>(options);
        return null;
    }

    /**
     * Returns the number sent with the event
     * @return the number sent with the event
     */
    public int getNum() {
        return num;
    }

    /**
     * Returns the ThinModel sent with the event
     * @return the ThinModel sent with the event
     */
    public ThinModel getModel() {
        return model;
    }
}
