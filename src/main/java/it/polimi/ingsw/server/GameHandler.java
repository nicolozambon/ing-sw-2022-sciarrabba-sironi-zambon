package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.json.HandlerDeserializer;
import it.polimi.ingsw.json.HandlerSerializer;
import it.polimi.ingsw.model.Handler;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ModelBuilder;
import it.polimi.ingsw.model.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles a single match, it contains all players ConnectionHandler, it starts the game (newly created or
 * resumed from a saving file), subscribe every listener to the correct listenable
 */
public class GameHandler implements Runnable {

    /**
     * Model of the match
     */
    private final Model model;

    /**
     * Controller of the Model
     */
    private final Controller controller;

    /**
     * Map containing unique player's match id and relative ConnectionHandler
     */
    private final Map<Integer, ConnectionHandler> playersConnection;

    /**
     * Name of the save file
     */
    private final String saveName;

    /**
     * Gson object to manage Model serialization/deserialization
     */
    private final Gson gson;

    /**
     * Constructor build or resume model for the match, initialize all attributes
     * @param playersConnection Map containing player's nickname - ConnectionHandler
     * @param completeRule if the rule for the game will be complete or not
     */
    protected GameHandler(Map<String, ConnectionHandler> playersConnection, boolean completeRule) {
        Model model1;
        this.playersConnection = new HashMap<>();
        this.saveName = String.join("_", playersConnection.keySet().stream().sorted().toList()) + ".json";

        Path path = Paths.get("./saves/" + this.saveName);
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

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Handler.class, new HandlerSerializer())
                .registerTypeAdapter(Handler.class, new HandlerDeserializer())
                .create();
    }

    /**
     * Run method to be executed by a Thread, subscribe every listener to the correct listenable, sends the first
     * AnswerEvents
     */
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

    /**
     * Sends the AnswerEvent to every ConnectionHandler
     * @param answerEvent the AnswerEvent to be sent
     * @see AnswerEvent
     */
    protected synchronized void launchAnswerEventEveryone(AnswerEvent answerEvent) {
        playersConnection.values().forEach(connectionHandler -> connectionHandler.send(answerEvent));

        //Delete save file
        if (answerEvent.getPropertyName().equals("winner")) {
            System.out.println("deleting...");
            try {
                Files.delete(Paths.get("./saves/" + saveName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends the AnswerEvent to the current player's ConnectionHandler
     * @param answerEvent the AnswerEvent to be sent
     * @see AnswerEvent
     */
    protected void launchAnswerEventCurrentPlayer(AnswerEvent answerEvent) {
        int currentPlayerId = controller.getActivePlayer().getId();
        AnswerEvent waitEvent = new AnswerEvent("wait", controller.getActivePlayer().getNickname());
        for (int id : playersConnection.keySet()) {
            if (currentPlayerId == id) {
                playersConnection.get(id).send(answerEvent);
            } else {
                playersConnection.get(id).send(waitEvent);
            }
        }

        //Save file for future resuming
        if (answerEvent.getPropertyName().equals("options")) {
            try {
                FileWriter saveFile = new FileWriter("./saves/" + saveName);
                saveFile.write(this.gson.toJson(this.model));
                saveFile.close();
            } catch (IOException e) {
                System.out.println("Error in saving the game!");
                //e.printStackTrace();
            }
        }
    }

    /**
     * Sends the AnswerEvent to specified player's ConnectionHandler
     * @param playerId the id of the player that will receive the AnswerEvent
     * @param answerEvent the AnswerEvent to be sent
     * @see AnswerEvent
     */
    protected synchronized void launchAnswerEventPlayer(int playerId, AnswerEvent answerEvent) {
        playersConnection.get(playerId).send(answerEvent);
    }

    /**
     * Returns the nicknames of the players currently in the match
     * @return the List of player's nicknames
     */
    protected List<String> getNicknames() {
        return model.getPlayers().stream().map(Player::getNickname).toList();
    }

    /**
     * Returns the ConnectionHandlers of the players currently in the match
     * @return the List of player's connections
     */
    protected List<ConnectionHandler> getConnections() {
        return playersConnection.values().stream().toList();
    }

    /**
     * Removes the specified ConnectionHandler from the match
     * @param connectionHandler the ConnectionHandler to be removed
     */
    protected void removeConnection(ConnectionHandler connectionHandler) {
        playersConnection.values().remove(connectionHandler);
    }
}
