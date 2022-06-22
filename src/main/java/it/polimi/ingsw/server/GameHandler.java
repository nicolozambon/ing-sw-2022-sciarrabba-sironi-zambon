package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHandler implements Runnable {

    private final Model model;
    private final Controller controller;
    private final Map<Integer, ConnectionHandler> playersConnection;

    public GameHandler(Map<String, ConnectionHandler> playersConnection, boolean completeRule) {
        Model model1;
        this.playersConnection = new HashMap<>();

        Path path = Paths.get("./saves/" + String.join("_", playersConnection.keySet().stream().sorted().toList()) + ".json");
        if (Files.exists(path)) {
            System.out.println("From savings");
            try {
                FileInputStream fileInputStream = new FileInputStream(path.toFile());
                model1 = new ModelBuilder().buildModel(fileInputStream);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                model1 = new ModelBuilder().buildModel(playersConnection.keySet().stream().toList(), false, completeRule);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Build");
            model1 = new ModelBuilder().buildModel(playersConnection.keySet().stream().toList(), false, completeRule);
        }

        this.model = model1;
        this.controller = model.getController();

        for (Player player : model.getPlayers()) {
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
        if (controller.getOptions().contains("chooseWizard")) launchAnswerEventEveryone(new AnswerEvent("options", controller.getOptions()));
        else launchAnswerEventCurrentPlayer(new AnswerEvent("options", controller.getOptions()));
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
        return model.getPlayers().stream().map(Player::getNickname).toList();
    }

    protected List<ConnectionHandler> getConnections() {
        return playersConnection.values().stream().toList();
    }

    protected void removeConnection(ConnectionHandler connectionHandler) {
        playersConnection.values().remove(connectionHandler);
    }
}
