package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.listenables.AnswerListenable;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.ModelSerializable;
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
        Gson gson = new Gson();
        this.model.addAnswerListener(this.virtualView);

        this.virtualView.addRequestListener(this.controller);

        for (Player player : controller.getPlayersToPlay()) {
            playersConnection.get(player.getNickname()).send(new AnswerEvent("set_id", player.getId()));
        }

        for (String nickname : playersConnection.keySet()) {
            playersConnection.get(nickname).addRequestListener(virtualView);
        }

        this.virtualView.setGameHandler(this);
        this.launchUpdateAnswerEvent(new AnswerEvent("update", gson.toJson(this.model)));
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
                playersConnection.get(nickname).send(new AnswerEvent("options", options));
            } else {
                playersConnection.get(nickname).send(new AnswerEvent("options", new ArrayList<String>()));
            }
        }
    }

    protected List<String> getPlayersNicknames() {
        return this.playersConnection.keySet().stream().toList();
    }

}
