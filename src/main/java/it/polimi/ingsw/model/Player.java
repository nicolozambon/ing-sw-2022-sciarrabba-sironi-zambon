package it.polimi.ingsw.model;

import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {

    private final String nickname;
    private final School school;
    public final int id;

    private final List<Coin> coins = new ArrayList<>();

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

    public void playAssistantCard(int index) {
        Card chosen = assistantCardDeck.cards.get(index);
        discardPileDeck.moveInCard(chosen, assistantCardDeck);
    }

    public void playCharacterCard(CharacterCard card) {
        int cost = card.getCoins();
        if (coins.size() < cost);
        for (int i = 0; i < cost; i++){
            coins.remove(0);
        }
    }

    public AssistantCard lastAssistantCard() {
        return (AssistantCard)discardPileDeck.cards.get(discardPileDeck.cards.size()-1);
    }

    public void moveStudentDiningRoom(Student student, List<Coin> coins) {
        if (school.moveStudentDiningRoom(student)) {
            this.coins.add(coins.remove(0));
        }
    }

    public void moveStudentIsland(Student student, Island island) {
        school.moveStudentIsland(student, island);
    }

    public void chooseCloud(Cloud cloud) {
        school.takeStudentsFromCloud(cloud);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
