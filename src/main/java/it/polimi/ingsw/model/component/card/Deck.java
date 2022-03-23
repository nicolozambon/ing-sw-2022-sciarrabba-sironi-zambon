package it.polimi.ingsw.model.component.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck {

    public final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    /**
     * Shuffles the given deck.
     * @param deck to shuffle.
     */
    public void shuffleDeck(Deck deck) {
        Collections.shuffle(deck.cards);
    }

    public void moveInCard(Card card, Deck src) {
        cards.add(card);
        src.cards.remove(card);
    }
}
