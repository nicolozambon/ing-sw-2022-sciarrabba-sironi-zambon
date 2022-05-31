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

    private Controller controller;
    private Handler handler;

    private Player winner;

    private transient AnswerListenable answerListenable;

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
        this.winner = null;

        this.answerListenable = new AnswerListenable();
    }

    public void setWizard(int playerId, Wizard wizard) {
        players.get(playerId).setWizard(wizard);
    }

    public void playAssistantCard(int playerId, int choice) throws CardException {
        players.get(playerId).playAssistantCard(choice);
        update();
    }


    public void playCharacterCard(int playerId, int choice) throws NotEnoughCoinsException, CardException {
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
    }

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

    public void moveMotherNature(int playerId, int stepsChoice) throws MotherNatureStepsException {
        try {
            this.handler.motherNatureMovement(players.get(playerId), motherNature, stepsChoice);
        } catch (WinnerException e) {
            //e.printStackTrace();
            this.winner = players.get(e.getPlayerId());
            System.out.println("Winner is player " + players.get(e.getPlayerId()).getNickname());
        } finally {
            update();
        }

    }

    public void addStudentsToClouds() {
        for (Cloud cloud : clouds) {
            for (int i = 0; i < numStudentToMove; i++) {
                bag.extractStudentAndMove(cloud);
            }
        }
        update();
    }

    public void takeStudentsFromCloud(int playerId, int choice) throws CloudException {
        if (choice < 0 || choice > clouds.size() - 1 || clouds.get(choice).getNumPawns() == 0) throw new CloudException();
        players.get(playerId).takeStudentsFromCloud(clouds.get(choice));
        update();
    }

    public void extraAction(int playerId, int ... values) throws Exception {
        this.handler.extraAction(players.get(playerId), this, values);
        update();
    }

    protected void returnStudentsToBag(Color color, int num){
        for (Player player : players) {
            player.returnStudentsToBag(bag, color, num);
        }
    }

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

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    protected int getCoinReserve() {
        return this.coinReserve;
    }

    protected List<Island> getIslands(){
        return new ArrayList<>(this.islands);
    }

    public Controller getController() {
        if (controller == null) {
            controller = new Controller(new ArrayList<>(this.players), this, this.numStudentToMove);
        }
        return controller;
    }

    protected Handler getHandler() {
        return handler;
    }

    public int getCharacterCardIdFromHandler() {
        return this.handler.getCardId();
    }

    public void resetHandler() {
        this.handler = new Handler(this.players);
    }

    public Player getWinner() {
        return winner;
    }

    private void update() {
        fireAnswer(new AnswerEvent("update", this));
        if (winner != null) fireAnswer(new AnswerEvent("winner", winner.getNickname()));
    }

    protected void resetAnswerListenable() {
        this.answerListenable = new AnswerListenable();
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
