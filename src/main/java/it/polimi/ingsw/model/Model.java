package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.Wizard;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.listenables.AnswerListenable;
import it.polimi.ingsw.listenables.AnswerListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.model.card.CharacterCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class for Model in MVC Pattern. Holds the state of the game and part of the game logic and rules
 */
public class Model implements AnswerListenableInterface {
    /**
     * List of the Players for the match
     */
    private final List<Player> players;

    /**
     * List of all the islands
     * @see Island
     */
    private final List<Island> islands;

    /**
     * List of all the clouds
     * @see Cloud
     */
    private final List<Cloud> clouds;

    /**
     * Mother Nature object
     * @see MotherNature
     */
    private final MotherNature motherNature;

    /**
     * List of all Character Cards
     * @see CharacterCard
     */
    private final List<CharacterCard> characterCards;

    /**
     * Coin reserve value
     */
    private int coinReserve;

    /**
     * Initial board where professors are generated and stored, before being put on the player's schools
     */
    private final Board<Professor> startingProfessorBoard;

    /**
     * Student bag for the match
     * @see StudentBag
     */
    private final StudentBag bag;

    /**
     * Number of students to move, depending on the number of players
     */
    private final int numStudentToMove;

    /**
     * Controller associated, handles game dynamics
     * @see Controller
     */
    private Controller controller;

    /**
     * Handler associated
     * @see Handler
     */
    private Handler handler;

    /**
     * List that holds winners of the game
     */
    private List<String> winner;

    /**
     * Boolean that is true if it is the last round
     */
    private boolean isLastRound;

    /**
     * Boolean true if current action is to take students from a cloud
     */
    private boolean isTakeCloud;

    /**
     * True if the match is being played with the complete rules (advanced rules)
     */
    private final boolean completeRule;

    /**
     * Answer Listenable associated
     * @see AnswerListenable
     */
    private transient AnswerListenable answerListenable;

    /**
     * Model constructor. Initializes all parameters of the class that determine the rules and the starting point of the match
     * @param players List of players
     * @param islands List of the islands
     * @param clouds List of the clouds
     * @param motherNature Mother Nature instance
     * @param characterCards List of the Character Cards
     * @param coinReserve Value of the starting coin reserve
     * @param startingProfessorBoard The starting professors board, holding all the professors of the game, to be moved in the player's schools
     * @param bag The student bag holding all the student pawns
     * @param numStudentToMove The number of students to move, depending on the number of players
     * @param completeRule True if the match is to be played with the full rules
     */
    protected Model(List<Player> players, List<Island> islands, List<Cloud> clouds, MotherNature motherNature,
                    List<CharacterCard> characterCards, int coinReserve, Board<Professor> startingProfessorBoard,
                    StudentBag bag, int numStudentToMove, boolean completeRule) {

        this.players = players;
        this.islands = islands;
        this.clouds = clouds;
        this.motherNature = motherNature;
        this.characterCards = characterCards;
        this.coinReserve = coinReserve;
        this.startingProfessorBoard = startingProfessorBoard;
        this.bag = bag;

        this.handler = new Handler(this.players);

        this.controller = null;
        this.numStudentToMove = numStudentToMove;
        this.winner = new ArrayList<>();

        this.isTakeCloud = true;
        this.isLastRound = false;
        this.completeRule = completeRule;

        this.answerListenable = new AnswerListenable();
    }

    /**
     * Sets the Wizard chosen for the player
     * @param playerId ID of the player that has made the choice
     * @param wizard The wizard chosen by the user
     */
    public void setWizard(int playerId, Wizard wizard) {
        players.get(playerId).setWizard(wizard);
    }

    /**
     * Plays the selected Assistant Card
     * @param playerId ID of the player that has played the card
     * @param choice Assistant Card ID chosen
     * @throws CardException
     */
    public void playAssistantCard(int playerId, int choice) throws CardException {
        players.get(playerId).playAssistantCard(choice);
        if (players.get(playerId).getAssistantCards().isEmpty()) isLastRound = true;
        update();
    }

    /**
     * Plays the selected Character Card
     * @param playerId ID of the player that has played the card
     * @param choice Character Card ID chosen
     * @return the played Character card
     * @throws NotEnoughCoinsException if the player doesn't have enough coins
     * @throws CardException if the Character Card is invalid
     */
    public CharacterCard playCharacterCard(int playerId, int choice) throws NotEnoughCoinsException, CardException {
        CharacterCard card;
        if (characterCards.stream().anyMatch(x -> x.getId() == choice)) {
            card = characterCards.stream().filter(x -> x.getId() == choice).findFirst().get();
            players.get(playerId).playCharacterCard(card);
            this.coinReserve += card.getCoins() - 1;
            card.incrementCoinCost();
            this.handler = new HandlerFactory().buildHandler(new ArrayList<>(players), card);
            update();
        } else {
            throw new CardException("Invalid character card played! Retry");
        }
        return card;
    }

