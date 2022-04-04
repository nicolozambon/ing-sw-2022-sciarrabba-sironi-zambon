package it.polimi.ingsw.model;

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

    public Model(List<Player> players, List<Island> islands, List<Cloud> clouds, MotherNature motherNature,
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

    public void extraAction(int value) {
        this.handler.extraAction(value, this);
    }

    private void resetHandler() {
        this.handler = new Handler(this.players);
    }

    protected List<Island> getIslands(){
        return new ArrayList<>(this.islands);
    }
}
