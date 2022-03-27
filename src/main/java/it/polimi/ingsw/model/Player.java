package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {

    private final String nickname;
    private final School school;
    private final TowerColor towerColor;
    public final int ID;

    private final List<Coin> coins = new ArrayList<>();

    private final Deck assistantCardDeck;
    private final Deck discardPileDeck;


    public Player(int ID, String nickname, List<Student> students, List<Tower> towers, Deck assistantCardDeck, Coin coin) {
        this.nickname = nickname;
        this.ID = ID;
        this.school = new School(this, students, towers);
        this.assistantCardDeck = assistantCardDeck;
        this.discardPileDeck = new Deck();
        this.coins.add(coin);

        // TODO: randomize and choose a color (or is it chosen by the client?).
        this.towerColor = TowerColor.GREY;
    }

    public School getSchool() {
        return school;
    }

    public TowerColor getTowerColor() {
        return towerColor;
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

    public AssistantCard getLastAssistantCard() {
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
        return ID == player.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
