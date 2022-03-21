package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.*;

import java.util.List;

public class Player {

    private String nickname;
    public final School school;
    public final int id;

    private final Deck AssistantCardDeck;
    private final Deck DiscardPileDeck;


    public Player(String nickname, List<Student> students, List<Tower> towers, int id, Deck ACD, Deck DPD) {
        this.nickname = nickname;
        this.id = id;
        school = new School(this, students, towers);
        this.AssistantCardDeck = ACD;
        this.DiscardPileDeck = DPD;
    }

    /**
    * @return Player's nickname
    **/
    public String getNickname() {
        return this.nickname;
    }

    public Card playAssistantCard(int index){
        Card chosen = AssistantCardDeck.cards.get(index);
        DiscardPileDeck.moveInCard(chosen, AssistantCardDeck);

        return chosen;
    }


}
