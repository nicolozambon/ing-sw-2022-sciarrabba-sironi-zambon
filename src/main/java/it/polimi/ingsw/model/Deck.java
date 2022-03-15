package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;


public class Deck {
    private ArrayList<Card> cardArrayList;

    public Deck(ArrayList<Card> cardArrayList) {
        this.cardArrayList = cardArrayList;
    }

    /**
     * @return extracted Card
     **/
    public Card extractCard() {
        return null;
    }

    /**
     * Shuffles the given deck.
     * @param deck to shuffle.
     */
    public void shuffleDeck(Deck deck) {
        Collections.shuffle(deck.cardArrayList);
    }

    /**
     * @param deck1 departure
     * @param deck2 destination
     * @param card to be moved
     */
    public void moveCard(Deck deck1, Deck deck2, Card card) {
        int position = deck1.cardArrayList.indexOf(card);
        deck2.cardArrayList.add(deck1.cardArrayList.get(position));
        deck1.cardArrayList.remove(card);
    }
}
