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

    /**
     * Nickname of the player
     */
    private final String nickname;

    /**
     * School of the player
     */
    private final School school;

    /**
     * Identifier of the player
     */
    private final int id;

    /**
     * Color of the towers of the player
     */
    private final TowerColor towerColor;

    /**
     * Coins of the player
     */
    private int coins;

    /**
     * Deck of assistant cards
     */
    private final Deck<AssistantCard> assistantCardDeck;

    /**
     * Deck of discard
     */
    private final Deck<AssistantCard> discardPileDeck;

    /**
     * Wizard of the player
     */
    private Wizard wizard;

    /**
     * Constructor of Player
     * @param id identifier
     * @param nickname nickname of the player
     * @param students students of the player
     * @param towers towers of the player
     * @param assistantCardDeck deck of AssistantCards of the player
     */
    protected Player(int id, String nickname, List<Student> students, List<Tower> towers, Deck<AssistantCard> assistantCardDeck) {
        this.nickname = nickname;
        this.id = id;
        this.school = new School(id, students, towers);
        this.towerColor = towers.get(0).getColor();
        this.assistantCardDeck = assistantCardDeck;
        this.discardPileDeck = new Deck<>();
        this.coins = 1;
    }

    /**
     * Getter the color of the towers
     * @return color of the towers
     */
    public TowerColor getTowerColor() {
        return towerColor;
    }

    /**
     * Getter of the nickname
     * @return nickname of the player
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Getter of the school
     * @return school of the player
     */
    protected School getSchool() {
        return school;
    }

    /**
     * Getter of the coins
     * @return coins of the player
     */
    protected int getCoins() {
        return this.coins;
    }

    /**
     * Getter of identifier
     * @return identifier of the player
     */
    public int getId() {
        return this.id;
    }

    /**
     * Play an AssistantCard
     * @param index number of the card
     * @throws CardException raised if an invalid card is played
     */
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

    /**
     * Getter of the AssistantCards
     * @return cards in the deck of the player
     */
    public List<AssistantCard> getAssistantCards() {
        return assistantCardDeck.getCards();
    }

    /**
     * Play a CharacterCard
     * @param card CharacterCard to be played
     * @throws NotEnoughCoinsException raised if the coins of the player are fewer of the cost of the card
     */
    protected void playCharacterCard(CharacterCard card) throws NotEnoughCoinsException {
        int cost = card.getCoins();
        if (this.coins >= cost) {
            this.coins = this.coins - cost;
        } else {
            throw new NotEnoughCoinsException();
        }
    }

    /**
     * Getter of last AssistantCard
     * @return last AssistantCard or null if not present
     */
    public AssistantCard getLastAssistantCard() {
        if (discardPileDeck.getCards().size() > 0) return discardPileDeck.getCards().get(discardPileDeck.getCards().size()-1);
        return null;
    }

    /**
     * Move student in dining room
     * @param student student to be moved
     * @param coinReserve reserve of coins
     * @return boolean
     * @throws InvalidActionException raised on invalid action
     */
    protected boolean moveStudentDiningRoom(Student student, int coinReserve) throws InvalidActionException {
        school.moveStudentDiningRoom(student);
        if (coinReserve > 0 && school.getDiningRoomByColor(student.getColor()).getNumPawns() % 3 == 0) {
            this.coins += 1;
            return true;
        }
        return false;
    }

    /**
     * Move student to island
     * @param student student to be moved
     * @param island island of destination
     */
    protected void moveStudentIsland(Student student, Island island) {
        school.moveStudentIsland(student, island);
    }

    /**
     * Take students from cloud
     * @param cloud cloud of source
     */
    protected void takeStudentsFromCloud(Cloud cloud) {
        this.school.takeStudentsFromCloud(cloud);
    }

    /**
     * Put students to bag
     * @param bag StudentBag instance
     * @param color color of the students
     * @param num amount of students
     */
    protected void returnStudentsToBag(StudentBag bag, Color color, int num) {
        this.school.returnStudentsToBag(bag, color, num);
    }

    /**
     * Exchange student entrance and dining room
     * @param entrancePawnPosition position of the pawn in entrance
     * @param color color of the student
     * @throws InvalidActionException raised on invalid action
     */
    protected void exchangeStudentsDiningRoomEntrance(int entrancePawnPosition, Color color) throws InvalidActionException {
        school.exchangeStudentsDiningRoomEntrance(entrancePawnPosition, color);
    }

    /**
     * Increase coins of a given amount
     * @param value value to be added
     */
    protected void increaseCoinBy(int value) {
        this.coins += value;
    }

    /**
     * Reset last AssistantCard
     */
    protected void resetLastAssistantCard() {
        discardPileDeck.clear();
    }

    /**
     * Getter for the wizard
     * @return wizard
     */
    protected Wizard getWizard() {
        return wizard;
    }

    /**
     * Setter for the wizard
     * @param wizard wizard
     */
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