    /**
     * Moves the chosen student to the playerId's Dining Room
     * @param playerId ID of the player to move the student to his Dining Room
     * @param choice Student to move into the Dining Room
     * @throws InvalidActionException
     */
    public void moveStudentToDiningRoom(int playerId, int choice) throws InvalidActionException {
        try {
            Player player = players.get(playerId);
            Student student = player.getSchool().getEntrance().getPawns().get(choice);
            if (player.moveStudentDiningRoom(student, this.coinReserve)) this.coinReserve--;
            this.handler.professorControl(players.get(playerId), student.getColor(), startingProfessorBoard);
            update();
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidActionException("Invalid pawn chosen, retry!");
        }
    }

    /**
     * Moves the chosen student to the selected island
     * @param playerId ID of the player that makes the move
     * @param studentChoice Choice of the student
     * @param islandChoice Choice of the island
     * @throws InvalidActionException if the pawn chosen is invalid
     */
    public void moveStudentToIsland(int playerId, int studentChoice, int islandChoice) throws InvalidActionException{
        try {
            Player player = players.get(playerId);
            Student student = player.getSchool().getEntrance().getPawns().get(studentChoice);
            player.moveStudentIsland(student, islands.get(islandChoice));
            update();
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidActionException("Invalid pawn chosen, retry!");
        }
    }

    /**
     * Moves MotherNature by "stepsChoice" steps
     * @param playerId ID of the player that makes the move
     * @param stepsChoice Number of steps to move mother nature to
     * @throws MotherNatureStepsException If the movement of Mother Nature requested is not valid
     */
    public void moveMotherNature(int playerId, int stepsChoice) throws MotherNatureStepsException {
        try {
            this.handler.motherNatureMovement(players.get(playerId), motherNature, stepsChoice);
            if (groupOfIslands() <= 3) {
                findWinner();
            }
        } catch (WinnerException e) {
            //e.printStackTrace();
            this.winner = new ArrayList<>(List.of(players.get(e.getPlayerId()).getNickname()));
        } finally {
            update();
        }

    }

