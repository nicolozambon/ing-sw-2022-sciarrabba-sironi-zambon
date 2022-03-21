package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.*;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String nickname;
    public final School school;
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

    public List<Coin> playCharacterCard(int cost) {
        List<Coin> temp = new ArrayList<>();

        if (cost > coins.size() - 1) return temp;

        for (int i = 0; i < cost; i++) {
            temp.add(coins.remove(i));
        }
        return temp;
    }

    public AssistantCard lastAssistantCard(){
        return (AssistantCard)discardPileDeck.cards.get(discardPileDeck.cards.size()-1);
    }


}
