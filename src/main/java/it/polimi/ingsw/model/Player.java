package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.*;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String nickname;
    public final School school;
    public final int id;

    private List<Coin> coins = new ArrayList<>();

    private final Deck AssistantCardDeck;
    private final Deck DiscardPileDeck;


    public Player(String nickname, List<Student> students, List<Tower> towers, int id, Deck ACD, Deck DPD, Coin coin) {
        this.nickname = nickname;
        this.id = id;
        school = new School(this, students, towers);
        this.AssistantCardDeck = ACD;
        this.DiscardPileDeck = DPD;
        coins.add(coin);
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

    public List<Coin> playCharacterCard(int cost) {
        List<Coin> temp = new ArrayList<>();

        if (cost > coins.size() - 1) return temp;

        for (int i = 0; i < cost; i++) {
            temp.add(coins.remove(i));
        }
        return temp;
    }


}
