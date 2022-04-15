package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ActionPhase;
import it.polimi.ingsw.enums.ConnectionAction;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;

import java.util.*;

public class GameHandler implements Runnable {

    private Model model;
    private ModelBuilder modelBuilder;
    Map<String, InitialClientConnection> playersConnection;

    public GameHandler(Map<String, InitialClientConnection> playersConnection) {
        this.playersConnection = playersConnection;
        model = modelBuilder.buildModel(playersConnection.keySet().stream().toList());
    }

    @Override
    public void run() {
        System.out.println("GameHandler");
        for (String name : playersConnection.keySet()) {
            playersConnection.get(name).setState(ConnectionAction.START);
        }
    }
}
