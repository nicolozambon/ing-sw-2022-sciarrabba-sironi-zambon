package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.listenables.AnswerListenable;
import it.polimi.ingsw.listenables.AnswerListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.model.card.CharacterCard;

import java.util.ArrayList;
import java.util.List;


public class Model implements AnswerListenableInterface {

    private final List<Player> players;
    private final List<Island> islands;
    private final List<Cloud> clouds;
    private final MotherNature motherNature;
    private final List<CharacterCard> characterCards;
    private int coinReserve;
    private final Board<Professor> startingProfessorBoard;
    private final StudentBag bag;
    private final int numStudentToMove;

    private transient Controller controller;
    private transient Handler handler;

    private boolean isThereWinner;
    private Player winner;

    private transient final AnswerListenable answerListenable;
    private transient final Gson gson;


    protected Model(List<Player> players, List<Island> islands, List<Cloud> clouds, MotherNature motherNature,
                 List<CharacterCard> characterCards, int coinReserve, Board<Professor> startingProfessorBoard, StudentBag bag, int numStudentToMove) {
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
        this.isThereWinner = false;
        this.winner = null;

        this.answerListenable = new AnswerListenable();
        this.gson = new Gson();
    }

    public void playAssistantCard(int playerId, int choice) throws CardException {
        players.get(playerId).playAssistantCard(choice);
        fireAnswer(new AnswerEvent("update", this));
    }


    public void playCharacterCard(int playerId, int choice) throws NotEnoughCoinsException, CardException {
        CharacterCard card;
        if (characterCards.stream().anyMatch(x -> x.getId() == choice)) {
            card = characterCards.stream().filter(x -> x.getId() == choice).findFirst().get();
            players.get(playerId).playCharacterCard(card);
            this.coinReserve += card.getCoins() - 1;
            card.incrementCoinCost();
            this.handler = new HandlerFactory().buildHandler(new ArrayList<>(players), card);
        } else {
            throw new CardException("Invalid character card played! Retry");
        }
        fireAnswer(new AnswerEvent("update", this));
    }

    public void moveStudentToDiningRoom(int playerId, int choice) throws InvalidActionException {
        Player player = players.get(playerId);
        Student student = player.getSchool().getEntrance().getPawns().get(choice);
        if (player.moveStudentDiningRoom(student, this.coinReserve))  this.coinReserve--;
        this.handler.professorControl(players.get(playerId), student.getColor(), startingProfessorBoard);
        fireAnswer(new AnswerEvent("update", this));
    }

    public void moveStudentToIsland(int playerId, int studentChoice, int islandChoice) {
        Player player = players.get(playerId);
        Student student = player.getSchool().getEntrance().getPawns().get(studentChoice);
        player.moveStudentIsland(student, islands.get(islandChoice));
        fireAnswer(new AnswerEvent("update", this));
    }

    public void moveMotherNature(int playerId, int stepsChoice) throws MotherNatureStepsException {
        this.handler.motherNatureMovement(players.get(playerId), motherNature, stepsChoice);
        playerHasFinishedTowers();
        threeGroupsIslandRemaining();
        fireAnswer(new AnswerEvent("update", this));
    }

    public void addStudentsToClouds() {
        for (Cloud cloud : clouds) {
            for (int i = 0; i < numStudentToMove; i++) {
                bag.extractStudentAndMove(cloud);
            }
        }
        fireAnswer(new AnswerEvent("update", this));
    }

    public void takeStudentsFromCloud(int playerId, int choice) throws CloudException {
        if (choice < 0 || choice > clouds.size() - 1 || clouds.get(choice).getNumPawns() == 0) throw new CloudException();
        players.get(playerId).takeStudentsFromCloud(clouds.get(choice));
        fireAnswer(new AnswerEvent("update", this));
    }

    public void extraAction(int playerId, int ... values) throws Exception {
        this.handler.extraAction(players.get(playerId), this, values);
        fireAnswer(new AnswerEvent("update", this));
    }

    protected MotherNature getMotherNature() {
        return this.motherNature;
    }

    protected List<Professor> getProfessors() {
        return new ArrayList<>(startingProfessorBoard.getPawns());
    }

    protected List<Cloud> getClouds() {
        return new ArrayList<>(clouds);
    }

    public List<CharacterCard> getCharacterCards() {
        return new ArrayList<>(characterCards);
    }

    protected List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    protected int getCoinReserve() {
        return this.coinReserve;
    }

    public void resetHandler() {
        this.handler = new Handler(this.players);
    }

    protected List<Island> getIslands(){
        return new ArrayList<>(this.islands);
    }

    protected void returnStudentsToBag(Color color, int num){
        for (Player player : players) {
            player.returnStudentsToBag(bag, color, num);
        }
        //fireAnswer(new AnswerEvent("update", this));
    }

