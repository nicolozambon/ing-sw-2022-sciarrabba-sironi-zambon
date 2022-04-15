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

        this.playersConnection = new HashMap<>(playersConnection);

        this.modelBuilder = new ModelBuilder();

        this.model = modelBuilder.buildModel(this.playersConnection.keySet().stream().toList());
        System.out.println("GameHandler");
    }

    @Override
    public void run() {
        System.out.println("GameHandler");
        for (String name : playersConnection.keySet()) {
            playersConnection.get(name).setState(ConnectionAction.START);
        }
    }
}
