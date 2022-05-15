package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GameHandler implements Runnable {

    private final Model model;
    private final Controller controller;
    private final Map<Integer, Connection> playersConnection;
    private final VirtualView virtualView;

    public GameHandler(Map<String, Connection> playersConnection) {
        this.playersConnection = new HashMap<>();
        this.model = new ModelBuilder().buildModel(playersConnection.keySet().stream().toList());
        this.controller = model.getController();
        for (Player player : controller.getPlayersToPlay()) {
            playersConnection.get(player.getNickname()).send(new AnswerEvent("set_id", player.getId()));
            playersConnection.get(player.getNickname()).send(new AnswerEvent("set_nickname", player.getNickname()));
            this.playersConnection.put(player.getId(), playersConnection.get(player.getNickname()));
        }
        this.virtualView = new VirtualView();
    }

    @Override
    public void run() {
        this.model.addAnswerListener(this.virtualView);

        this.virtualView.addRequestListener(this.controller);

        playersConnection.values().forEach(c -> c.addRequestListener(virtualView));

        this.virtualView.setGameHandler(this);
        this.launchUpdateAnswerEvent(new AnswerEvent("update", this.model));
        this.launchOptionsAnswerEvent();
    }

    public synchronized void launchUpdateAnswerEvent(AnswerEvent answerEvent) {
        playersConnection.values().forEach(connection -> connection.send(answerEvent));
    }

    public synchronized void launchOptionsAnswerEvent() {
        int currentPlayerId = controller.getActivePlayer().getId();
        List<String> options = controller.getOptions();
        for (int id : playersConnection.keySet()) {
            if (currentPlayerId == id) {
                playersConnection.get(id).send(new AnswerEvent("options", options));
            } else {
                playersConnection.get(id).send(new AnswerEvent("wait"));
            }
        }
    }

    public synchronized void launchErrorAnswerEvent(int playerId, AnswerEvent answerEvent) {
        playersConnection.get(playerId).send(answerEvent);
    }

    protected List<String> getNicknames() {
        return playersConnection.values().stream().map(Connection::getNickname).toList();
    }

    protected List<Connection> getConnections() {
        return playersConnection.values().stream().toList();
    }

}