    /**
     * Adds to all the clouds numStudentToMove students chosen randomly from the Student Bag
     */
    public void addStudentsToClouds() {
        try {
            for (Cloud cloud : clouds) {
                int num = cloud.getNumPawns();
                for (int i = 0; i < numStudentToMove - num; i++) {
                    bag.extractStudentAndMove(cloud);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            isLastRound = true;
            isTakeCloud = false;
        } finally {
            update();
        }
    }

    /**
     * Takes all the students from the chosen cloud and puts them in the playerId's School Entrance
     * @param playerId ID of the player to move the pawns to
     * @param choice Chosen cloud
     * @throws CloudException if the requested cloud is invalid (is empty or does not exist)
     */
    public void takeStudentsFromCloud(int playerId, int choice) throws CloudException {
        if (choice < 0 || choice > clouds.size() - 1 || clouds.get(choice).getNumPawns() == 0) throw new CloudException();
        players.get(playerId).takeStudentsFromCloud(clouds.get(choice));
        update();
    }

    /**
     * Perform an extra action required by the play of a Character Card
     * @param playerId ID of the player that requests the action
     * @param values Values required by the sub-methods of the appropriate Character Card
     * @throws Exception If any Exception is thrown by the sub-methods
     * @see Handler
     */
    public void extraAction(int playerId, int ... values) throws Exception {
        this.handler.extraAction(players.get(playerId), this, values);
        update();
    }

    /**
     * Resets the Last Played Assistant Cards info
     */
    public void resetLastAssistantCards() {
        for (Player player : players) {
            player.resetLastAssistantCard();
        }
        update();
    }

    /**
     * Given a selection of the color, returns "num" students of "color" color to the Student Bag
     * @param color Chosen color of the students
     * @param num Number of students to return to bag
     */
    protected void returnStudentsToBag(Color color, int num){
        for (Player player : players) {
            player.returnStudentsToBag(bag, color, num);
        }
        if (bag.getNumPawns() > 0 && isLastRound) {
            boolean check = true;
            for (Player p : players) {
                if (p.getAssistantCards().isEmpty()) check = false;
            }
            if (check) {
                isLastRound = false;
                winner.clear();
            }
        }
    }

    /**
     * Exchanges Students from the Dining Room to the Entrance
     * @param playerId ID of the player that requests the action
     * @param entrancePawnPosition Position of the pawn in Entrance
     * @param color Color of the student (for the dining room selection)
     * @throws InvalidActionException
     */
    protected void exchangeStudentsDiningRoomEntrance(int playerId, int entrancePawnPosition, Color color) throws InvalidActionException {
        Player player = players.get(playerId);
        Color entranceStudentColor = player.getSchool().getEntrance().getPawns().get(entrancePawnPosition).getColor();

        player.exchangeStudentsDiningRoomEntrance(entrancePawnPosition, color);

        this.handler.professorControl(player, color, startingProfessorBoard);
        this.handler.professorControl(player, entranceStudentColor, startingProfessorBoard);

        if (player.getSchool().getDiningRoomByColor(entranceStudentColor).getNumPawns() % 3 == 0) {
            player.increaseCoinBy(1);
        }
    }

    /**
     * @return true if current phase is Take From Cloud
     */
    public boolean isTakeCloud() {
        return isTakeCloud;
    }

    /**
     * @return true if this is the last round
     */
    public boolean isLastRound() {
        return isLastRound;
    }

    /**
     * @return true if the match is being played with the full rules
     */
    public boolean isCompleteRule() {
        return completeRule;
    }

    /**
     * @return Mother Nature instance
     */
    protected MotherNature getMotherNature() {
        return this.motherNature;
    }

    /**
     * @return a List of all Professors
     */
    protected List<Professor> getProfessors() {
        return new ArrayList<>(startingProfessorBoard.getPawns());
    }

    /**
     * @return a List of all the Clouds
     */
    protected List<Cloud> getClouds() {
        return new ArrayList<>(clouds);
    }

    /**
     * @return a List of all the Character Cards
     */
    public List<CharacterCard> getCharacterCards() {
        return new ArrayList<>(characterCards);
    }

    /**
     * @return a List of all the players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * @return the value of the coin reserve
     */
    protected int getCoinReserve() {
        return this.coinReserve;
    }

    /**
     * @return a List of all the Islands
     */
    protected List<Island> getIslands(){
        return new ArrayList<>(this.islands);
    }

    /**
     * @return the Controller associated with the Model. If it doesn't exist, it creates one
     */
    public Controller getController() {
        if (controller == null) {
            controller = new Controller(new ArrayList<>(this.players), this, this.numStudentToMove);
        }
        return controller;
    }

    /**
     * @return the Handler associated with the Model
     */
    protected Handler getHandler() {
        return handler;
    }

    /**
     * @return the Character Card ID from the Handler getter method
     * @see Handler
     */
    public int getCharacterCardIdFromHandler() {
        return this.handler.getCardId();
    }

    /**
     * Creates a new Handler objects, overriding the current one
     */
    public void resetHandler() {
        this.handler = new Handler(this.players);
    }

    /**
     * @return true if there is at least one winner
     */
    public boolean isThereWinner() {
        return winner.size() > 0;
    }

    /**
     * Fires an AnswerEvent, answering an Update request, with the instance of this (model).
     * If there is a winner, notifies the players by firing a "winner" Answer Event
     * @see AnswerEvent
     */
    private void update() {
        fireAnswer(new AnswerEvent("update", this));
        if (isThereWinner()) fireAnswer(new AnswerEvent("winner", winner));
    }

    /**
     * Helper method when a saving is loaded and a match is resumed, creating a new Answer Listenable and a new list of winners
     */
    protected void resumeFromDisk() {
        this.answerListenable = new AnswerListenable();
        this.winner = new ArrayList<>();
    }

    /**
     * @return the number of groups of islands
     */
    private int groupOfIslands() {
        int num = 12;
        for (Island island : islands) {
            if (island.isUnifyNext()) num--;
        }
        return num;
    }

    /**
     * Finds a winner by checking the number of towers for each player and comparing them. Sets appropriately the list of winners
     * Uses helper method getPlayerWinningRanking
     */
    public void findWinner() {
        List<Player> temp = new ArrayList<>(players);
        temp = temp.stream().sorted(Comparator.comparingInt(p -> -getPlayerNumTowers(p))).toList();
        if (getPlayerNumTowers(temp.get(temp.size() - 2)) == getPlayerNumTowers(temp.get(temp.size() - 1))) {
            temp = new ArrayList<>(players);
            temp = temp.stream().sorted(Comparator.comparingInt(this::getPlayerWinningRanking)).toList();
            winner = new ArrayList<>();
            winner.add(temp.get(temp.size() - 1).getNickname());
            if (getPlayerWinningRanking(temp.get(temp.size() - 2)) == getPlayerWinningRanking(temp.get(temp.size() - 1))) {
                winner.add(temp.get(temp.size() - 1).getNickname());
            }
        } else {
            winner = new ArrayList<>();
            winner.add(temp.get(temp.size() - 1).getNickname());
        }
    }

    /**
     * Returns a value of the winning ranking for the desired player
     * @param player Player to check
     * @return Number of professors - number of towers
     */
    private int getPlayerWinningRanking(Player player) {
        School school = player.getSchool();
        return school.getProfessorsTable().getNumPawns() - school.getTowersBoard().getNumPawns();
    }

    /**
     * @param player Player to check
     * @return the number of towers in the Player's Tower board
     */
    private int getPlayerNumTowers(Player player) {
        return player.getSchool().getTowersBoard().getNumPawns();
    }

    @Override
    public void addAnswerListener(AnswerListener answerListener) {
        this.answerListenable.addAnswerListener(answerListener);
    }

    @Override
    public void removeAnswerListener(AnswerListener answerListener) {
        this.answerListenable.removeAnswerListener(answerListener);
    }

    @Override
    public void fireAnswer(AnswerEvent answerEvent) {
        this.answerListenable.fireAnswer(answerEvent);
    }
}
