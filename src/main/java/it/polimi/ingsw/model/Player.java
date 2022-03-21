package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Deck;

import java.util.List;

public class Player {

    private String nickname;
    public final School school;
    public final int id;

    private Deck AssistantCardDeck;
    private Deck DiscardPileDeck;


    public Player(String nickname, List<Student> students, List<Tower> towers, int id) {
        this.nickname = nickname;
        this.id = id;
        school = new School(this, students, towers);
        this.AssistantCardDeck = new DeckAssistantCard(null);
        this.DiscardPileDeck = new Deck (null);
        //TODO: modify these null cardArrayLists
    }

    /**
    * @return Player's nickname
    **/
    public String getNickname() {
        return this.nickname;
    }


}
