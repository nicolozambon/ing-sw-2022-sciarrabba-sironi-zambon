package it.polimi.ingsw.model.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck<T> {

    private final List<T> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<T> cards) {
        this.cards = new ArrayList<>(cards);
    }

    /**
     * Shuffles the given deck.
     * @param deck to shuffle.
     */
    public void shuffleDeck(Deck<T> deck) {
        Collections.shuffle(deck.cards);
    }

    public List<T> getCards() {
        return new ArrayList<T>(cards);
    }

    public void moveInCard(T card, Deck<T> src) {
        this.cards.add(card);
        src.cards.remove(card);
    }
}
