package it.polimi.ingsw.model.card;

import java.util.ArrayList;
import java.util.List;

public class Deck<T> {

    private final List<T> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<T> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public void clear() {
        cards.clear();
    }

    public List<T> getCards() {
        return new ArrayList<T>(cards);
    }

    public void moveInCard(T card, Deck<T> src) {
        this.cards.add(card);
        src.cards.remove(card);
    }
}
