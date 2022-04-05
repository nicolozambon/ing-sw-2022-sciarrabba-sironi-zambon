package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.Deck;

import java.util.List;
import java.util.Objects;

public class Player {

    private final String nickname;
    private final School school;
    public final int id;

    private int coins;

    private final Deck<AssistantCard> assistantCardDeck;
    private final Deck<AssistantCard> discardPileDeck;


    protected Player(int id, String nickname, List<Student> students, List<Tower> towers, Deck<AssistantCard> assistantCardDeck) {
        this.nickname = nickname;
        this.id = id;
        this.school = new School(this, students, towers);
        this.assistantCardDeck = assistantCardDeck;
        this.discardPileDeck = new Deck();
        this.coins = 1;
    }

    public String getNickname() {
        return this.nickname;
    }

    protected School getSchool() {
        return school;
    }

    protected int getCoins() {
        return this.coins;
    }

    protected void playAssistantCard(int index) {
        for(AssistantCard card : assistantCardDeck.getCards()) {
            if(card.getId() == index) discardPileDeck.moveInCard(card, assistantCardDeck);
        }
    }

    protected void playCharacterCard(CharacterCard card) {
        int cost = card.getCoins();
        if (this.coins < cost) {
            this.coins -= cost;
        }
    }

    public AssistantCard getLastAssistantCard() {
        return discardPileDeck.getCards().get(discardPileDeck.getCards().size()-1);
    }

    protected boolean moveStudentDiningRoom(Student student, int coinReserve) {
        school.moveStudentDiningRoom(student);
        if (coinReserve > 0 && school.getDiningRoomByColor(student.getColor()).getNumPawns() % 3 == 0) {
            this.coins += 1;
            return true;
        }
        return false;
    }

    protected void moveStudentIsland(Student student, Island island) {
        school.moveStudentIsland(student, island);
    }

    protected void takeStudentsFromCloud(Cloud cloud) {
        this.school.takeStudentsFromCloud(cloud);
    }

    protected void returnStudentsToBag(StudentBag bag, Color color, int num) {
        this.school.returnStudentsToBag(bag, color, num);
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
