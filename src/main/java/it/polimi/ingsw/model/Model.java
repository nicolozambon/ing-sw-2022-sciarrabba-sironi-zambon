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

    private List<String> winner;

    private boolean isLastRound;
    private boolean isTakeCloud;
    private boolean completeRule;

    private transient AnswerListenable answerListenable;

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

    public void setWizard(int playerId, Wizard wizard) {
        players.get(playerId).setWizard(wizard);
    }

    public void playAssistantCard(int playerId, int choice) throws CardException {
        players.get(playerId).playAssistantCard(choice);
        if (players.get(playerId).getAssistantCards().isEmpty()) isLastRound = true;
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

    public void addStudentsToClouds() {
        try {
            for (Cloud cloud : clouds) {
                for (int i = 0; i < numStudentToMove; i++) {
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
        if (bag.getNumPawns() > 0 && isLastRound) {
            boolean check = true;
            for (Player p : players) {
                if (p.getAssistantCards().isEmpty()) check = false;
            }
            if (check) isLastRound = false;
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

    public boolean isTakeCloud() {
        return isTakeCloud;
    }

    public boolean isLastRound() {
        return isLastRound;
    }

    public boolean isCompleteRule() {
        return completeRule;
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

    public boolean isThereWinner() {
        return winner.size() > 0;
    }

    private void update() {
        fireAnswer(new AnswerEvent("update", this));
        if (isThereWinner()) fireAnswer(new AnswerEvent("winner", winner));
    }

    protected void resumeFromDisk() {
        this.answerListenable = new AnswerListenable();
        this.winner = new ArrayList<>();
    }

    private int groupOfIslands() {
        int num = 12;
        for (Island island : islands) {
            if (island.isUnifyNext()) num--;
        }
        return num;
    }

    public void findWinner() {
        if (isLastRound) {
            List<Player> temp = new ArrayList<>(players);
            temp = temp.stream().sorted(Comparator.comparingInt(this::getPlayerNumTowers)).toList();
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
    }

    private int getPlayerWinningRanking(Player player) {
        School school = player.getSchool();
        return school.getProfessorsTable().getNumPawns() - school.getTowersBoard().getNumPawns();
    }

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
