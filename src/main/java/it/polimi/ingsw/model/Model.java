package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InvalidCardIdException;
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
    }

    public void playAssistantCard(int playerId, int choice) {
        players.get(playerId).playAssistantCard(choice);
    }


    public void playCharacterCard(int playerId, int choice) throws NotEnoughCoinsException, InvalidCardIdException {
        CharacterCard card;
        if (characterCards.stream().anyMatch(x -> x.getId() == choice)) {
            card = characterCards.stream().filter(x -> x.getId() == choice).findFirst().get();
            players.get(playerId).playCharacterCard(card);
            card.incrementCoinCost();
            this.handler = new HandlerFactory().buildHandler(new ArrayList<>(players), card);
        } else {
            throw new InvalidCardIdException();
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

    public void moveMotherNature(int playerId, int stepsChoice) {
        this.handler.motherNatureMovement(players.get(playerId), motherNature, stepsChoice);
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
    protected Player isThereWinner() {
        //The game ends immediately...

        //... when a player builds their last tower -> that player wins the game
        for (Player player : players) {
            if (player.getSchool().getTowersBoard().getNumPawns() == 0) return player;
        }

        //... when only 3 groups of islands remain on the table
        if (numOfGroupsOfIslands() == 3) return playerWithMostTowersOrProfessors();

        //... when the last student has been drawn from the bag
        if (bag.getPawns().size() == 0) return playerWithMostTowersOrProfessors();

        //... or should any player run out of Assistant Cards in their hand
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
            controller = new Controller(this.players,this.clouds,this.bag,this.numStudentToMove, this);
        }
        return controller;
    }

}
