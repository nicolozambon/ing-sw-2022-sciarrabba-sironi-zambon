package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.ModelSerializable;
import it.polimi.ingsw.model.Player;

import java.io.*;
import java.util.*;

public class GameHandler implements Runnable {

    private Model model;
    private Controller controller;
    private Map<String, Connection> playersConnection;
    private VirtualView virtualView;

    public GameHandler(Map<String, Connection> playersConnection) {

        this.playersConnection = new HashMap<>(playersConnection);
        this.model = new ModelBuilder().buildModel(this.playersConnection.keySet().stream().toList());
        this.controller = model.getController();
        this.virtualView = new VirtualView();
    }

    @Override
    public void run() {
        this.virtualView.addRequestListener(this.controller);

        for (Player player : controller.getPlayersToPlay()) {
            playersConnection.get(player.getNickname()).send(new AnswerEvent("set_id", player.getId()));
        }

        for (String nickname : playersConnection.keySet()) {
            playersConnection.get(nickname).addRequestListener(virtualView);
        }

        this.virtualView.setGameHandler(this);
        launchUpdateAnswerEvent();

    }

    public synchronized void launchUpdateAnswerEvent() {
        String currentPlayer = controller.getActivePlayer().getNickname();
        System.out.println(currentPlayer);
        List<String> options = controller.getOptions();
        //System.out.println(options);

        ModelSerializable modelSerializable = new ModelSerializable(this.model);
        for (String nickname : playersConnection.keySet()) {
            if (currentPlayer.equals(nickname)) {
                playersConnection.get(nickname).send(new AnswerEvent("options", options));
            } else {
                playersConnection.get(nickname).send(new AnswerEvent("options", new ArrayList<String>()));
            }
            playersConnection.get(nickname).send(new AnswerEvent("update", modelSerializable));
        }
    }

    protected List<String> getPlayersNicknames() {
        return this.playersConnection.keySet().stream().toList();
    }

}
