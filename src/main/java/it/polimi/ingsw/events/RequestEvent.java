package it.polimi.ingsw.events;

/**
 * Event fired by the clients to the RequestListener, used to perform various action on the Model
 * @see it.polimi.ingsw.listeners.RequestListener
 */
public class RequestEvent {

    /**
     * Property name of the event
     */
    private final String propertyName;

    /**
     * Client's id that sent the event
     */
    private final int playerId;

    /**
     * Array of int sent with the event
     */
    private final int[] values;

    /**
     * String sent with event
     */
    private final String string;


    /**
     * Constructor for an event with variables numbers of int parameters
     * @param propertyName name of the action to be performed
     * @param playerId client's id
     * @param values various numbers to be sent with the event
     */
    public RequestEvent(String propertyName, int playerId, int ... values) {
        this.propertyName = propertyName;
        this.playerId = playerId;
        this.values = values;
        this.string = null;
    }

    /**
     * Constructor for an event with a String as parameters
     * @param propertyName name of the action to be performed
     * @param playerId client's id
     * @param string String to be sent with the event
     */
    public RequestEvent(String propertyName, int playerId, String string) {
        this.propertyName = propertyName;
        this.playerId = playerId;
        this.values = null;
        this.string = string;
    }

    /**
     * Returns the client's id of the event
     * @return client's id
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Returns the array sent with the event
     * @return the array sent with the event
     */
    public int[] getValues() {
        return values;
    }

    /**
     * Returns the property name of the event
     * @return the property name of the event
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the String sent with the event
     * @return the String sent with the event
     */
    public String getString() {
        return string;
    }
}
