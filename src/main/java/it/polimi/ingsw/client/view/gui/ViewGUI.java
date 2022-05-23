package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.view.gui.controllers.GUIController;
import it.polimi.ingsw.client.view.gui.controllers.LobbyController;
import it.polimi.ingsw.client.view.gui.controllers.StartMenuController;
import it.polimi.ingsw.events.AnswerEvent;
import it.polimi.ingsw.events.RequestEvent;
import it.polimi.ingsw.listenables.RequestListenable;
import it.polimi.ingsw.listenables.RequestListenableInterface;
import it.polimi.ingsw.listeners.AnswerListener;
import it.polimi.ingsw.listeners.RequestListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewGUI extends Application implements RequestListenableInterface, AnswerListener {

    private ClientConnection clientConnection;

    private RequestListenable requestListenable;

    private Map<String, GUIController> controllerMap;
    private Map<String, Scene> sceneMap;

    private Stage stage;
    private Scene currentScene;
    private GUIController currentController;

    private int id;
    private String nickname;

    public ViewGUI() {
        sceneMap =new HashMap<>();
        controllerMap = new HashMap<>();
        requestListenable =new RequestListenable();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.stage = stage;
        this.stage.setScene(currentScene);
        this.stage.setTitle("Start Menu");
        this.stage.show();
    }

    public void setup() {
        List<String> fxmlScenes = new ArrayList<>(List.of("startMenu", "lobby"));
        FXMLLoader loader;
        GUIController controller;
        try {
            for (String scene : fxmlScenes) {
                loader = new FXMLLoader(getClass().getResource("/assets/gui/scenes/" + scene + ".fxml"));
                sceneMap.put(scene, new Scene(loader.load()));
                controller = loader.getController();
                controller.setGUI(this);
                controllerMap.put(scene, controller);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentScene = sceneMap.get("startMenu");
        currentController = controllerMap.get("startMenu");
    }

    public void connect(String ip, String nickname) throws Exception {
        clientConnection = new ClientConnection(ip);
        new Thread (clientConnection).start();
        clientConnection.addAnswerListener(this);
        this.addRequestListener(clientConnection);
        this.fireRequest(new RequestEvent("nickname", this.id, nickname));
    }

    @Override
    public void addRequestListener(RequestListener requestListener) {
        this.requestListenable.addRequestListener(requestListener);
    }

    @Override
    public void removeRequestListener(RequestListener requestListener) {
        this.requestListenable.removeRequestListener(requestListener);
    }

    @Override
    public void fireRequest(RequestEvent requestEvent) throws Exception {
        this.requestListenable.fireRequest(requestEvent);
    }

    @Override
    public void onAnswerEvent(AnswerEvent answerEvent) {
        switch(answerEvent.getPropertyName()) {
            case "set_nickname" -> this.nickname = answerEvent.getMessage();
            case "lobby" -> {
                System.out.println(answerEvent.getOptions());
                LobbyController controller = (LobbyController) controllerMap.get("lobby");
                controller.setLobbyNames(answerEvent.getOptions());
            }
            case "set_id" -> this.id = answerEvent.getNum();
            case "options" -> clientConnection.send(new RequestEvent("first_player", 0, 3));
            case "update" -> System.out.println("todo");
            case "error" -> System.out.println("todo");
            case "stop" -> {
                System.out.println(answerEvent.getMessage());
                clientConnection.stopClient();
            }
            default -> System.out.println("Answer Error!");
        }
    }

    public void changeScene() {
        this.stage.setScene(sceneMap.get("lobby"));
        this.stage.setTitle("Lobby");
        this.stage.show();
    }

    @Override
    public void stop() {
        clientConnection.stopClient();
    }
}