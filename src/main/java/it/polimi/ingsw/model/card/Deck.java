package it.polimi.ingsw.model.card;

import java.util.ArrayList;
import java.util.Collections;


public class Deck {

    public final ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<Card>();
    }

    public Deck(ArrayList<Card> cardArrayList) {
        this.cards = cardArrayList;
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
