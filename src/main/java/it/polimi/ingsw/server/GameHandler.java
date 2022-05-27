package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHandler implements Runnable {

    private final Model model;
    private final Controller controller;
    private final Map<Integer, ConnectionHandler> playersConnection;

    public GameHandler(Map<String, ConnectionHandler> playersConnection) {
        this.playersConnection = new HashMap<>();
        //TODO: change to false to have only 3 Character cards
        this.model = new ModelBuilder().buildModel(playersConnection.keySet().stream().toList(), true);
        this.controller = model.getController();
        for (Player player : controller.getPlayersToPlay()) {
            playersConnection.get(player.getNickname()).send(new AnswerEvent("set_id", player.getId()));
            this.playersConnection.put(player.getId(), playersConnection.get(player.getNickname()));
        }
    }

    @Override
    public void run() {
        VirtualView virtualView = new VirtualView(this);
        this.model.addAnswerListener(virtualView);

        virtualView.addRequestListener(this.controller);

        playersConnection.values().forEach(c -> c.addRequestListener(virtualView));

        launchAnswerEventEveryone(new AnswerEvent("update", this.model));
        launchAnswerEventEveryone(new AnswerEvent("options", controller.getOptions()));
    }

    public synchronized void launchAnswerEventEveryone(AnswerEvent answerEvent) {
        playersConnection.values().forEach(connectionHandler -> connectionHandler.send(answerEvent));
    }

    public void launchAnswerEventCurrentPlayer(AnswerEvent answerEvent) {
        int currentPlayerId = controller.getActivePlayer().getId();
        AnswerEvent waitEvent = new AnswerEvent("wait", controller.getActivePlayer().getNickname());
        for (int id : playersConnection.keySet()) {
            if (currentPlayerId == id) {
                playersConnection.get(id).send(answerEvent);
            } else {
                playersConnection.get(id).send(waitEvent);
            }
        }
    }

    public synchronized void launchAnswerEventPlayer(int playerId, AnswerEvent answerEvent) {
        playersConnection.get(playerId).send(answerEvent);
    }

    protected List<String> getNicknames() {
        return playersConnection.values().stream().map(ConnectionHandler::getNickname).toList();
    }

    protected List<ConnectionHandler> getConnections() {
        return playersConnection.values().stream().toList();
    }
}
