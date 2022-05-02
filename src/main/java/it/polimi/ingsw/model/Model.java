package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InvalidCardException;
import it.polimi.ingsw.exceptions.InvalidMotherNatureStepsException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.model.card.CharacterCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Model implements Serializable {
    private static final long serialVersionUID = 987654321L;

    private final List<Player> players;
    private final List<Island> islands;
    private final List<Cloud> clouds;
    private final MotherNature motherNature;
    private final List<CharacterCard> characterCards;
    private int coinReserve;
    private final Board<Professor> startingProfessorBoard;
    private final StudentBag bag;
    private final int numStudentToMove;
    private Controller controller;

    private Handler handler;

    private boolean isThereWinner;
    private Player winner;


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
    }

    public void playAssistantCard(int playerId, int choice) {
        players.get(playerId).playAssistantCard(choice);
    }


    public void playCharacterCard(int playerId, int choice) throws NotEnoughCoinsException, InvalidCardException {
        CharacterCard card;
        if (characterCards.stream().anyMatch(x -> x.getId() == choice)) {
            card = characterCards.stream().filter(x -> x.getId() == choice).findFirst().get();
            players.get(playerId).playCharacterCard(card);
            card.incrementCoinCost();
            this.handler = new HandlerFactory().buildHandler(new ArrayList<>(players), card);
        } else {
            throw new InvalidCardException();
        }
    }

    public void moveStudentToDiningRoom(int playerId, int choice) {
        Player player = players.get(playerId);
        Student student = player.getSchool().getEntrance().getPawns().get(choice);
        if (player.moveStudentDiningRoom(student, this.coinReserve))  this.coinReserve--;
        this.handler.professorControl(players.get(playerId), student.getColor(), startingProfessorBoard);
    }

    public void moveStudentToIsland(int playerId, int studentChoice, int islandChoice) {
        Player player = players.get(playerId);
        Student student = player.getSchool().getEntrance().getPawns().get(studentChoice);
        player.moveStudentIsland(student, islands.get(islandChoice));
    }

    public void moveMotherNature(int playerId, int stepsChoice) throws InvalidMotherNatureStepsException {
        this.handler.motherNatureMovement(players.get(playerId), motherNature, stepsChoice);
        playerHasFinishedTowers();
        //TODO Fix, position OutOfBound
        //threeGroupsIslandRemaining();

    }

    public void addStudentsToClouds() {
        for (Cloud cloud : clouds) {
            for (int i = 0; i < numStudentToMove; i++) {
                bag.extractStudentAndMove(cloud);
            }
        }
    }

    protected MotherNature getMotherNature() {
        return this.motherNature;
    }

    protected List<Professor> getProfessors() {
        return new ArrayList<>(startingProfessorBoard.getPawns());
    }

    //TODO: remove?
    protected Board<Professor> getProfessorsBoard() {
        return this.startingProfessorBoard;
    }

    protected List<Cloud> getClouds() {
        return new ArrayList<>(clouds);
    }

    public void takeStudentsFromCloud(int playerId, int choice) {
        players.get(playerId).takeStudentsFromCloud(clouds.get(choice));
    }

    public void extraAction(int ... values) {
        this.handler.extraAction(players.get(0), this, values);
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
        if (numOfGroupsOfIslands() == 3) {
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



    private int numOfGroupsOfIslands () {
        int numOfGroupOfIslands = 0;
        int position = 0;
        Island currentIsland = islands.get(0);

        while (currentIsland.isUnifyPrev()) {
            currentIsland = currentIsland.getPrevIsland();
        }

        while (position < islands.size()) {
            if (!currentIsland.isUnifyNext()) {
                numOfGroupOfIslands++;
            }
            position++;
            currentIsland = islands.get(position);
        }
        return numOfGroupOfIslands;
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
}
