package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.exceptions.CardException;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;
import it.polimi.ingsw.model.card.Deck;

import java.util.List;
import java.util.Objects;

public class Player {

    private final String nickname;
    private final School school;
    private final int id;
    private final TowerColor towerColor;

    private int coins;

    private final Deck<AssistantCard> assistantCardDeck;
    private final Deck<AssistantCard> discardPileDeck;

    private Wizard wizard;


    protected Player(int id, String nickname, List<Student> students, List<Tower> towers, Deck<AssistantCard> assistantCardDeck) {
        this.nickname = nickname;
        this.id = id;
        this.school = new School(id, students, towers);
        this.towerColor = towers.get(0).getColor();
        this.assistantCardDeck = assistantCardDeck;
        this.discardPileDeck = new Deck<>();
        this.coins = 1;
    }

    public TowerColor getTowerColor() {
        return towerColor;
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

    public int getId() {
        return this.id;
    }

    protected void playAssistantCard(int index) throws CardException {
        boolean played = false;
        for(AssistantCard card : assistantCardDeck.getCards()) {
            if(card.getValue() == index) {
                discardPileDeck.moveInCard(card, assistantCardDeck);
                played = true;
            }
        }
        if (!played) throw new CardException("Invalid assistant card played! Retry");

    }

    public List<AssistantCard> getAssistantCards() {
        return assistantCardDeck.getCards();
    }

    protected void playCharacterCard(CharacterCard card) throws NotEnoughCoinsException {
        int cost = card.getCoins();
        if (this.coins >= cost) {
            this.coins = this.coins - cost;
        } else {
            throw new NotEnoughCoinsException();
        }
    }

    public AssistantCard getLastAssistantCard() {
        if (discardPileDeck.getCards().size() > 0) return discardPileDeck.getCards().get(discardPileDeck.getCards().size()-1);
        return null;
    }

    protected boolean moveStudentDiningRoom(Student student, int coinReserve) throws InvalidActionException {
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

    protected void exchangeStudentsDiningRoomEntrance(int entrancePawnPosition, Color color) throws InvalidActionException {
        school.exchangeStudentsDiningRoomEntrance(entrancePawnPosition, color);
    }

    protected void increaseCoinBy(int value) {
        this.coins += value;
    }

    protected int getAssistantCardDeckSize() {
        return this.assistantCardDeck.getCards().size();
    }

    protected Wizard getWizard() {
        return wizard;
    }

    protected void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && nickname.equals(player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", id=" + id +
                '}';
    }
}
