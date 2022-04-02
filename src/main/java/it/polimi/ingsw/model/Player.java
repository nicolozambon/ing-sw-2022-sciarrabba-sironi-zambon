package it.polimi.ingsw.model;

import it.polimi.ingsw.model.component.*;
import it.polimi.ingsw.model.component.card.*;

import java.util.List;
import java.util.Objects;

public class Player {

    private final String nickname;
    private final School school;
    public final int ID;

    private int coins;

    private final Deck assistantCardDeck;
    private final Deck discardPileDeck;


    public Player(int ID, String nickname, List<Student> students, List<Tower> towers, Deck assistantCardDeck) {
        this.nickname = nickname;
        this.ID = ID;
        this.school = new School(this, students, towers);
        this.assistantCardDeck = assistantCardDeck;
        this.discardPileDeck = new Deck();
        this.coins = 1;
    }

    public String getNickname() {
        return this.nickname;
    }

    public School getSchool() {
        return school;
    }

    public int getCoins() {
        return this.coins;
    }

    private void increaseCoinValueByOne() {
        this.coins++;
    }

    private void decreaseCoinValueByOne() {
        this.coins--;
    }

    public void playAssistantCard(int index) {
        Card chosen = assistantCardDeck.cards.get(index);
        discardPileDeck.moveInCard(chosen, assistantCardDeck);
    }

    public void playCharacterCard(CharacterCard card) {
        int cost = card.getCoins();
        if (coins < cost);
        for (int i = 0; i < cost; i++){
            this.decreaseCoinValueByOne();
        }
    }

    public AssistantCard getLastAssistantCard() {
        return (AssistantCard)discardPileDeck.cards.get(discardPileDeck.cards.size()-1);
    }

    public boolean moveStudentDiningRoom(Student student, int coins) {
        if (school.moveStudentDiningRoom(student) && coins > 0) {
            this.increaseCoinValueByOne();
            return true;
        }
        return false;
    }

    public void moveStudentIsland(Student student, Island island) {
        school.moveStudentIsland(student, island);
    }

    public void takeStudentsFromCloud(Cloud cloud) {
        this.school.takeStudentsFromCloud(cloud);
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
