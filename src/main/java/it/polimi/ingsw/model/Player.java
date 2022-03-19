package it.polimi.ingsw.model;

import java.util.List;

public class Player {

    private String nickname;
    public final School school;

    private Deck AssistantCardDeck;
    private Deck DiscardPileDeck;


    public Player(String nickname, List<Student> students, List<Tower> towers) {
        this.nickname = nickname;
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