    protected void exchangeStudentsDiningRoomEntrance(int playerId, int entrancePawnPosition, Color color) throws InvalidActionException {
        Player player = players.get(playerId);
        player.exchangeStudentsDiningRoomEntrance(entrancePawnPosition, color);

        this.handler.professorControl(player, color, startingProfessorBoard);
        Color entranceStudentColor = player.getSchool().getEntrance().getPawns().get(entrancePawnPosition).getColor();
        this.handler.professorControl(player, entranceStudentColor, startingProfessorBoard);

        if (player.getSchool().getDiningRoomByColor(entranceStudentColor).getNumPawns() % 3 == 0) {
            player.increaseCoinBy(1);
        }


    }

    /**
     * Methods isThereWinner checks if there is at least one condition for which the game ends.
     * @return NULL if there is no winner, the winning player otherwise.
     */

    //TODO: separate isThereWinner in single functions
    protected Player isThereWinner() {
        //The game ends immediately...

        //... when a player builds their last tower -> that player wins the game
        if (playerHasFinishedTowers()) return this.winner;

        //... when only 3 groups of islands remain on the table
        if (threeGroupsIslandRemaining()) return this.winner;

        //... when the last student has been drawn from the bag
        //if (bag.getPawns().size() == 0) return playerWithMostTowersOrProfessors();
        studentBagIsEmpty();

        //... or should any player run out of Assistant Cards in their hand
        //if (aPlayerHasFinishedAssistantCards()) return playerWithMostTowersOrProfessors();
        anyPlayerHasFinishedAssistantCards();

        return null;
    }

    /**
     * Method checks if the current player has finished towers. If so, that player wins.
     * This check has to happen every time a tower is moved.
     * @return true if there is a winner, false otherwise.
     */
    protected boolean playerHasFinishedTowers () {
        for (Player player : players) {
            if (player.getSchool().getTowersBoard().getNumPawns() == 0) {
                this.setIsThereWinner(true);
                this.setWinner(player);
                return true;
            }
        }
        return false;
    }

    /**
     * Method checks if there are 3 groups of islands remaining. If so, the player with most towers or professors wins.
     * This check has to happen every time there is a change in the islands.
     * @return the winner if there is a winner, null otherwise.
     */
    protected boolean threeGroupsIslandRemaining () {
        if (numOfGroupsOfIslands() < 4) {
            this.setIsThereWinner(true);
            this.setWinner(playerWithMostTowersOrProfessors());
            return true;
        }
        return false;
    }

    /**
     * If the studentBag is empty, the match ends and the player with most towers or professors wins.
     * This checks has to happen at the end of (every) round.
     * @return the winner if there is a winner, null otherwise.
     */
    protected Player studentBagIsEmpty () {
        if (bag.getPawns().size() == 0) return playerWithMostTowersOrProfessors();
        return null;
    }

    /**
     * If any player has finished assistants card, then the match ends and the player with most towers or professors wins.
     * This checks has to happen at the end of (every) round.
     * @return the winner if there is a winner, null otherwise.
     */
    protected Player anyPlayerHasFinishedAssistantCards() {
        if (aPlayerHasFinishedAssistantCards()) return playerWithMostTowersOrProfessors();
        return null;
    }



    private int numOfGroupsOfIslands() {
        int num = 0;
        Island island = islands.get(0);

        while (island.isUnifyPrev()) {
            island = island.getPrevIsland();
        }

        for (int i = 0; i < 12; i++) {
            if (!island.isUnifyNext()) num++;
            island = island.getNextIsland();
            num++;

        }

        return num;
    }

    private Player playerWithMostTowersOrProfessors() {
        int minTowersInBoard = 10;
        Player playerMinTowersInBoard = null;

        for (Player player : players) {
            if (player.getSchool().getTowersBoard().getPawns().size() < minTowersInBoard) {
                minTowersInBoard = player.getSchool().getTowersBoard().getPawns().size();
                playerMinTowersInBoard = player;
            }
        }

        for (Player player : players) {
            if (player.getSchool().getTowersBoard().getPawns().size() == minTowersInBoard && !player.equals(playerMinTowersInBoard)) {
                return playerWithMostProfessors();
            }
        }
        return playerMinTowersInBoard;
    }

    private Player playerWithMostProfessors() {
        int maxProfessorsControlled = 0;
        Player playerMaxProfessorControlled = null;

        for (Player player : players) {
            if (player.getSchool().getProfessorsTable().getPawns().size() > maxProfessorsControlled) {
                maxProfessorsControlled = player.getSchool().getProfessorsTable().getPawns().size();
                playerMaxProfessorControlled = player;
            }
        }

        return playerMaxProfessorControlled;
    }

    private boolean aPlayerHasFinishedAssistantCards () {
        for (Player player : players) {
            if (player.getAssistantCardDeckSize() == 0) return true;
        }
        return false;
    }

    public Controller getController() {
        if (controller == null) {
            controller = new Controller(new ArrayList<>(this.players), this, this.numStudentToMove);
        }
        return controller;
    }

    public boolean getIsThereWinner() {
        return this.isThereWinner;
    }

    public void setIsThereWinner(boolean isThereWinner) {
        this.isThereWinner = isThereWinner;
    }

    public void setWinner(Player player) {
        this.winner = player;
    }

    public Player getWinner() {
        return this.winner;
    }

    protected Handler getHandler() {
        return handler;
    }

    public int getCharacterCardIdByHandler() {
        return this.handler.getCardId();
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
