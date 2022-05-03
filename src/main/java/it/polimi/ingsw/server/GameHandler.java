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

        launchUpdateAnswerEvent();

        while (!model.getIsThereWinner()) {

        }
    }

    public synchronized void launchUpdateAnswerEvent() {
        Gson gson = new Gson();

        for (String nickname : playersConnection.keySet()) {
            String currentPlayer = controller.getActivePlayer().getNickname();
            if (currentPlayer.equals(nickname)) {
                System.out.println("found current player!");
                String options = gson.toJson(controller.getOptions());
                playersConnection.get(nickname).send(new AnswerEvent("options", options));
            } else {
                String options = gson.toJson(new ArrayList<String>());
                playersConnection.get(nickname).send(new AnswerEvent("options", options));
            }
            ModelSerializable modelSerializable = new ModelSerializable(this.model);
            playersConnection.get(nickname).send(new AnswerEvent("update", modelSerializable));
        }
    }

    protected List<String> getPlayersNicknames() {
        return this.playersConnection.keySet().stream().toList();
    }

}
