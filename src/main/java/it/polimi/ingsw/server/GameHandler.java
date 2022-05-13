package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.Player;

import java.util.*;

public class GameHandler implements Runnable {

    private final Model model;
    private final Controller controller;
    private final Map<String, Connection> playersConnection;
    private final VirtualView virtualView;

    public GameHandler(Map<String, Connection> playersConnection) {
        this.playersConnection = new HashMap<>(playersConnection);
        this.model = new ModelBuilder().buildModel(this.playersConnection.keySet().stream().toList());
        this.controller = model.getController();
        this.virtualView = new VirtualView();
    }

    @Override
    public void run() {
        this.model.addAnswerListener(this.virtualView);

        this.virtualView.addRequestListener(this.controller);

        for (Player player : controller.getPlayersToPlay()) {
            playersConnection.get(player.getNickname()).send(new AnswerEvent("set_id", player.getId()));
            playersConnection.get(player.getNickname()).send(new AnswerEvent("set_nickname", player.getNickname()));
        }

        for (String nickname : playersConnection.keySet()) {
            playersConnection.get(nickname).addRequestListener(virtualView);
        }

        this.virtualView.setGameHandler(this);
        this.launchUpdateAnswerEvent(new AnswerEvent("update", this.model));
        this.launchOptionsAnswerEvent();
    }

    public synchronized void launchUpdateAnswerEvent(AnswerEvent answerEvent) {
        for (String nickname : playersConnection.keySet()) {
            playersConnection.get(nickname).send(answerEvent);
        }
    }

    public synchronized void launchOptionsAnswerEvent() {
        String currentPlayer = controller.getActivePlayer().getNickname();
        List<String> options = controller.getOptions();
        for (String nickname : playersConnection.keySet()) {
            if (currentPlayer.equals(nickname)) {
                System.out.println("sending options");
                playersConnection.get(nickname).send(new AnswerEvent("options", options));
            } else {
                //playersConnection.get(nickname).send(new AnswerEvent("options", gson.toJson(new ArrayList<String>()) ));
                playersConnection.get(nickname).send(new AnswerEvent("wait"));
            }
        }
    }

    public synchronized void launchErrorAnswerEvent(AnswerEvent answerEvent) {
        playersConnection.get(controller.getActivePlayer().getNickname()).send(answerEvent);
    }

    protected List<String> getPlayersNicknames() {
        return this.playersConnection.keySet().stream().toList();
    }

    protected Map<String, Connection> getPlayersConnection() {
        return new HashMap<>(playersConnection);
    }

}
