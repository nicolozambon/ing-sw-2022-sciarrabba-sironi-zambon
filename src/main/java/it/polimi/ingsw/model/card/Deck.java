package it.polimi.ingsw.model.card;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a deck of specified type
 * @param <T> the type of the deck
 */
public class Deck<T> {

    /**
     * List of the object in the deck
     */
    private final List<T> cards;

    /**
     * Constructor without any object in the deck
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Constructor with object in the deck
     * @param cards the list of the object that will be copied in deck
     */
    public Deck(List<T> cards) {
        this.cards = new ArrayList<>(cards);
    }

    /**
     * Clears the deck
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Returns a copy of the list of the object in the deck
     * @return the list of object in the deck
     */
    public List<T> getCards() {
        return new ArrayList<T>(cards);
    }

    /**
     * Move in an object from another deck
     * @param card the object to be moved
     * @param src the deck where the object is
     */
    public void moveInCard(T card, Deck<T> src) {
        this.cards.add(card);
        src.cards.remove(card);
    }
}
