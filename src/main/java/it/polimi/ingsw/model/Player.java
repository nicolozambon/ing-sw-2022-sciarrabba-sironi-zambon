package it.polimi.ingsw.model;

import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String nickname;
    private final School school;
    public final int id;

    private List<Coin> coins = new ArrayList<>();

    private final Deck assistantCardDeck;
    private final Deck discardPileDeck;


    public Player(String nickname, List<Student> students, List<Tower> towers, int id, Deck ACD, Deck DPD, Coin coin) {
        this.nickname = nickname;
        this.id = id;
        school = new School(this, students, towers);
        this.assistantCardDeck = ACD;
        this.discardPileDeck = DPD;
        coins.add(coin);
    }

    public School getSchool() {
        return school;
    }

    /**
    * @return Player's nickname
    **/
    public String getNickname() {
        return this.nickname;
    }

    public Card playAssistantCard(int index){
        Card chosen = assistantCardDeck.cards.get(index);
        discardPileDeck.moveInCard(chosen, assistantCardDeck);

        return chosen;
    }

    public void playCharacterCard(CharacterCard card){
        int cost = card.getCost();
        if (coins.size() < cost); //TODO gestire errore
        for (int i = 0; i < cost; i++){
            coins.remove(0);
        }
    }

    public AssistantCard lastAssistantCard(){
        return (AssistantCard)discardPileDeck.cards.get(discardPileDeck.cards.size()-1);
    }


}
