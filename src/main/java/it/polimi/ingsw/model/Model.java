package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.model.card.CharacterCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Model {

    private final List<Player> players;
    private final List<Island> islands;
    private final List<Cloud> clouds;
    private final MotherNature motherNature;
    private final List<CharacterCard> characterCards;
    private int coinReserve;
    private final Board<Professor> startingProfessorBoard;
    private final StudentBag bag;
    private final int numStudentToMove;

    Handler handler;


    private Map<Integer, Integer> numStudentToMovePerPlayer = new HashMap<>(){{
        this.put(2, 3);
        this.put(3, 4);
        //this.put(4, 3);
    }};

    protected Model(List<Player> players, List<Island> islands, List<Cloud> clouds, MotherNature motherNature,
                 List<CharacterCard> characterCards, int coinReserve, Board<Professor> startingProfessorBoard, StudentBag bag) {
        this.players = players;
        this.islands = islands;
        this.clouds = clouds;
        this.motherNature = motherNature;
        this.characterCards = characterCards;
        this.coinReserve = coinReserve;
        this.startingProfessorBoard = startingProfessorBoard;
        this.bag = bag;

        handler = new Handler(new ArrayList<>(players));

        this.numStudentToMove = numStudentToMovePerPlayer.get(this.players.size());
    }

    public void playAssistantCard(int playerId, int choice) {
        players.get(playerId).playAssistantCard(choice);
    }

    public void playCharacterCard(int playerId, int choice) {
        CharacterCard card =    characterCards
                                .stream()
                                .filter(x -> x.getId() == choice)
                                .findFirst()
                                .get();

        card.incrementCoinCost();

        players.get(playerId).playCharacterCard(card);
        this.handler = new HandlerFactory(card).buildHandler(new ArrayList<>(players));
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

    public void getStudentsFromCloud(int playerId, int choice) {
        players.get(playerId).takeStudentsFromCloud(clouds.get(choice));
    }

    public void extraAction(int ... values) {
        this.handler.extraAction(players.get(0), this, values);
    }

    private void resetHandler() {
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
     * Methos isThereWinner checks if there is at least one condition for which the game ends.
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
}
