package it.polimi.ingsw.model.card;

import java.util.ArrayList;
import java.util.Collections;


public class Deck {
    private ArrayList<Card> cardArrayList;

    public Deck(ArrayList<Card> cardArrayList) {
        this.cardArrayList = cardArrayList;
    }

    /**
     * Shuffles the given deck.
     * @param deck to shuffle.
     */
    public void shuffleDeck(Deck deck) {
        Collections.shuffle(deck.cardArrayList);
    }

    public void moveInCard(Card card, Deck src) {
        cardArrayList.add(card);
        src.cardArrayList.remove(card);
    }
}
